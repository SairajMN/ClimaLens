package com.climaterisk.ingestion.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Configuration properties for WeatherAPI.com client.
 * <p>
 * Values are injected from application-secrets.yml or environment variables.
 * Never hardcode real API keys in source code.
 * </p>
 */
@Component
@ConfigurationProperties(prefix = "weatherapi")
public class WeatherApiProperties {
    private String baseUrl = "https://api.weatherapi.com/v1";
    private String apiKey;

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }
}