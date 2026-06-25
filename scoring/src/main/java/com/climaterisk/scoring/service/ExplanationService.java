package com.climaterisk.scoring.service;

import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Generates plain-language explanations for risk scores.
 * <p>
 * Maps feature contributions to human-readable factors
 * (e.g., "High precipitation (35mm) increased flood risk by 25%").
 * </p>
 */
@Component
public class ExplanationService {

    private static final Map<String, String> FLOOD_FACTOR_LABELS = Map.ofEntries(
            Map.entry("precip_mm", "Recent precipitation"),
            Map.entry("soil_saturation_proxy", "Soil saturation level"),
            Map.entry("flood_risk_indicator", "Combined flood risk indicator"),
            Map.entry("drainage_proxy", "Drainage capacity"),
            Map.entry("temp_anomaly_c", "Temperature anomaly"),
            Map.entry("precip_anomaly_mm", "Precipitation anomaly"));

    private static final Map<String, String> HEAT_FACTOR_LABELS = Map.ofEntries(
            Map.entry("temperature_c", "Air temperature"),
            Map.entry("heat_index_c", "Heat index (feels-like)"),
            Map.entry("uv_index", "UV radiation"),
            Map.entry("temp_anomaly_c", "Temperature anomaly"),
            Map.entry("heat_risk_indicator", "Combined heat risk indicator"));

    /**
     * Generates explanation for flood risk score.
     *
     * @param staticFeatures  static zone features
     * @param dynamicFeatures dynamic weather features
     * @param score           flood risk score (0-100)
     * @return list of explanation factors
     */
    public List<ExplanationFactor> explainFloodRisk(Map<String, Object> staticFeatures,
            Map<String, Object> dynamicFeatures,
            double score) {
        List<ExplanationFactor> factors = new ArrayList<>();

        // Precipitation contribution
        double precipMm = getDouble(dynamicFeatures, "precip_mm");
        if (precipMm > 10) {
            double contribution = Math.min((precipMm / 50.0) * 30, 30);
            factors.add(new ExplanationFactor(
                    FLOOD_FACTOR_LABELS.get("precip_mm"),
                    String.format("%.1f mm", precipMm),
                    contribution,
                    "High precipitation increases surface runoff"));
        }

        // Soil saturation
        double soilSaturation = getDouble(dynamicFeatures, "soil_saturation_proxy");
        if (soilSaturation > 0.5) {
            double contribution = soilSaturation * 25;
            factors.add(new ExplanationFactor(
                    FLOOD_FACTOR_LABELS.get("soil_saturation_proxy"),
                    String.format("%.0f%% saturated", soilSaturation * 100),
                    contribution,
                    "Saturated soil reduces infiltration capacity"));
        }

        // Drainage
        double drainage = getDouble(staticFeatures, "drainage_proxy");
        if (drainage < 0.5) {
            double contribution = (1.0 - drainage) * 15;
            factors.add(new ExplanationFactor(
                    FLOOD_FACTOR_LABELS.get("drainage_proxy"),
                    String.format("Poor (%.0f/1.0)", drainage),
                    contribution,
                    "Poor drainage increases flood likelihood"));
        }

        // Overall summary
        String summary = generateSummary(score, factors, "flood");
        factors.add(new ExplanationFactor("Summary", "", score, summary));

        return factors;
    }

    /**
     * Generates explanation for heat risk score.
     */
    public List<ExplanationFactor> explainHeatRisk(Map<String, Object> staticFeatures,
            Map<String, Object> dynamicFeatures,
            double score) {
        List<ExplanationFactor> factors = new ArrayList<>();

        double tempC = getDouble(dynamicFeatures, "temperature_c");
        double heatIndex = getDouble(dynamicFeatures, "heat_index_c");
        double uvIndex = getDouble(dynamicFeatures, "uv_index");
        double tempAnomaly = getDouble(dynamicFeatures, "temp_anomaly_c");

        // Temperature/heat index
        double effectiveTemp = heatIndex > 0 ? heatIndex : tempC;
        if (effectiveTemp > 30) {
            double contribution = Math.min((effectiveTemp - 30) / 10.0, 1.0) * 35;
            factors.add(new ExplanationFactor(
                    HEAT_FACTOR_LABELS.get("heat_index_c"),
                    String.format("%.1f°C (feels like)", effectiveTemp),
                    contribution,
                    "High temperature increases heat stress"));
        }

        // UV index
        if (uvIndex > 5) {
            double contribution = Math.min(uvIndex / 10.0, 1.0) * 25;
            factors.add(new ExplanationFactor(
                    HEAT_FACTOR_LABELS.get("uv_index"),
                    String.format("UV Index %.1f", uvIndex),
                    contribution,
                    "High UV radiation increases health risk"));
        }

        // Temperature anomaly
        if (tempAnomaly > 2) {
            double contribution = Math.min((tempAnomaly + 5) / 10.0, 1.0) * 20;
            factors.add(new ExplanationFactor(
                    HEAT_FACTOR_LABELS.get("temp_anomaly_c"),
                    String.format("+%.1f°C above normal", tempAnomaly),
                    contribution,
                    "Unusually hot for this time of year"));
        }

        // Overall summary
        String summary = generateSummary(score, factors, "heat");
        factors.add(new ExplanationFactor("Summary", "", score, summary));

        return factors;
    }

    private String generateSummary(double score, List<ExplanationFactor> factors, String riskType) {
        if (score < 30) {
            return String.format("Low %s risk. Conditions are within normal ranges.", riskType);
        } else if (score < 60) {
            return String.format("Moderate %s risk. Monitor conditions and take precautions.", riskType);
        } else if (score < 80) {
            return String.format("High %s risk. Take protective actions immediately.", riskType);
        } else {
            return String.format("Extreme %s risk. Dangerous conditions. Avoid outdoor activities.", riskType);
        }
    }

    private double getDouble(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value == null)
            return 0.0;
        if (value instanceof Double d)
            return d;
        if (value instanceof Number n)
            return n.doubleValue();
        return 0.0;
    }

    /**
     * Explanation factor for a risk score.
     */
    public record ExplanationFactor(
            String name,
            String value,
            double contribution,
            String description) {
    }
}