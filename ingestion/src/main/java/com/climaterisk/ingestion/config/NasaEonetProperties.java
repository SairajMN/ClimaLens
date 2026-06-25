package com.climaterisk.ingestion.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "nasa")
public class NasaEonetProperties {
    private String baseUrl = "https://api.nasa.gov";
    private String eonetUrl = "https://eonet.gsfc.nasa.gov/api/v3";
    private String apiKey = "DEMO_KEY";

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getEonetUrl() {
        return eonetUrl;
    }

    public void setEonetUrl(String eonetUrl) {
        this.eonetUrl = eonetUrl;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }
}