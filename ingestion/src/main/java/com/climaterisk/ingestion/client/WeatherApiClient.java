package com.climaterisk.ingestion.client;

import com.climaterisk.ingestion.config.WeatherApiProperties;
import com.climaterisk.ingestion.dto.WeatherApiCurrentResponse;
import com.climaterisk.ingestion.dto.WeatherApiForecastResponse;
import com.climaterisk.ingestion.dto.WeatherApiHistoryResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * WeatherAPI.com client for current conditions, 3-day forecast, and
 * historical weather data.
 * <p>
 * Uses Spring WebFlux WebClient for non-blocking I/O. Each method is
 * wrapped with Resilience4j retry (3 attempts, exponential backoff) and
 * circuit breaker (failure threshold 50%, 30s open state).
 * </p>
 */
@Component
public class WeatherApiClient {

    private final WebClient webClient;
    private final WeatherApiProperties properties;

    public WeatherApiClient(WebClient.Builder webClientBuilder, WeatherApiProperties properties) {
        this.properties = properties;
        this.webClient = webClientBuilder
                .baseUrl(properties.getBaseUrl())
                .build();
    }

    /**
     * Fetches current weather conditions for a coordinate.
     *
     * @param lat latitude (WGS84)
     * @param lon longitude (WGS84)
     * @return Mono of current weather response
     */
    @Retry(name = "weatherApi", fallbackMethod = "getCurrentFallback")
    @CircuitBreaker(name = "weatherApi", fallbackMethod = "getCurrentFallback")
    public Mono<WeatherApiCurrentResponse> getCurrent(double lat, double lon) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/current.json")
                        .queryParam("key", properties.getApiKey())
                        .queryParam("q", lat + "," + lon)
                        .build())
                .retrieve()
                .bodyToMono(WeatherApiCurrentResponse.class);
    }

    /**
     * Fetches 3-day hourly forecast for a coordinate.
     *
     * @param lat  latitude (WGS84)
     * @param lon  longitude (WGS84)
     * @param days number of forecast days (1-3 for free tier)
     * @return Mono of forecast response
     */
    @Retry(name = "weatherApi", fallbackMethod = "getForecastFallback")
    @CircuitBreaker(name = "weatherApi", fallbackMethod = "getForecastFallback")
    public Mono<WeatherApiForecastResponse> getForecast(double lat, double lon, int days) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/forecast.json")
                        .queryParam("key", properties.getApiKey())
                        .queryParam("q", lat + "," + lon)
                        .queryParam("days", Math.min(days, 3))
                        .build())
                .retrieve()
                .bodyToMono(WeatherApiForecastResponse.class);
    }

    /**
     * Fetches historical weather for a specific date (last 7 days free).
     *
     * @param lat  latitude (WGS84)
     * @param lon  longitude (WGS84)
     * @param date date in YYYY-MM-DD format
     * @return Mono of history response
     */
    @Retry(name = "weatherApi", fallbackMethod = "getHistoryFallback")
    @CircuitBreaker(name = "weatherApi", fallbackMethod = "getHistoryFallback")
    public Mono<WeatherApiHistoryResponse> getHistory(double lat, double lon, String date) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/history.json")
                        .queryParam("key", properties.getApiKey())
                        .queryParam("q", lat + "," + lon)
                        .queryParam("dt", date)
                        .build())
                .retrieve()
                .bodyToMono(WeatherApiHistoryResponse.class);
    }

    // --- Fallback methods (return empty Mono on failure) ---

    private Mono<WeatherApiCurrentResponse> getCurrentFallback(double lat, double lon, Exception e) {
        return Mono.empty();
    }

    private Mono<WeatherApiForecastResponse> getForecastFallback(double lat, double lon, int days, Exception e) {
        return Mono.empty();
    }

    private Mono<WeatherApiHistoryResponse> getHistoryFallback(double lat, double lon, String date, Exception e) {
        return Mono.empty();
    }
}