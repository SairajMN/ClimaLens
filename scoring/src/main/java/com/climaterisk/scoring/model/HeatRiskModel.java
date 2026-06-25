package com.climaterisk.scoring.model;

import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Heat risk prediction model using Smile gradient boosting.
 * <p>
 * Predicts heat risk score (0-100) based on zone features.
 * For MVP, uses a simple heuristic fallback if model is not trained.
 * </p>
 */
@Component
public class HeatRiskModel {

    /**
     * Predicts heat risk score for a zone.
     *
     * @param staticFeatures  static zone features
     * @param dynamicFeatures dynamic weather features
     * @return heat risk score (0-100)
     */
    public double predict(Map<String, Object> staticFeatures, Map<String, Object> dynamicFeatures) {
        // TODO: Enable when Smile model is trained
        // Fallback to heuristic
        return heuristicScore(staticFeatures, dynamicFeatures);
    }

    /**
     * Heuristic fallback score (0-100) when model is not available.
     */
    private double heuristicScore(Map<String, Object> staticFeatures, Map<String, Object> dynamic) {
        double tempC = getDouble(dynamic, "temperature_c");
        double heatIndex = getDouble(dynamic, "heat_index_c");
        double uvIndex = getDouble(dynamic, "uv_index");
        double tempAnomaly = getDouble(dynamic, "temp_anomaly_c");
        double heatRiskIndicator = getDouble(dynamic, "heat_risk_indicator");

        // Use heat index if available, otherwise temperature
        double effectiveTemp = heatIndex > 0 ? heatIndex : tempC;

        // Temperature factor (threshold: 35°C)
        double tempFactor = Math.min(Math.max((effectiveTemp - 30.0) / 10.0, 0.0), 1.0);

        // UV factor (threshold: 8)
        double uvFactor = Math.min(uvIndex / 10.0, 1.0);

        // Temperature anomaly factor (positive anomaly = hotter than normal)
        double anomalyFactor = Math.min(Math.max((tempAnomaly + 5.0) / 10.0, 0.0), 1.0);

        // Weighted combination
        double score = (tempFactor * 35) + // Temperature/heat index
                (uvFactor * 25) + // UV radiation
                (anomalyFactor * 20) + // Temperature anomaly
                (heatRiskIndicator * 20); // Combined risk indicator

        return Math.min(Math.max(score, 0), 100);
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
}