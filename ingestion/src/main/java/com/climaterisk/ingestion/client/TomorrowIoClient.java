package com.climaterisk.ingestion.client;

import com.climaterisk.ingestion.config.TomorrowIoProperties;
import com.climaterisk.ingestion.dto.TomorrowIoForecastResponse;
import com.climaterisk.ingestion.dto.TomorrowIoRealtimeResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * Tomorrow.io client for high-resolution precipitation and weather events.
 * <p>
 * Uses Spring WebFlux WebClient for non-blocking I/O with Resilience4j.
 * </p>
 */
@Component
public class TomorrowIoClient {

    private final WebClient webClient;
    private final TomorrowIoProperties properties;

    public TomorrowIoClient(WebClient.Builder webClientBuilder, TomorrowIoProperties properties) {
        this.properties = properties;
        this.webClient = webClientBuilder
                .baseUrl(properties.getBaseUrl())
                .build();
    }

    /**
     * Fetches real-time weather for a coordinate.
     */
    @Retry(name = "tomorrowIo", fallbackMethod = "getRealtimeFallback")
    @CircuitBreaker(name = "tomorrowIo", fallbackMethod = "getRealtimeFallback")
    public Mono<TomorrowIoRealtimeResponse> getRealtime(double lat, double lon) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/weather/realtime")
                        .queryParam("location", lat + "," + lon)
                        .queryParam("apikey", properties.getApiKey())
                        .build())
                .retrieve()
                .bodyToMono(TomorrowIoRealtimeResponse.class);
    }

    /**
     * Fetches forecast for a coordinate.
     */
    @Retry(name = "tomorrowIo", fallbackMethod = "getForecastFallback")
    @CircuitBreaker(name = "tomorrowIo", fallbackMethod = "getForecastFallback")
    public Mono<TomorrowIoForecastResponse> getForecast(double lat, double lon) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/weather/forecast")
                        .queryParam("location", lat + "," + lon)
                        .queryParam("apikey", properties.getApiKey())
                        .build())
                .retrieve()
                .bodyToMono(TomorrowIoForecastResponse.class);
    }

    // --- Fallbacks ---
    private Mono<TomorrowIoRealtimeResponse> getRealtimeFallback(double lat, double lon, Exception e) {
        return Mono.empty();
    }

    private Mono<TomorrowIoForecastResponse> getForecastFallback(double lat, double lon, Exception e) {
        return Mono.empty();
    }
}