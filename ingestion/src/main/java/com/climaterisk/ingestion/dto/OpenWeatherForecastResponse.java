package com.climaterisk.ingestion.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class OpenWeatherForecastResponse {
    private List<ForecastItem> list;

    public List<ForecastItem> getList() {
        return list;
    }

    public void setList(List<ForecastItem> list) {
        this.list = list;
    }

    public static class ForecastItem {
        @JsonProperty("dt")
        private long timestamp;
        private Main main;
        private Wind wind;
        @JsonProperty("pop")
        private double pop;

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

        public Wind getWind() {
            return wind;
        }

        public void setWind(Wind wind) {
            this.wind = wind;
        }

        public double getPop() {
            return pop;
        }

        public void setPop(double pop) {
            this.pop = pop;
        }
    }

    public static class Main {
        @JsonProperty("temp")
        private double temp;
        @JsonProperty("feels_like")
        private double feelsLike;
        @JsonProperty("humidity")
        private int humidity;
        @JsonProperty("pressure")
        private double pressure;

        public double getTemp() {
            return temp;
        }

        public void setTemp(double temp) {
            this.temp = temp;
        }

        public double getFeelsLike() {
            return feelsLike;
        }

        public void setFeelsLike(double feelsLike) {
            this.feelsLike = feelsLike;
        }

        public int getHumidity() {
            return humidity;
        }

        public void setHumidity(int humidity) {
            this.humidity = humidity;
        }

        public double getPressure() {
            return pressure;
        }

        public void setPressure(double pressure) {
            this.pressure = pressure;
        }
    }

    public static class Wind {
        @JsonProperty("speed")
        private double speed;

        public double getSpeed() {
            return speed;
        }

        public void setSpeed(double speed) {
            this.speed = speed;
        }
    }
}