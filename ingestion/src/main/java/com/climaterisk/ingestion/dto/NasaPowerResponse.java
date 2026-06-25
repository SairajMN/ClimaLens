package com.climaterisk.ingestion.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class NasaPowerResponse {
    private Properties properties;

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public static class Properties {
        @JsonProperty("T2M")
        private Parameter t2m;
        @JsonProperty("PRECTOT")
        private Parameter prectot;
        @JsonProperty("RH2M")
        private Parameter rh2m;

        public Parameter getT2m() {
            return t2m;
        }

        public void setT2m(Parameter t2m) {
            this.t2m = t2m;
        }

        public Parameter getPrectot() {
            return prectot;
        }

        public void setPrectot(Parameter prectot) {
            this.prectot = prectot;
        }

        public Parameter getRh2m() {
            return rh2m;
        }

        public void setRh2m(Parameter rh2m) {
            this.rh2m = rh2m;
        }
    }

    public static class Parameter {
        @JsonProperty("description")
        private String description;
        @JsonProperty("units")
        private String units;
        private Map<String, Double> data;

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getUnits() {
            return units;
        }

        public void setUnits(String units) {
            this.units = units;
        }

        public Map<String, Double> getData() {
            return data;
        }

        public void setData(Map<String, Double> data) {
            this.data = data;
        }
    }
}