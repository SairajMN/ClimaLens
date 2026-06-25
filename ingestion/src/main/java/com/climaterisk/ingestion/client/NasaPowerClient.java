package com.climaterisk.ingestion.client;

import com.climaterisk.ingestion.config.NasaPowerProperties;
import com.climaterisk.ingestion.dto.NasaPowerResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * NASA POWER API client for historical climatology data.
 * <p>
 * Provides long-term daily averages (since 1981) for temperature,
 * precipitation, and solar radiation at a given coordinate.
 * </p>
 */
@Component
public class NasaPowerClient {

    private final WebClient webClient;
    private final NasaPowerProperties properties;

    public NasaPowerClient(WebClient.Builder webClientBuilder, NasaPowerProperties properties) {
        this.properties = properties;
        this.webClient = webClientBuilder
                .baseUrl(properties.getPowerUrl())
                .build();
    }

    /**
     * Fetches daily climatology for a coordinate.
     *
     * @param lat   latitude (WGS84)
     * @param lon   longitude (WGS84)
     * @param start start date (YYYYMMDD)
     * @param end   end date (YYYYMMDD)
     */
    @Retry(name = "nasaPower", fallbackMethod = "getClimatologyFallback")
    @CircuitBreaker(name = "nasaPower", fallbackMethod = "getClimatologyFallback")
    public Mono<NasaPowerResponse> getClimatology(double lat, double lon, String start, String end) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/temporal/daily/point")
                        .queryParam("parameters", "T2M,PRECTOT,RH2M")
                        .queryParam("community", "AG")
                        .queryParam("longitude", lon)
                        .queryParam("latitude", lat)
                        .queryParam("start", start)
                        .queryParam("end", end)
                        .queryParam("format", "JSON")
                        .queryParam("api_key", properties.getApiKey())
                        .build())
                .retrieve()
                .bodyToMono(NasaPowerResponse.class);
    }

    private Mono<NasaPowerResponse> getClimatologyFallback(double lat, double lon, String start, String end,
            Exception e) {
        return Mono.empty();
    }
}