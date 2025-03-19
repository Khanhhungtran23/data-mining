package org.example.model;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class TrendData {
    private List<GenreTrend> genreTrends;
    private List<RatingDistribution> ratingDistribution;

    @Data
    public static class GenreTrend {
        private String genre;
        private List<YearValue> popularity;
    }

    @Data
    public static class YearValue {
        private Integer year;
        private Double value;
    }

    @Data
    public static class RatingDistribution {
        private Integer rating;
        private Integer count;
    }
}