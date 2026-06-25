package com.climaterisk.ingestion.service;

import com.climaterisk.common.entity.MicroZone;
import com.climaterisk.common.repository.MicroZoneRepository;
import com.climaterisk.ingestion.client.NasaPowerClient;
import com.climaterisk.ingestion.dto.NasaPowerResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * Builds feature vectors for each zone by combining static geospatial
 * features with dynamic weather and climatology data.
 * <p>
 * Static features: elevation, imperviousness, NDVI proxy, drainage proxy.
 * Dynamic features: temperature, humidity, precipitation, wind, pressure,
 * NASA POWER historical baseline anomalies.
 * </p>
 */
@Component
public class ZoneFeatureBuilder {

    private final WeatherAggregatorService weatherAggregatorService;
    private final NasaPowerClient nasaPowerClient;
    private final MicroZoneRepository microZoneRepository;

    public ZoneFeatureBuilder(WeatherAggregatorService weatherAggregatorService,
            NasaPowerClient nasaPowerClient,
            MicroZoneRepository microZoneRepository) {
        this.weatherAggregatorService = weatherAggregatorService;
        this.nasaPowerClient = nasaPowerClient;
        this.microZoneRepository = microZoneRepository;
    }

    /**
     * Builds a feature vector for a specific zone.
     *
     * @param zoneId H3 index of the zone
     * @return Mono of ZoneFeatureVector
     */
    public Mono<ZoneFeatureVector> buildForZone(String zoneId) {
        MicroZone zone = microZoneRepository.findByH3Index(zoneId)
                .orElseThrow(() -> new IllegalArgumentException("Zone not found: " + zoneId));

        double lat = zone.getCentroidLat();
        double lon = zone.getCentroidLon();

        return weatherAggregatorService.aggregateCurrent(lat, lon)
                .timeout(Duration.ofSeconds(15))
                .onErrorResume(e -> {
                    // Return degraded feature vector on timeout/error
                    return Mono.just(new WeatherAggregatorService.AggregatedWeather());
                })
                .flatMap(weather -> {
                    // Fetch NASA POWER climatology for baseline (last 30 days)
                    String endDate = java.time.LocalDate.now()
                            .format(java.time.format.DateTimeFormatter.BASIC_ISO_DATE);
                    String startDate = java.time.LocalDate.now().minusDays(30)
                            .format(java.time.format.DateTimeFormatter.BASIC_ISO_DATE);

                    return nasaPowerClient.getClimatology(lat, lon, startDate, endDate)
                            .map(nasaData -> combineFeatures(zone, weather, nasaData))
                            .onErrorResume(e -> Mono.just(combineFeatures(zone, weather, null)));
                });
    }

    /**
     * Combines static zone features with dynamic weather and NASA data.
     */
    private ZoneFeatureVector combineFeatures(MicroZone zone,
            WeatherAggregatorService.AggregatedWeather weather,
            NasaPowerResponse nasaData) {
        Map<String, Object> staticFeatures = new HashMap<>();
        Map<String, Object> dynamicFeatures = new HashMap<>();

        // Static features (from zone metadata - placeholder values for MVP)
        staticFeatures.put("elevation_m", 0.0); // TODO: integrate DEM API
        staticFeatures.put("imperviousness", 0.5); // TODO: fetch from satellite data
        staticFeatures.put("ndvi_proxy", 0.3); // TODO: calculate from NASA GIBS
        staticFeatures.put("drainage_proxy", 0.4); // TODO: derive from distance to water

        // Dynamic weather features
        dynamicFeatures.put("temperature_c", weather.getTemperatureC());
        dynamicFeatures.put("humidity", weather.getHumidity());
        dynamicFeatures.put("precip_mm", weather.getPrecipMm());
        dynamicFeatures.put("wind_kph", weather.getWindKph());
        dynamicFeatures.put("pressure_mb", weather.getPressureMb());
        dynamicFeatures.put("cloud_percent", weather.getCloudPercent());
        dynamicFeatures.put("uv_index", weather.getUvIndex());
        dynamicFeatures.put("air_quality_index", weather.getAirQualityIndex());
        dynamicFeatures.put("source_disagreement", weather.isSourceDisagreement());
        dynamicFeatures.put("degraded", weather.isDegraded());

        // NASA POWER climatology features (historical baseline)
        if (nasaData != null && nasaData.getProperties() != null) {
            if (nasaData.getProperties().getT2m() != null) {
                dynamicFeatures.put("temp_anomaly_c", calculateAnomaly(
                        weather.getTemperatureC(),
                        nasaData.getProperties().getT2m().getData()));
            }
            if (nasaData.getProperties().getPrectot() != null) {
                dynamicFeatures.put("precip_anomaly_mm", calculateAnomaly(
                        weather.getPrecipMm(),
                        nasaData.getProperties().getPrectot().getData()));
            }
        } else {
            dynamicFeatures.put("temp_anomaly_c", 0.0);
            dynamicFeatures.put("precip_anomaly_mm", 0.0);
        }

        return new ZoneFeatureVector(
                zone.getH3Index(),
                zone.getCity(),
                zone.getCountry(),
                staticFeatures,
                dynamicFeatures,
                System.currentTimeMillis());
    }

    /**
     * Calculates anomaly: current value - historical average.
     */
    private double calculateAnomaly(double currentValue, Map<String, Double> historicalData) {
        if (historicalData == null || historicalData.isEmpty()) {
            return 0.0;
        }
        double avg = historicalData.values().stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0);
        return currentValue - avg;
    }

    /**
     * Immutable feature vector for a zone.
     */
    public record ZoneFeatureVector(
            String h3Index,
            String city,
            String country,
            Map<String, Object> staticFeatures,
            Map<String, Object> dynamicFeatures,
            long timestamp) {
    }
}