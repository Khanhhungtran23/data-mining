package org.example;

import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.trees.DecisionStump;
import weka.classifiers.trees.REPTree;
import weka.classifiers.trees.M5P;
import weka.classifiers.trees.RandomForest;
import weka.classifiers.functions.LinearRegression;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.functions.SMOreg;
import weka.classifiers.lazy.IBk;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ModelComparison {
    public static void main(String[] args) throws Exception {
        System.out.println("Bắt đầu đánh giá mô hình...");

        // Kiểm tra xem file output.csv đã tồn tại chưa
        File outputFile = new File("/Users/hungtran/Downloads/Projects/data-mining/modelEvaluation/data/output.csv");
        if (!outputFile.exists()) {
            System.out.println("File output.csv chưa tồn tại. Đang tiến hành tiền xử lý dữ liệu...");
            Main.main(args); // Chạy lớp Main để tiền xử lý dữ liệu
        }

        // Tải dataset
        String datasetPath = "/Users/hungtran/Downloads/Projects/data-mining/modelEvaluation/data/output.csv";
        DataSource source = new DataSource(datasetPath);
        Instances dataset = source.getDataSet();

        // Đặt cột "vote_average" là mục tiêu
        String targetColumnName = "vote_average";
        int classIndex = dataset.attribute(targetColumnName).index();
        dataset.setClassIndex(classIndex);

        System.out.println("Đã tải dataset với " + dataset.numInstances() + " instances và "
                + dataset.numAttributes() + " thuộc tính.");

        // Tạo một bản sao nhỏ của dataset cho mô hình Gaussian Processes (nếu cần)
        Instances smallDataset = new Instances(dataset, 0, Math.min(10000, dataset.numInstances()));
        smallDataset.setClassIndex(classIndex);

        // Khởi tạo các mô hình
        List<ModelInfo> models = new ArrayList<>();
        models.add(new ModelInfo("Linear Regression", new LinearRegression(), dataset));
        models.add(new ModelInfo("Decision Stump", new DecisionStump(), dataset));
        models.add(new ModelInfo("REPTree", new REPTree(), dataset));
        models.add(new ModelInfo("M5P", new M5P(), dataset));
        models.add(new ModelInfo("Random Forest", new RandomForest(), dataset));
        models.add(new ModelInfo("K-Nearest Neighbors (K=5)", new IBk(5), dataset));
        models.add(new ModelInfo("Multilayer Perceptron", new MultilayerPerceptron(), dataset));
        models.add(new ModelInfo("SMOreg", new SMOreg(), dataset));

        // Tạo thư mục kết quả
        File resultsDir = new File("results");
        if (!resultsDir.exists()) {
            resultsDir.mkdirs();
        }

        // Tạo file kết quả
        PrintWriter resultWriter = new PrintWriter(new FileWriter("results/model_comparison.txt"));
        resultWriter.println("==== So sánh các mô hình dự đoán rating phim ====");
        resultWriter.println();

        // Bảng để in kết quả trên console
        System.out.println("\n==== Kết quả đánh giá các mô hình ====");
        System.out.printf("%-30s %-15s %-15s %-15s %-15s%n",
                "Mô hình", "MAE", "RMSE", "Correlation", "Thời gian (ms)");
        System.out.println("-".repeat(90));

        resultWriter.printf("%-30s %-15s %-15s %-15s %-15s%n",
                "Mô hình", "MAE", "RMSE", "Correlation", "Thời gian (ms)");
        resultWriter.println("-".repeat(90));

        // Đánh giá từng mô hình
        for (ModelInfo model : models) {
            System.out.println("Đang đánh giá mô hình: " + model.name);

            long startTime = System.currentTimeMillis();
            Evaluation evaluation = evaluateModel(model.classifier, model.dataset);
            long endTime = System.currentTimeMillis();
            long runtime = endTime - startTime;

            // Lưu kết quả
            model.mae = evaluation.meanAbsoluteError();
            model.rmse = evaluation.rootMeanSquaredError();
            model.correlation = evaluation.correlationCoefficient();
            model.runtime = runtime;

            // In kết quả
            System.out.printf("%-30s %-15.4f %-15.4f %-15.4f %-15d%n",
                    model.name, model.mae, model.rmse, model.correlation, model.runtime);

            resultWriter.printf("%-30s %-15.4f %-15.4f %-15.4f %-15d%n",
                    model.name, model.mae, model.rmse, model.correlation, model.runtime);
        }

        // Đánh giá Gaussian Processes trên tập dữ liệu nhỏ
        String gpName = "Gaussian Processes (10k instances)";
        System.out.println("Đang đánh giá mô hình: " + gpName);

        Classifier gaussianProcesses = new weka.classifiers.functions.GaussianProcesses();
        long gpStartTime = System.currentTimeMillis();
        Evaluation gpEvaluation = evaluateModel(gaussianProcesses, smallDataset);
        long gpEndTime = System.currentTimeMillis();
        long gpRuntime = gpEndTime - gpStartTime;

        // In kết quả của Gaussian Processes
        System.out.printf("%-30s %-15.4f %-15.4f %-15.4f %-15d%n",
                gpName, gpEvaluation.meanAbsoluteError(), gpEvaluation.rootMeanSquaredError(),
                gpEvaluation.correlationCoefficient(), gpRuntime);

        resultWriter.printf("%-30s %-15.4f %-15.4f %-15.4f %-15d%n",
                gpName, gpEvaluation.meanAbsoluteError(), gpEvaluation.rootMeanSquaredError(),
                gpEvaluation.correlationCoefficient(), gpRuntime);

        resultWriter.close();
        System.out.println("\nĐã lưu kết quả vào: results/model_comparison.txt");
    }

    public static Evaluation evaluateModel(Classifier classifier, Instances data) throws Exception {
        Evaluation evaluation = new Evaluation(data);
        evaluation.crossValidateModel(classifier, data, 10, new Random(1));
        return evaluation;
    }

    // Lớp để lưu thông tin mô hình
    static class ModelInfo {
        String name;
        Classifier classifier;
        Instances dataset;
        double mae;
        double rmse;
        double correlation;
        long runtime;

        public ModelInfo(String name, Classifier classifier, Instances dataset) {
            this.name = name;
            this.classifier = classifier;
            this.dataset = dataset;
        }
    }
}