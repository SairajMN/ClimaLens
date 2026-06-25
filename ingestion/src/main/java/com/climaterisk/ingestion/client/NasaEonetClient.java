package com.climaterisk.ingestion.client;

import com.climaterisk.ingestion.config.NasaEonetProperties;
import com.climaterisk.ingestion.dto.NasaEonetResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * NASA EONET (Earth Observatory Natural Event Tracker) client.
 * <p>
 * Provides active natural events (wildfires, storms, floods) for
 * situational awareness layer.
 * </p>
 */
@Component
public class NasaEonetClient {

    private final WebClient webClient;
    private final NasaEonetProperties properties;

    public NasaEonetClient(WebClient.Builder webClientBuilder, NasaEonetProperties properties) {
        this.properties = properties;
        this.webClient = webClientBuilder
                .baseUrl(properties.getEonetUrl())
                .build();
    }

    /**
     * Fetches active natural events.
     */
    @Retry(name = "nasaEonet", fallbackMethod = "getEventsFallback")
    @CircuitBreaker(name = "nasaEonet", fallbackMethod = "getEventsFallback")
    public Mono<NasaEonetResponse> getEvents() {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/events")
                        .queryParam("api_key", properties.getApiKey())
                        .build())
                .retrieve()
                .bodyToMono(NasaEonetResponse.class);
    }

    private Mono<NasaEonetResponse> getEventsFallback(Exception e) {
        return Mono.empty();
    }
}