package org.example;

import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.LinearRegression;
import weka.classifiers.trees.RandomForest;

import java.util.Random;

public class ModelEvaluation {
    public static void main(String[] args) throws Exception {
        System.out.println("Max Heap Size: " + Runtime.getRuntime().maxMemory() / (1024 * 1024) + " MB");

        // Tải dataset
        String datasetPath = "/Users/hungtran/Downloads/Projects/data-mining/modelEvaluation/data/output.csv"; // Đường dẫn đến dataset đã xử lý
        DataSource source = new DataSource(datasetPath);
        Instances dataset = source.getDataSet();

        // Đặt cột "vote_average" là mục tiêu
        String targetColumnName = "vote_average"; // Tên cột mục tiêu
        int classIndex = dataset.attribute(targetColumnName).index(); // Lấy chỉ số của cột mục tiêu
        dataset.setClassIndex(classIndex);

        // Khởi tạo các mô hình
        Classifier randomForest = new RandomForest();
        Classifier linearRegression = new LinearRegression();

        // Thực hiện đánh giá 10-fold cross-validation cho Random Forest
        System.out.println("Đang đánh giá Random Forest...");
        Evaluation rfEvaluation = evaluateModel(randomForest, dataset);

        // Thực hiện đánh giá 10-fold cross-validation cho Linear Regression
        System.out.println("Đang đánh giá Linear Regression...");
        Evaluation lrEvaluation = evaluateModel(linearRegression, dataset);

        // In kết quả
        System.out.println("\n--- Kết quả ---");
        System.out.println("Random Forest:");
        printEvaluationResults(rfEvaluation);
        System.out.println("Linear Regression:");
        printEvaluationResults(lrEvaluation);
    }

    // Phương thức để đánh giá mô hình sử dụng 10-fold cross-validation
    public static Evaluation evaluateModel(Classifier classifier, Instances data) throws Exception {
        Evaluation evaluation = new Evaluation(data);
        long startTime = System.currentTimeMillis(); // Đo thời gian chạy
        evaluation.crossValidateModel(classifier, data, 10, new Random(1)); // 10-fold cross-validation
        long endTime = System.currentTimeMillis();
        System.out.println("Thời gian chạy: " + (endTime - startTime) / 1000.0 + " giây");
        return evaluation;
    }

    // Phương thức in các chỉ số đánh giá
    public static void printEvaluationResults(Evaluation evaluation) throws Exception {
        System.out.println("Mean Absolute Error (MAE): " + evaluation.meanAbsoluteError());
        System.out.println("Root Mean Squared Error (RMSE): " + evaluation.rootMeanSquaredError());
        System.out.println("Correlation Coefficient: " + evaluation.correlationCoefficient());
    }
}