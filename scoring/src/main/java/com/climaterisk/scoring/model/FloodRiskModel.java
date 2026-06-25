package com.climaterisk.scoring.model;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Flood risk prediction model using Smile gradient boosting.
 * <p>
 * Predicts flood risk score (0-100) based on zone features.
 * Model is trained offline and loaded at startup.
 * For MVP, uses a simple heuristic fallback if model is not trained.
 * </p>
 */
@Component
public class FloodRiskModel {

    // private GradientTreeBoost model; // TODO: Add Smile dependency and train
    // model
    // private boolean modelLoaded = false;
    private boolean modelLoaded = false;

    /**
     * Predicts flood risk score for a zone.
     *
     * @param features zone feature vector
     * @return flood risk score (0-100)
     */
    public double predict(Map<String, Object> staticFeatures, Map<String, Object> dynamicFeatures) {
        // TODO: Enable when Smile model is trained
        // if (modelLoaded && model != null) {
        // double[] featureVector = extractFeatureVector(staticFeatures,
        // dynamicFeatures);
        // double probability = model.predict(featureVector);
        // return probability * 100;
        // }
        // Fallback to heuristic
        return heuristicScore(staticFeatures, dynamicFeatures);
    }

    /**
     * Trains the model from labeled data.
     * TODO: Implement actual training from CSV
     */
    public void train(List<double[]> trainingData, List<Integer> labels) {
        // Placeholder for Smile training
        // In production, this would:
        // 1. Convert features to double[][] matrix
        // 2. Train GradientTreeBoost
        // 3. Save model to disk
        // 4. Load on startup
    }

    /**
     * Loads pre-trained model from disk.
     */
    public void loadModel(String modelPath) throws IOException {
        // Placeholder for model loading
        // model = GradientTreeBoost.load(modelPath);
        // modelLoaded = true;
    }

    /**
     * Heuristic fallback score (0-100) when model is not available.
     */
    private double heuristicScore(Map<String, Object> staticFeatures, Map<String, Object> dynamic) {

        double precipMm = getDouble(dynamic, "precip_mm");
        double soilSaturation = getDouble(dynamic, "soil_saturation_proxy");
        double floodRiskIndicator = getDouble(dynamic, "flood_risk_indicator");
        double drainageProxy = getDouble(staticFeatures, "drainage_proxy");

        // Weighted heuristic
        double score = (precipMm / 50.0) * 30 + // Precipitation contribution
                (soilSaturation * 25) + // Soil saturation
                (floodRiskIndicator * 30) + // Combined risk indicator
                ((1.0 - drainageProxy) * 15); // Poor drainage penalty

        return Math.min(Math.max(score, 0), 100);
    }

    /**
     * Extracts feature vector for Smile model.
     * Order must match training feature order.
     */
    private double[] extractFeatureVector(Map<String, Object> staticFeatures, Map<String, Object> dynamic) {

        return new double[] {
                getDouble(dynamic, "temperature_c"),
                getDouble(dynamic, "humidity"),
                getDouble(dynamic, "precip_mm"),
                getDouble(dynamic, "wind_kph"),
                getDouble(dynamic, "pressure_mb"),
                getDouble(dynamic, "cloud_percent"),
                getDouble(dynamic, "uv_index"),
                getDouble(staticFeatures, "elevation_m"),
                getDouble(staticFeatures, "imperviousness"),
                getDouble(staticFeatures, "ndvi_proxy"),
                getDouble(staticFeatures, "drainage_proxy"),
                getDouble(dynamic, "soil_saturation_proxy"),
                getDouble(dynamic, "temp_anomaly_c"),
                getDouble(dynamic, "precip_anomaly_mm")
        };
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