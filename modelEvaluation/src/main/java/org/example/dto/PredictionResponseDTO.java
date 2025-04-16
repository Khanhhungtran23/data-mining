package org.example.dto;

public class PredictionResponseDTO {
    private String movieTitle;
    private Double predictedRating;
    private String message;
    private Long processingTimeMs;
    private String modelUsed;

    public PredictionResponseDTO() {
    }

    public PredictionResponseDTO(String movieTitle, Double predictedRating, String message, Long processingTimeMs, String modelUsed) {
        this.movieTitle = movieTitle;
        this.predictedRating = predictedRating;
        this.message = message;
        this.processingTimeMs = processingTimeMs;
        this.modelUsed = modelUsed;
    }

    // Getters and Setters
    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public Double getPredictedRating() {
        return predictedRating;
    }

    public void setPredictedRating(Double predictedRating) {
        this.predictedRating = predictedRating;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getProcessingTimeMs() {
        return processingTimeMs;
    }

    public void setProcessingTimeMs(Long processingTimeMs) {
        this.processingTimeMs = processingTimeMs;
    }

    public String getModelUsed() {
        return modelUsed;
    }

    public void setModelUsed(String modelUsed) {
        this.modelUsed = modelUsed;
    }

    // Custom builder
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String movieTitle;
        private Double predictedRating;
        private String message;
        private Long processingTimeMs;
        private String modelUsed;

        public Builder movieTitle(String movieTitle) {
            this.movieTitle = movieTitle;
            return this;
        }

        public Builder predictedRating(Double predictedRating) {
            this.predictedRating = predictedRating;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder processingTimeMs(Long processingTimeMs) {
            this.processingTimeMs = processingTimeMs;
            return this;
        }

        public Builder modelUsed(String modelUsed) {
            this.modelUsed = modelUsed;
            return this;
        }

        public PredictionResponseDTO build() {
            return new PredictionResponseDTO(movieTitle, predictedRating, message, processingTimeMs, modelUsed);
        }
    }
}