package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PredictionResponse {
    private String movieTitle;
    private Double predictedRating;
    private Double confidenceLevel;
    private Map<String, Double> featureImportance;
    private List<SimilarMovie> similarMovies;

//    private TrendData trendData;
}