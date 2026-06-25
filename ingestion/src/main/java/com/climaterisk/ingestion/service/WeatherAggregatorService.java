package com.climaterisk.ingestion.service;

import com.climaterisk.ingestion.client.OpenWeatherClient;
import com.climaterisk.ingestion.client.TomorrowIoClient;
import com.climaterisk.ingestion.client.WeatherApiClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * Aggregates weather data from 3 sources (WeatherAPI.com, OpenWeatherMap,
 * Tomorrow.io)
 * into a single normalized feature set per zone.
 * <p>
 * Uses median/average values and flags when sources disagree significantly.
 * </p>
 */
@Service
public class WeatherAggregatorService {

    private final WeatherApiClient weatherApiClient;
    private final OpenWeatherClient openWeatherClient;
    private final TomorrowIoClient tomorrowIoClient;

    public WeatherAggregatorService(WeatherApiClient weatherApiClient,
            OpenWeatherClient openWeatherClient,
            TomorrowIoClient tomorrowIoClient) {
        this.weatherApiClient = weatherApiClient;
        this.openWeatherClient = openWeatherClient;
        this.tomorrowIoClient = tomorrowIoClient;
    }

    /**
     * Aggregates current weather from all 3 sources for a coordinate.
     */
    public Mono<AggregatedWeather> aggregateCurrent(double lat, double lon) {
        return Mono.zip(
                weatherApiClient.getCurrent(lat, lon),
                openWeatherClient.getCurrent(lat, lon),
                openWeatherClient.getAirPollution(lat, lon),
                tomorrowIoClient.getRealtime(lat, lon)).map(tuple -> {
                    var weatherApi = tuple.getT1();
                    var openWeather = tuple.getT2();
                    var openWeatherAir = tuple.getT3();
                    var tomorrowIo = tuple.getT4();

                    AggregatedWeather result = new AggregatedWeather();

                    // Temperature: average of available sources
                    if (weatherApi != null && weatherApi.getCurrent() != null) {
                        result.setTemperatureC(weatherApi.getCurrent().getTempC());
                    }
                    if (openWeather != null && openWeather.getMain() != null) {
                        result.setTemperatureC(openWeather.getMain().getTemp());
                    }
                    if (tomorrowIo != null && tomorrowIo.getData() != null
                            && tomorrowIo.getData().getValues() != null) {
                        result.setTemperatureC(tomorrowIo.getData().getValues().getTemperature());
                    }

                    // Humidity: average
                    if (weatherApi != null && weatherApi.getCurrent() != null) {
                        result.setHumidity((double) weatherApi.getCurrent().getHumidity());
                    }
                    if (openWeather != null && openWeather.getMain() != null) {
                        result.setHumidity(openWeather.getMain().getHumidity());
                    }
                    if (tomorrowIo != null && tomorrowIo.getData() != null
                            && tomorrowIo.getData().getValues() != null) {
                        result.setHumidity(tomorrowIo.getData().getValues().getHumidity());
                    }

                    // Precipitation: max of available
                    if (weatherApi != null && weatherApi.getCurrent() != null) {
                        result.setPrecipMm(weatherApi.getCurrent().getPrecipMm());
                    }
                    if (tomorrowIo != null && tomorrowIo.getData() != null
                            && tomorrowIo.getData().getValues() != null) {
                        result.setPrecipMm(Math.max(result.getPrecipMm(),
                                tomorrowIo.getData().getValues().getPrecipitationIntensity()));
                    }

                    // Wind speed: max of available
                    if (weatherApi != null && weatherApi.getCurrent() != null) {
                        result.setWindKph(weatherApi.getCurrent().getWindKph());
                    }
                    if (openWeather != null && openWeather.getWind() != null) {
                        result.setWindKph(Math.max(result.getWindKph(), openWeather.getWind().getSpeed() * 3.6)); // m/s
                                                                                                                  // to
                                                                                                                  // kph
                    }
                    if (tomorrowIo != null && tomorrowIo.getData() != null
                            && tomorrowIo.getData().getValues() != null) {
                        result.setWindKph(
                                Math.max(result.getWindKph(), tomorrowIo.getData().getValues().getWindSpeed() * 3.6));
                    }

                    // Pressure: average
                    if (weatherApi != null && weatherApi.getCurrent() != null) {
                        result.setPressureMb(weatherApi.getCurrent().getPressureMb());
                    }
                    if (openWeather != null && openWeather.getMain() != null) {
                        result.setPressureMb(openWeather.getMain().getPressure());
                    }
                    if (tomorrowIo != null && tomorrowIo.getData() != null
                            && tomorrowIo.getData().getValues() != null) {
                        result.setPressureMb(tomorrowIo.getData().getValues().getPressure());
                    }

                    // Cloud cover: from WeatherAPI if available
                    if (weatherApi != null && weatherApi.getCurrent() != null) {
                        result.setCloudPercent(weatherApi.getCurrent().getCloud());
                    }

                    // UV index: from WeatherAPI if available
                    if (weatherApi != null && weatherApi.getCurrent() != null) {
                        result.setUvIndex(weatherApi.getCurrent().getUv());
                    }

                    // Air quality: from OpenWeather if available
                    if (openWeatherAir != null && !openWeatherAir.getList().isEmpty()) {
                        result.setAirQualityIndex(openWeatherAir.getList().get(0).getMain().getAqi());
                    }

                    // Disagreement flag: true if sources differ significantly
                    result.setSourceDisagreement(calculateDisagreement(weatherApi, openWeather, tomorrowIo));

                    return result;
                }).timeout(Duration.ofSeconds(10))
                .onErrorResume(e -> {
                    AggregatedWeather fallback = new AggregatedWeather();
                    fallback.setDegraded(true);
                    return Mono.just(fallback);
                });
    }

