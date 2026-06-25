package com.climaterisk.ingestion.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class OpenWeatherAirPollutionResponse {
    private List<AirPollutionItem> list;

    public List<AirPollutionItem> getList() {
        return list;
    }

    public void setList(List<AirPollutionItem> list) {
        this.list = list;
    }

    public static class AirPollutionItem {
        @JsonProperty("dt")
        private long timestamp;
        private Main main;
        private Components components;

        public long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(long timestamp) {
            this.timestamp = timestamp;
        }

        public Main getMain() {
            return main;
        }

        public void setMain(Main main) {
            this.main = main;
        }

        public Components getComponents() {
            return components;
        }

        public void setComponents(Components components) {
            this.components = components;
        }
    }

    public static class Main {
        @JsonProperty("aqi")
        private int aqi;

        public int getAqi() {
            return aqi;
        }

        public void setAqi(int aqi) {
            this.aqi = aqi;
        }
    }

    public static class Components {
        @JsonProperty("pm2_5")
        private double pm25;
        @JsonProperty("pm10")
        private double pm10;

        public double getPm25() {
            return pm25;
        }

        public void setPm25(double pm25) {
            this.pm25 = pm25;
        }

        public double getPm10() {
            return pm10;
        }

        public void setPm10(double pm10) {
            this.pm10 = pm10;
        }
    }
}