package org.example;

import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.classifiers.Classifier;
import weka.classifiers.trees.M5P;
import weka.classifiers.Evaluation;

import java.util.Random;

public class M5PEvaluation {
    public static void main(String[] args) throws Exception {
        // Tải dataset
        String datasetPath = "/Users/hungtran/Downloads/Projects/data-mining/modelEvaluation/data/output.csv";
        DataSource source = new DataSource(datasetPath);
        Instances dataset = source.getDataSet();

        // Đặt thuộc tính mục tiêu (class index) cho mô hình
        String targetColumnName = "vote_average";
        int classIndex = dataset.attribute(targetColumnName).index();
        dataset.setClassIndex(classIndex);

        // Khởi tạo mô hình M5P (cây hồi quy)
        Classifier m5p = new M5P();

        // Đo thời gian chạy
        long startTime = System.currentTimeMillis(); // Bắt đầu đo thời gian
        Evaluation evaluation = evaluateModelWithProgress(m5p, dataset, 10); // 10-fold cross-validation
        long endTime = System.currentTimeMillis();   // Kết thúc đo thời gian

        // In kết quả
        System.out.println("M5P Decision Tree:");
        printEvaluationResults(evaluation);
        System.out.println("Thời gian chạy: " + (endTime - startTime) + " ms");
    }

    // Phương thức đánh giá mô hình với hiển thị tiến độ trong 10-fold cross-validation
    public static Evaluation evaluateModelWithProgress(Classifier classifier, Instances data, int numFolds) throws Exception {
        Evaluation evaluation = new Evaluation(data);
        Random rand = new Random(1); // Seed để đảm bảo kết quả tái lập được
        Instances randomizedData = new Instances(data);
        randomizedData.randomize(rand);

        // Phân tầng nếu thuộc tính lớp là danh nghĩa
        if (randomizedData.classAttribute().isNominal()) {
            randomizedData.stratify(numFolds);
        }

        // Thực hiện 10-fold cross-validation
        for (int i = 0; i < numFolds; i++) {
            Instances train = randomizedData.trainCV(numFolds, i, rand);
            Instances test = randomizedData.testCV(numFolds, i);

            // Xây dựng và đánh giá mô hình
            classifier.buildClassifier(train);
            evaluation.evaluateModel(classifier, test);

            // Hiển thị tiến độ
            int progress = (int) (((double) (i + 1) / numFolds) * 100);
            System.out.println("Tiến độ: " + progress + "%");
        }
        return evaluation;
    }

    // Phương thức in kết quả đánh giá
    public static void printEvaluationResults(Evaluation evaluation) throws Exception {
        System.out.println("MAE: " + evaluation.meanAbsoluteError());
        System.out.println("RMSE: " + evaluation.rootMeanSquaredError());
        System.out.println("Correlation: " + evaluation.correlationCoefficient());
    }
}