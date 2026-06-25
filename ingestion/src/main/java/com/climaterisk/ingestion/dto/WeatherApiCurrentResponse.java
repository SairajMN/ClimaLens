package com.climaterisk.ingestion.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO for WeatherAPI.com current weather response.
 */
public class WeatherApiCurrentResponse {
    private Current current;

    public Current getCurrent() {
        return current;
    }

    public void setCurrent(Current current) {
        this.current = current;
    }

    public static class Current {
        @JsonProperty("temp_c")
        private double tempC;

        @JsonProperty("feelslike_c")
        private double feelslikeC;

        @JsonProperty("humidity")
        private int humidity;

        @JsonProperty("precip_mm")
        private double precipMm;

        @JsonProperty("wind_kph")
        private double windKph;

        @JsonProperty("wind_degree")
        private int windDegree;

        @JsonProperty("pressure_mb")
        private double pressureMb;

        @JsonProperty("cloud")
        private int cloud;

        @JsonProperty("uv")
        private double uv;

        public double getTempC() {
            return tempC;
        }

        public void setTempC(double tempC) {
            this.tempC = tempC;
        }

        public double getFeelslikeC() {
            return feelslikeC;
        }

        public void setFeelslikeC(double feelslikeC) {
            this.feelslikeC = feelslikeC;
        }

        public int getHumidity() {
            return humidity;
        }

        public void setHumidity(int humidity) {
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

        public int getWindDegree() {
            return windDegree;
        }

        public void setWindDegree(int windDegree) {
            this.windDegree = windDegree;
        }

        public double getPressureMb() {
            return pressureMb;
        }

        public void setPressureMb(double pressureMb) {
            this.pressureMb = pressureMb;
        }

        public int getCloud() {
            return cloud;
        }

        public void setCloud(int cloud) {
            this.cloud = cloud;
        }

        public double getUv() {
            return uv;
        }

        public void setUv(double uv) {
            this.uv = uv;
        }
    }
}