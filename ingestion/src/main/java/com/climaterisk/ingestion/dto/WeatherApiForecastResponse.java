package com.climaterisk.ingestion.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO for WeatherAPI.com forecast response.
 */
public class WeatherApiForecastResponse {
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

        @JsonProperty("hour")
        private java.util.List<Hour> hours;

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

        public java.util.List<Hour> getHours() {
            return hours;
        }

        public void setHours(java.util.List<Hour> hours) {
            this.hours = hours;
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

        @JsonProperty("uv")
        private double uv;

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

        public double getUv() {
            return uv;
        }

        public void setUv(double uv) {
            this.uv = uv;
        }
    }

    public static class Hour {
        @JsonProperty("time")
        private String time;

        @JsonProperty("temp_c")
        private double tempC;

        @JsonProperty("precip_mm")
        private double precipMm;

        @JsonProperty("humidity")
        private int humidity;

        @JsonProperty("wind_kph")
        private double windKph;

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public double getTempC() {
            return tempC;
        }

        public void setTempC(double tempC) {
            this.tempC = tempC;
        }

        public double getPrecipMm() {
            return precipMm;
        }

        public void setPrecipMm(double precipMm) {
            this.precipMm = precipMm;
        }

        public int getHumidity() {
            return humidity;
        }

        public void setHumidity(int humidity) {
            this.humidity = humidity;
        }

        public double getWindKph() {
            return windKph;
        }

        public void setWindKph(double windKph) {
            this.windKph = windKph;
        }
    }
}