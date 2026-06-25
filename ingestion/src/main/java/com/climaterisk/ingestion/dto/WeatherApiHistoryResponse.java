package com.climaterisk.ingestion.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO for WeatherAPI.com history response.
 */
public class WeatherApiHistoryResponse {
    @JsonProperty("forecast")
    private Forecast forecast;

    public Forecast getForecast() {
        return forecast;
    }

    public void setForecast(Forecast forecast) {
        this.forecast = forecast;
    }

    public static class Forecast {
        @JsonProperty("forecastday")
        private java.util.List<ForecastDay> forecastDays;

        public java.util.List<ForecastDay> getForecastDays() {
            return forecastDays;
        }

        public void setForecastDays(java.util.List<ForecastDay> forecastDays) {
            this.forecastDays = forecastDays;
        }
    }

    public static class ForecastDay {
        @JsonProperty("date")
        private String date;

        @JsonProperty("day")
        private Day day;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public Day getDay() {
            return day;
        }

        public void setDay(Day day) {
            this.day = day;
        }
    }

    public static class Day {
        @JsonProperty("maxtemp_c")
        private double maxTempC;

        @JsonProperty("mintemp_c")
        private double minTempC;

        @JsonProperty("avgtemp_c")
        private double avgTempC;

        @JsonProperty("totalprecip_mm")
        private double totalPrecipMm;

        @JsonProperty("maxwind_kph")
        private double maxWindKph;

        @JsonProperty("avghumidity")
        private int avgHumidity;

        public double getMaxTempC() {
            return maxTempC;
        }

        public void setMaxTempC(double maxTempC) {
            this.maxTempC = maxTempC;
        }

        public double getMinTempC() {
            return minTempC;
        }

        public void setMinTempC(double minTempC) {
            this.minTempC = minTempC;
        }

        public double getAvgTempC() {
            return avgTempC;
        }

        public void setAvgTempC(double avgTempC) {
            this.avgTempC = avgTempC;
        }

        public double getTotalPrecipMm() {
            return totalPrecipMm;
        }

        public void setTotalPrecipMm(double totalPrecipMm) {
            this.totalPrecipMm = totalPrecipMm;
        }

        public double getMaxWindKph() {
            return maxWindKph;
        }

        public void setMaxWindKph(double maxWindKph) {
            this.maxWindKph = maxWindKph;
        }

        public int getAvgHumidity() {
            return avgHumidity;
        }

        public void setAvgHumidity(int avgHumidity) {
            this.avgHumidity = avgHumidity;
        }
    }
}