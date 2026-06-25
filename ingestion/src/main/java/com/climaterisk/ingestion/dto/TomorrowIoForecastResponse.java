package com.climaterisk.ingestion.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class TomorrowIoForecastResponse {
    private List<Timestep> timelines;

    public List<Timestep> getTimelines() {
        return timelines;
    }

    public void setTimelines(List<Timestep> timelines) {
        this.timelines = timelines;
    }

    public static class Timestep {
        @JsonProperty("timestep")
        private String timestep;
        private List<Interval> intervals;

        public String getTimestep() {
            return timestep;
        }

        public void setTimestep(String timestep) {
            this.timestep = timestep;
        }

        public List<Interval> getIntervals() {
            return intervals;
        }

        public void setIntervals(List<Interval> intervals) {
            this.intervals = intervals;
        }
    }

    public static class Interval {
        @JsonProperty("startTime")
        private String startTime;
        @JsonProperty("endTime")
        private String endTime;
        private Values values;

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

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
    }
}