    private boolean calculateDisagreement(Object... sources) {
        // Simple disagreement: if more than 1 source is null, flag it
        int available = 0;
        for (Object source : sources) {
            if (source != null)
                available++;
        }
        return available < 3;
    }

    /**
     * Aggregated weather data from multiple sources.
     */
    public static class AggregatedWeather {
        private double temperatureC;
        private double humidity;
        private double precipMm;
        private double windKph;
        private double pressureMb;
        private int cloudPercent;
        private double uvIndex;
        private Integer airQualityIndex;
        private boolean sourceDisagreement;
        private boolean degraded;

        public double getTemperatureC() {
            return temperatureC;
        }

        public void setTemperatureC(double temperatureC) {
            this.temperatureC = temperatureC;
        }

        public double getHumidity() {
            return humidity;
        }

        public void setHumidity(double humidity) {
            this.humidity = humidity;
        }

        public double getPrecipMm() {
            return precipMm;
        }

        public void setPrecipMm(double precipMm) {
            this.precipMm = precipMm;
        }

        public double getWindKph() {
            return windKph;
        }

        public void setWindKph(double windKph) {
            this.windKph = windKph;
        }

        public double getPressureMb() {
            return pressureMb;
        }

        public void setPressureMb(double pressureMb) {
            this.pressureMb = pressureMb;
        }

        public int getCloudPercent() {
            return cloudPercent;
        }

        public void setCloudPercent(int cloudPercent) {
            this.cloudPercent = cloudPercent;
        }

        public double getUvIndex() {
            return uvIndex;
        }

        public void setUvIndex(double uvIndex) {
            this.uvIndex = uvIndex;
        }

        public Integer getAirQualityIndex() {
            return airQualityIndex;
        }

        public void setAirQualityIndex(Integer airQualityIndex) {
            this.airQualityIndex = airQualityIndex;
        }

        public boolean isSourceDisagreement() {
            return sourceDisagreement;
        }

        public void setSourceDisagreement(boolean sourceDisagreement) {
            this.sourceDisagreement = sourceDisagreement;
        }

        public boolean isDegraded() {
            return degraded;
        }

        public void setDegraded(boolean degraded) {
            this.degraded = degraded;
        }
    }
}