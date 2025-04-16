package org.example;

import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.classifiers.Classifier;
import weka.classifiers.functions.SMOreg;
import weka.classifiers.Evaluation;

import java.util.Random;

public class SMOregEvaluation {
    public static void main(String[] args) throws Exception {
        String datasetPath = "/Users/hungtran/Downloads/Projects/data-mining/modelEvaluation/data/output.csv";
        DataSource source = new DataSource(datasetPath);
        Instances dataset = source.getDataSet();

        String targetColumnName = "vote_average";
        int classIndex = dataset.attribute(targetColumnName).index();
        dataset.setClassIndex(classIndex);

        Runtime runtime = Runtime.getRuntime();
        System.out.println("Max Heap Size: " + (runtime.maxMemory() / 1024 / 1024) + " MB");
        System.out.println("Total Heap Size: " + (runtime.totalMemory() / 1024 / 1024) + " MB");
        System.out.println("Free Heap Size: " + (runtime.freeMemory() / 1024 / 1024) + " MB");

        Classifier smoReg = new SMOreg();

        // Đo thời gian chạy
        long startTime = System.currentTimeMillis();
        Evaluation evaluation = evaluateModel(smoReg, dataset);
        long endTime = System.currentTimeMillis();

        System.out.println("SMOreg:");
        printEvaluationResults(evaluation);
        System.out.println("Thời gian chạy: " + (endTime - startTime) + " ms");
    }

    public static Evaluation evaluateModel(Classifier classifier, Instances data) throws Exception {
        Evaluation evaluation = new Evaluation(data);
        System.out.println("Bắt đầu cross-validation 10-fold...");
        evaluation.crossValidateModel(classifier, data, 10, new Random(1));
        System.out.println("Hoàn thành cross-validation.");
        return evaluation;
    }

    public static void printEvaluationResults(Evaluation evaluation) throws Exception {
        System.out.println("MAE: " + evaluation.meanAbsoluteError());
        System.out.println("RMSE: " + evaluation.rootMeanSquaredError());
        System.out.println("Correlation: " + evaluation.correlationCoefficient());
    }
}