package org.example.weka.evaluation;

public interface ModelEvaluator {
    void evaluate(String datasetPath, String targetColumn) throws Exception;
}
