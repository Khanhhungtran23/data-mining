package org.example;

import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.classifiers.Classifier;
import weka.classifiers.trees.DecisionStump;
import weka.classifiers.Evaluation;

import java.util.Random;

public class DecisionStumpEvaluation {
    public static void main(String[] args) throws Exception {
        String datasetPath = "/Users/hungtran/Downloads/Projects/data-mining/modelEvaluation/data/output.csv";
        DataSource source = new DataSource(datasetPath);
        Instances dataset = source.getDataSet();

        // Nếu muốn chạy với tập dữ liệu nhỏ hơn để thử nghiệm, bỏ comment dòng sau
        // dataset = new Instances(dataset, 0, 10000); // Sử dụng 10,000 instances đầu tiên

        String targetColumnName = "vote_average";
        int classIndex = dataset.attribute(targetColumnName).index();
        dataset.setClassIndex(classIndex);

        Runtime runtime = Runtime.getRuntime();
        System.out.println("Max Heap Size: " + (runtime.maxMemory() / 1024 / 1024) + " MB");
        System.out.println("Total Heap Size: " + (runtime.totalMemory() / 1024 / 1024) + " MB");
        System.out.println("Free Heap Size: " + (runtime.freeMemory() / 1024 / 1024) + " MB");

        Classifier decisionStump = new DecisionStump();

        // Đo thời gian chạy
        long startTime = System.currentTimeMillis();
        Evaluation evaluation = evaluateModelWithProgress(decisionStump, dataset, 10); // 10-fold CV
        long endTime = System.currentTimeMillis();

        System.out.println("Decision Stump:");
        printEvaluationResults(evaluation);
        System.out.println("Thời gian chạy: " + (endTime - startTime) + " ms");
    }

    public static Evaluation evaluateModelWithProgress(Classifier classifier, Instances data, int numFolds) throws Exception {
        Evaluation evaluation = new Evaluation(data);
        Random rand = new Random(1); // Seed để đảm bảo kết quả tái lập được
        Instances randomizedData = new Instances(data);
        randomizedData.randomize(rand);

        if (randomizedData.classAttribute().isNominal()) {
            randomizedData.stratify(numFolds);
        }

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

    public static void printEvaluationResults(Evaluation evaluation) throws Exception {
        System.out.println("MAE: " + evaluation.meanAbsoluteError());
        System.out.println("RMSE: " + evaluation.rootMeanSquaredError());
        System.out.println("Correlation: " + evaluation.correlationCoefficient());
    }
}