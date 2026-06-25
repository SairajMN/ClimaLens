package com.climaterisk.scoring.util;

import com.climaterisk.common.entity.ZoneFeature;
import com.climaterisk.common.entity.RiskScore;
import com.climaterisk.common.repository.ZoneFeatureRepository;
import com.climaterisk.common.repository.RiskScoreRepository;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Prepares training data for ML models by combining features with labels.
 * <p>
 * Exports CSV files for flood and heat risk model training.
 * Labels are derived from historical risk scores and events.
 * </p>
 */
@Component
public class TrainingDataPrep {

    private final ZoneFeatureRepository zoneFeatureRepository;
    private final RiskScoreRepository riskScoreRepository;

    public TrainingDataPrep(ZoneFeatureRepository zoneFeatureRepository,
            RiskScoreRepository riskScoreRepository) {
        this.zoneFeatureRepository = zoneFeatureRepository;
        this.riskScoreRepository = riskScoreRepository;
    }

    /**
     * Exports training data for flood risk model to CSV.
     */
    public void exportFloodTrainingData(String outputPath) throws IOException {
        List<ZoneFeature> features = zoneFeatureRepository.findAll();
        List<RiskScore> scores = riskScoreRepository.findAll();

        try (FileWriter writer = new FileWriter(outputPath)) {
            // Header
            writer.write("h3_index,temperature_c,humidity,precip_mm,wind_kph,pressure_mb," +
                    "cloud_percent,uv_index,soil_saturation,flood_risk_indicator,label\n");

            // TODO: Join features with scores and write rows
            // For now, write placeholder header only
        }
    }

    /**
     * Exports training data for heat risk model to CSV.
     */
    public void exportHeatTrainingData(String outputPath) throws IOException {
        List<ZoneFeature> features = zoneFeatureRepository.findAll();
        List<RiskScore> scores = riskScoreRepository.findAll();

        try (FileWriter writer = new FileWriter(outputPath)) {
            // Header
            writer.write("h3_index,temperature_c,humidity,heat_index_c,uv_index," +
                    "temp_anomaly_c,heat_risk_indicator,label\n");

            // TODO: Join features with scores and write rows
            // For now, write placeholder header only
        }
    }

    /**
     * Generates synthetic training data for MVP/testing.
     * Creates labeled examples based on simple rules.
     */
    public void generateSyntheticData(String floodOutputPath, String heatOutputPath) throws IOException {
        try (FileWriter floodWriter = new FileWriter(floodOutputPath);
                FileWriter heatWriter = new FileWriter(heatOutputPath)) {

            // Headers
            floodWriter.write("h3_index,temperature_c,humidity,precip_mm,wind_kph,pressure_mb," +
                    "cloud_percent,uv_index,soil_saturation,flood_risk_indicator,label\n");
            heatWriter.write("h3_index,temperature_c,humidity,heat_index_c,uv_index," +
                    "temp_anomaly_c,heat_risk_indicator,label\n");

            // Generate 1000 synthetic examples
            for (int i = 0; i < 1000; i++) {
                String h3Index = "synthetic_" + i;
                double tempC = 20 + Math.random() * 20; // 20-40°C
                double humidity = 40 + Math.random() * 60; // 40-100%
                double precipMm = Math.random() * 50; // 0-50mm
                double windKph = 5 + Math.random() * 30; // 5-35 kph
                double pressureMb = 1000 + Math.random() * 30; // 1000-1030 mb
                double cloudPercent = Math.random() * 100;
                double uvIndex = Math.random() * 11;
                double soilSaturation = Math.random();
                double floodRisk = Math.random();
                double heatIndex = tempC + (humidity / 100.0) * 5;
                double tempAnomaly = (Math.random() - 0.5) * 10; // -5 to +5
                double heatRisk = Math.random();

                // Labels: 1 if risk > 0.7, else 0
                int floodLabel = floodRisk > 0.7 ? 1 : 0;
                int heatLabel = heatRisk > 0.7 ? 1 : 0;

                floodWriter.write(String.format("%s,%.2f,%.2f,%.2f,%.2f,%.2f,%.2f,%.2f,%.2f,%.2f,%d\n",
                        h3Index, tempC, humidity, precipMm, windKph, pressureMb,
                        cloudPercent, uvIndex, soilSaturation, floodRisk, floodLabel));

                heatWriter.write(String.format("%s,%.2f,%.2f,%.2f,%.2f,%.2f,%.2f,%d\n",
                        h3Index, tempC, humidity, heatIndex, uvIndex, tempAnomaly, heatRisk, heatLabel));
            }
        }
    }
}