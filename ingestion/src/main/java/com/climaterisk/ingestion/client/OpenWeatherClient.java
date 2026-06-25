package com.climaterisk.ingestion.client;

import com.climaterisk.ingestion.config.OpenWeatherProperties;
import com.climaterisk.ingestion.dto.OpenWeatherAirPollutionResponse;
import com.climaterisk.ingestion.dto.OpenWeatherCurrentResponse;
import com.climaterisk.ingestion.dto.OpenWeatherForecastResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * OpenWeatherMap client for current conditions, 5-day forecast, and
 * air pollution data.
 * <p>
 * Uses Spring WebFlux WebClient for non-blocking I/O. Each method is
 * wrapped with Resilience4j retry and circuit breaker.
 * </p>
 */
@Component
public class OpenWeatherClient {

    private final WebClient webClient;
    private final OpenWeatherProperties properties;

    public OpenWeatherClient(WebClient.Builder webClientBuilder, OpenWeatherProperties properties) {
        this.properties = properties;
        this.webClient = webClientBuilder
                .baseUrl(properties.getBaseUrl())
                .build();
    }

    /**
     * Fetches current weather for a coordinate.
     */
    @Retry(name = "openWeather", fallbackMethod = "getCurrentFallback")
    @CircuitBreaker(name = "openWeather", fallbackMethod = "getCurrentFallback")
    public Mono<OpenWeatherCurrentResponse> getCurrent(double lat, double lon) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/weather")
                        .queryParam("lat", lat)
                        .queryParam("lon", lon)
                        .queryParam("appid", properties.getApiKey())
                        .queryParam("units", "metric")
                        .build())
                .retrieve()
                .bodyToMono(OpenWeatherCurrentResponse.class);
    }

    /**
     * Fetches 5-day/3-hour forecast for a coordinate.
     */
    @Retry(name = "openWeather", fallbackMethod = "getForecastFallback")
    @CircuitBreaker(name = "openWeather", fallbackMethod = "getForecastFallback")
    public Mono<OpenWeatherForecastResponse> getForecast(double lat, double lon) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/forecast")
                        .queryParam("lat", lat)
                        .queryParam("lon", lon)
                        .queryParam("appid", properties.getApiKey())
                        .queryParam("units", "metric")
                        .build())
                .retrieve()
                .bodyToMono(OpenWeatherForecastResponse.class);
    }

    /**
     * Fetches air pollution data for a coordinate.
     */
    @Retry(name = "openWeather", fallbackMethod = "getAirPollutionFallback")
    @CircuitBreaker(name = "openWeather", fallbackMethod = "getAirPollutionFallback")
    public Mono<OpenWeatherAirPollutionResponse> getAirPollution(double lat, double lon) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/air_pollution")
                        .queryParam("lat", lat)
                        .queryParam("lon", lon)
                        .queryParam("appid", properties.getApiKey())
                        .build())
                .retrieve()
                .bodyToMono(OpenWeatherAirPollutionResponse.class);
    }

    // --- Fallbacks ---
    private Mono<OpenWeatherCurrentResponse> getCurrentFallback(double lat, double lon, Exception e) {
        return Mono.empty();
    }

    private Mono<OpenWeatherForecastResponse> getForecastFallback(double lat, double lon, Exception e) {
        return Mono.empty();
    }

    private Mono<OpenWeatherAirPollutionResponse> getAirPollutionFallback(double lat, double lon, Exception e) {
        return Mono.empty();
    }
}