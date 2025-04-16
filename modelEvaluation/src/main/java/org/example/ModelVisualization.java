package org.example;

import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.classifiers.Classifier;
import weka.classifiers.evaluation.Prediction;
import weka.classifiers.trees.RandomForest;
import weka.classifiers.functions.LinearRegression;
import weka.classifiers.Evaluation;
import weka.gui.visualize.PlotData2D;
import weka.gui.visualize.ThresholdVisualizePanel;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Random;

public class ModelVisualization {
    public static void main(String[] args) throws Exception {
        System.out.println("Bắt đầu trực quan hóa kết quả mô hình...");

        // Tải dataset
        String datasetPath = "/Users/hungtran/Downloads/Projects/data-mining/modelEvaluation/data/output.csv";
        DataSource source = new DataSource(datasetPath);
        Instances dataset = source.getDataSet();

        // Đặt cột "vote_average" là mục tiêu
        String targetColumnName = "vote_average";
        int classIndex = dataset.attribute(targetColumnName).index();
        dataset.setClassIndex(classIndex);

        // Chia tập dữ liệu thành tập huấn luyện và tập kiểm tra
        Random rand = new Random(1);
        Instances randomizedData = new Instances(dataset);
        randomizedData.randomize(rand);

        int trainSize = (int) Math.round(randomizedData.numInstances() * 0.8);
        int testSize = randomizedData.numInstances() - trainSize;

        Instances trainData = new Instances(randomizedData, 0, trainSize);
        Instances testData = new Instances(randomizedData, trainSize, testSize);

        // Huấn luyện mô hình Random Forest (hoặc mô hình nào đó cho kết quả tốt nhất)
        RandomForest rf = new RandomForest();
        rf.buildClassifier(trainData);

        // Huấn luyện mô hình Linear Regression
        LinearRegression lr = new LinearRegression();
        lr.buildClassifier(trainData);

        // Đánh giá mô hình trên tập kiểm tra
        Evaluation rfEvaluation = new Evaluation(trainData);
        rfEvaluation.evaluateModel(rf, testData);

        Evaluation lrEvaluation = new Evaluation(trainData);
        lrEvaluation.evaluateModel(lr, testData);

        // In kết quả
        System.out.println("Random Forest:");
        System.out.println("MAE: " + rfEvaluation.meanAbsoluteError());
        System.out.println("RMSE: " + rfEvaluation.rootMeanSquaredError());
        System.out.println("Correlation: " + rfEvaluation.correlationCoefficient());

        System.out.println("\nLinear Regression:");
        System.out.println("MAE: " + lrEvaluation.meanAbsoluteError());
        System.out.println("RMSE: " + lrEvaluation.rootMeanSquaredError());
        System.out.println("Correlation: " + lrEvaluation.correlationCoefficient());

        // Lưu dự đoán vào file CSV để trực quan hóa
        BufferedWriter predictionWriter = new BufferedWriter(new FileWriter("results/predictions.csv"));
        predictionWriter.write("actual,predicted_rf,predicted_lr\n");

        // Dự đoán trên tập kiểm tra và lưu kết quả
        ArrayList<Prediction> rfPredictions = rfEvaluation.predictions();
        ArrayList<Prediction> lrPredictions = lrEvaluation.predictions();

        for (int i = 0; i < rfPredictions.size(); i++) {
            double actual = rfPredictions.get(i).actual();
            double predictedRF = rfPredictions.get(i).predicted();
            double predictedLR = lrPredictions.get(i).predicted();

            predictionWriter.write(actual + "," + predictedRF + "," + predictedLR + "\n");
        }

        predictionWriter.close();
        System.out.println("\nĐã lưu dự đoán vào: results/predictions.csv");
        System.out.println("Bạn có thể sử dụng file này để trực quan hóa kết quả bằng các công cụ như Excel, Python, R, v.v.");

        // In hướng dẫn để trực quan hóa
        System.out.println("\nHướng dẫn trực quan hóa kết quả bằng Python:");
        System.out.println("1. Cài đặt Python (nếu chưa có)");
        System.out.println("2. Cài đặt các thư viện: pandas, matplotlib, seaborn");
        System.out.println("3. Tạo file visualize.py với nội dung sau:");

        System.out.println("```python");
        System.out.println("import pandas as pd");
        System.out.println("import matplotlib.pyplot as plt");
        System.out.println("import seaborn as sns");
        System.out.println("");
        System.out.println("# Đọc dữ liệu");
        System.out.println("data = pd.read_csv('results/predictions.csv')");
        System.out.println("");
        System.out.println("# Tạo scatter plot cho Random Forest");
        System.out.println("plt.figure(figsize=(12, 6))");
        System.out.println("plt.subplot(1, 2, 1)");
        System.out.println("plt.scatter(data['actual'], data['predicted_rf'], alpha=0.5)");
        System.out.println("plt.plot([data['actual'].min(), data['actual'].max()], [data['actual'].min(), data['actual'].max()], 'r--')");
        System.out.println("plt.xlabel('Giá trị thực tế')");
        System.out.println("plt.ylabel('Giá trị dự đoán')");
        System.out.println("plt.title('Random Forest')");
        System.out.println("");
        System.out.println("# Tạo scatter plot cho Linear Regression");
        System.out.println("plt.subplot(1, 2, 2)");
        System.out.println("plt.scatter(data['actual'], data['predicted_lr'], alpha=0.5)");
        System.out.println("plt.plot([data['actual'].min(), data['actual'].max()], [data['actual'].min(), data['actual'].max()], 'r--')");
        System.out.println("plt.xlabel('Giá trị thực tế')");
        System.out.println("plt.ylabel('Giá trị dự đoán')");
        System.out.println("plt.title('Linear Regression')");
        System.out.println("");
        System.out.println("plt.tight_layout()");
        System.out.println("plt.savefig('results/model_comparison.png')");
        System.out.println("plt.show()");
        System.out.println("");
        System.out.println("# Tạo box plot để so sánh sai số tuyệt đối");
        System.out.println("plt.figure(figsize=(10, 6))");
        System.out.println("data['error_rf'] = abs(data['actual'] - data['predicted_rf'])");
        System.out.println("data['error_lr'] = abs(data['actual'] - data['predicted_lr'])");
        System.out.println("errors = pd.DataFrame({");
        System.out.println("    'Random Forest': data['error_rf'],");
        System.out.println("    'Linear Regression': data['error_lr']");
        System.out.println("})");
        System.out.println("errors = pd.melt(errors)");
        System.out.println("sns.boxplot(x='variable', y='value', data=errors)");
        System.out.println("plt.xlabel('Mô hình')");
        System.out.println("plt.ylabel('Sai số tuyệt đối')");
        System.out.println("plt.title('So sánh sai số giữa các mô hình')");
        System.out.println("plt.savefig('results/error_comparison.png')");
        System.out.println("plt.show()");
        System.out.println("```");

        System.out.println("\n4. Chạy file visualize.py bằng lệnh: python visualize.py");
    }
}