package com.climaterisk.ingestion.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class NasaEonetResponse {
    private List<Event> events;

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    public static class Event {
        private String id;
        private String title;
        @JsonProperty("event_type")
        private String eventType;
        @JsonProperty("geometry")
        private List<Geometry> geometries;
        private List<String> categories;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getEventType() {
            return eventType;
        }

        public void setEventType(String eventType) {
            this.eventType = eventType;
        }

        public List<Geometry> getGeometries() {
            return geometries;
        }

        public void setGeometries(List<Geometry> geometries) {
            this.geometries = geometries;
        }

        public List<String> getCategories() {
            return categories;
        }

        public void setCategories(List<String> categories) {
            this.categories = categories;
        }
    }

    public static class Geometry {
        @JsonProperty("date")
        private String date;
        @JsonProperty("type")
        private String type;
        private double[] coordinates;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public double[] getCoordinates() {
            return coordinates;
        }

        public void setCoordinates(double[] coordinates) {
            this.coordinates = coordinates;
        }
    }
}