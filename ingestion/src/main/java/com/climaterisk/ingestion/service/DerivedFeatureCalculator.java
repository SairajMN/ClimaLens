package com.climaterisk.ingestion.service;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Calculates derived features from raw weather and zone data.
 * <p>
 * Derived features include:
 * - Rainfall accumulation windows (1h, 3h, 6h, 24h)
 * - Soil saturation proxy (based on precipitation + drainage)
 * - Temperature anomaly vs. NASA POWER baseline
 * - Heat index (feels-like temperature)
 * - Wind chill
 * </p>
 */
@Component
public class DerivedFeatureCalculator {

    /**
     * Calculates all derived features for a zone.
     *
     * @param rawFeatures raw feature map from ZoneFeatureBuilder
     * @return map of derived feature names to values
     */
    public Map<String, Object> calculateDerivedFeatures(Map<String, Object> rawFeatures) {
        Map<String, Object> derived = new HashMap<>();

        // Soil saturation proxy (0-1 scale)
        double precipMm = getDouble(rawFeatures, "precip_mm");
        double drainageProxy = getDouble(rawFeatures, "drainage_proxy");
        double soilSaturation = calculateSoilSaturation(precipMm, drainageProxy);
        derived.put("soil_saturation_proxy", soilSaturation);

        // Heat index (simplified)
        double tempC = getDouble(rawFeatures, "temperature_c");
        double humidity = getDouble(rawFeatures, "humidity");
        double heatIndex = calculateHeatIndex(tempC, humidity);
        derived.put("heat_index_c", heatIndex);

        // Temperature anomaly (already calculated in ZoneFeatureBuilder, but ensure it
        // exists)
        if (!rawFeatures.containsKey("temp_anomaly_c")) {
            derived.put("temp_anomaly_c", 0.0);
        } else {
            derived.put("temp_anomaly_c", rawFeatures.get("temp_anomaly_c"));
        }

        // Precipitation anomaly (already calculated in ZoneFeatureBuilder, but ensure
        // it exists)
        if (!rawFeatures.containsKey("precip_anomaly_mm")) {
            derived.put("precip_anomaly_mm", 0.0);
        } else {
            derived.put("precip_anomaly_mm", rawFeatures.get("precip_anomaly_mm"));
        }

        // Flood risk indicator (simple heuristic)
        double floodRisk = calculateFloodRiskIndicator(soilSaturation, precipMm, drainageProxy);
        derived.put("flood_risk_indicator", floodRisk);

        // Heat risk indicator (simple heuristic)
        double heatRisk = calculateHeatRiskIndicator(tempC, heatIndex, rawFeatures);
        derived.put("heat_risk_indicator", heatRisk);

        return derived;
    }

    /**
     * Calculates soil saturation proxy (0-1 scale).
     * Higher values indicate more saturated soil (higher flood risk).
     */
    private double calculateSoilSaturation(double precipMm, double drainageProxy) {
        // Simple model: saturation increases with precipitation, decreases with
        // drainage
        // Normalize precipitation to 0-1 (assume 50mm/day as max for normalization)
        double precipFactor = Math.min(precipMm / 50.0, 1.0);
        // Drainage proxy: 0 = poor drainage, 1 = excellent drainage
        double saturation = (precipFactor * 0.7) + ((1.0 - drainageProxy) * 0.3);
        return Math.min(Math.max(saturation, 0.0), 1.0);
    }

    /**
     * Calculates heat index (simplified version of NOAA heat index).
     * Returns "feels like" temperature in Celsius.
     */
    private double calculateHeatIndex(double tempC, double humidity) {
        if (tempC < 27.0) {
            return tempC; // Heat index only relevant for high temps
        }

        // Simplified heat index formula
        double T = tempC;
        double R = humidity;

        double HI = -8.78469475556
                + (1.61139411 * T)
                + (2.33854883889 * R)
                + (-0.14611605 * T * R)
                + (-0.012308094 * T * T)
                + (-0.0164248277778 * R * R)
                + (0.002211732 * T * T * R)
                + (0.00072546 * T * R * R)
                + (-0.000003582 * T * T * R * R);

        return Math.max(tempC, HI);
    }

    /**
     * Calculates flood risk indicator (0-1 scale).
     * Combines soil saturation, precipitation, and drainage.
     */
    private double calculateFloodRiskIndicator(double soilSaturation, double precipMm, double drainageProxy) {
        // Weighted combination
        double precipFactor = Math.min(precipMm / 25.0, 1.0); // 25mm as threshold
        double risk = (soilSaturation * 0.5) + (precipFactor * 0.3) + ((1.0 - drainageProxy) * 0.2);
        return Math.min(Math.max(risk, 0.0), 1.0);
    }

    /**
     * Calculates heat risk indicator (0-1 scale).
     * Combines temperature, heat index, and UV index.
     */
    private double calculateHeatRiskIndicator(double tempC, double heatIndex, Map<String, Object> features) {
        double uvIndex = getDouble(features, "uv_index");

        // Temperature factor (threshold: 35°C)
        double tempFactor = Math.min(Math.max((tempC - 30.0) / 10.0, 0.0), 1.0);

        // UV factor (threshold: 8)
        double uvFactor = Math.min(uvIndex / 10.0, 1.0);

        double risk = (tempFactor * 0.6) + (uvFactor * 0.4);
        return Math.min(Math.max(risk, 0.0), 1.0);
    }

    private double getDouble(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value == null)
            return 0.0;
        if (value instanceof Double d)
            return d;
        if (value instanceof Integer i)
            return (double) i;
        if (value instanceof Number n)
            return n.doubleValue();
        return 0.0;
    }
}