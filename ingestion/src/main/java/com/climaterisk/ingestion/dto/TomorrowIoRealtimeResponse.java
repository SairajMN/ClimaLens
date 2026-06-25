package com.climaterisk.ingestion.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TomorrowIoRealtimeResponse {
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public static class Data {
        private Values values;

        public Values getValues() {
            return values;
        }

        public void setValues(Values values) {
            this.values = values;
        }
    }

    public static class Values {
        @JsonProperty("temperature")
        private double temperature;
        @JsonProperty("humidity")
        private double humidity;
        @JsonProperty("precipitationIntensity")
        private double precipitationIntensity;
        @JsonProperty("windSpeed")
        private double windSpeed;
        @JsonProperty("windDirection")
        private double windDirection;
        @JsonProperty("pressure")
        private double pressure;

        public double getTemperature() {
            return temperature;
        }

        public void setTemperature(double temperature) {
            this.temperature = temperature;
        }

        public double getHumidity() {
            return humidity;
        }

        public void setHumidity(double humidity) {
            this.humidity = humidity;
        }

        public double getPrecipitationIntensity() {
            return precipitationIntensity;
        }

        public void setPrecipitationIntensity(double precipitationIntensity) {
            this.precipitationIntensity = precipitationIntensity;
        }

        public double getWindSpeed() {
            return windSpeed;
        }

        public void setWindSpeed(double windSpeed) {
            this.windSpeed = windSpeed;
        }

        public double getWindDirection() {
            return windDirection;
        }

        public void setWindDirection(double windDirection) {
            this.windDirection = windDirection;
        }

        public double getPressure() {
            return pressure;
        }

        public void setPressure(double pressure) {
            this.pressure = pressure;
        }
    }
}