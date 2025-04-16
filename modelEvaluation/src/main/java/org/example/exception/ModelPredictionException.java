package org.example.exception;

public class ModelPredictionException extends RuntimeException {

    public ModelPredictionException(String message) {
        super(message);
    }

    public ModelPredictionException(String message, Throwable cause) {
        super(message, cause);
    }
}