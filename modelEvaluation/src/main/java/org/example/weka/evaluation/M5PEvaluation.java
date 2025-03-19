package org.example.weka.evaluation;

import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.classifiers.Classifier;
import weka.classifiers.trees.M5P;
import weka.classifiers.Evaluation;
import weka.core.SerializationHelper;

import java.util.Random;
import java.io.File;

public class M5PEvaluation implements ModelEvaluator {

    private static final String DEFAULT_DATASET_PATH = "src/main/resources/data/train.csv";
    private static final String TARGET_COLUMN = "vote_average";
    private static final String MODEL_OUTPUT_PATH = "src/main/resources/models/m5p_model.model";

    public static void main(String[] args) throws Exception {
        M5PEvaluation evaluator = new M5PEvaluation();

        // Use command line argument for dataset path if provided
        String datasetPath = args.length > 0 ? args[0] : DEFAULT_DATASET_PATH;

        // Evaluate the model and save it if requested
        evaluator.evaluate(datasetPath, TARGET_COLUMN, true);
    }

    @Override
    public void evaluate(String datasetPath, String targetColumn) throws Exception {
        evaluate(datasetPath, targetColumn, false);
    }

    /**
     * Evaluates the M5P model on the given dataset
     *
     * @param datasetPath Path to the dataset CSV file
     * @param targetColumn Name of the target column to predict
     * @param saveModel Whether to save the trained model
     * @throws Exception If an error occurs during evaluation
     */
    public void evaluate(String datasetPath, String targetColumn, boolean saveModel) throws Exception {
        System.out.println("Loading dataset from: " + datasetPath);

        // Load dataset
        DataSource source = new DataSource(datasetPath);
        Instances dataset = source.getDataSet();

        // Check if the target column exists
        if (dataset.attribute(targetColumn) == null) {
            throw new IllegalArgumentException("Target column '" + targetColumn + "' not found in the dataset.");
        }

        // Set the target attribute (class index) for the model
        int classIndex = dataset.attribute(targetColumn).index();
        dataset.setClassIndex(classIndex);

        System.out.println("Dataset loaded successfully. Total instances: " + dataset.numInstances());
        System.out.println("Target attribute: " + targetColumn + " (index: " + classIndex + ")");

        // Initialize the M5P model (regression tree)
        M5P m5p = new M5P();

        // Optional: Configure M5P parameters
        // m5p.setMinNumInstances(4);
        // m5p.setBuildRegressionTree(true);
        // m5p.setUnpruned(false);

        // Measure runtime
        System.out.println("\nStarting 10-fold cross-validation...");
        long startTime = System.currentTimeMillis(); // Start timer
        Evaluation evaluation = evaluateModelWithProgress(m5p, dataset, 10); // 10-fold cross-validation
        long endTime = System.currentTimeMillis();   // End timer
        long runtimeMs = endTime - startTime;

        // Print the results
        System.out.println("\n==== M5P Decision Tree Evaluation Results ====");
        printEvaluationResults(evaluation);
        System.out.println("Runtime: " + runtimeMs + " ms (" + (runtimeMs / 1000.0) + " seconds)");

        // Save the trained model if requested
        if (saveModel) {
            // Train the model on the full dataset before saving
            System.out.println("\nTraining model on full dataset for saving...");
            m5p.buildClassifier(dataset);

            // Create directory if it doesn't exist
            File modelDir = new File(MODEL_OUTPUT_PATH).getParentFile();
            if (!modelDir.exists()) {
                modelDir.mkdirs();
            }

            // Save the model
            SerializationHelper.write(MODEL_OUTPUT_PATH, m5p);
            System.out.println("Model saved to: " + MODEL_OUTPUT_PATH);
        }
    }

    /**
     * Evaluates the model with progress display during cross-validation
     *
     * @param classifier The classifier to evaluate
     * @param data The dataset to use for evaluation
     * @param numFolds Number of folds for cross-validation
     * @return Evaluation object with results
     * @throws Exception If an error occurs during evaluation
     */
    public static Evaluation evaluateModelWithProgress(Classifier classifier, Instances data, int numFolds) throws Exception {
        Evaluation evaluation = new Evaluation(data);
        Random rand = new Random(1); // Seed for reproducibility
        Instances randomizedData = new Instances(data);
        randomizedData.randomize(rand);

        // Stratify if the class attribute is nominal
        if (randomizedData.classAttribute().isNominal()) {
            randomizedData.stratify(numFolds);
        }

        // Perform cross-validation
        for (int i = 0; i < numFolds; i++) {
            Instances train = randomizedData.trainCV(numFolds, i, rand);
            Instances test = randomizedData.testCV(numFolds, i);

            // Build and evaluate the model
            classifier.buildClassifier(train);
            evaluation.evaluateModel(classifier, test);

            // Display progress
            int progress = (int) (((double) (i + 1) / numFolds) * 100);
            System.out.println("Progress: " + progress + "% (Fold " + (i + 1) + "/" + numFolds + ")");
        }
        return evaluation;
    }

    /**
     * Prints the evaluation results
     *
     * @param evaluation The evaluation object containing results
     * @throws Exception If an error occurs during printing
     */
    public static void printEvaluationResults(Evaluation evaluation) throws Exception {
        System.out.println("Mean Absolute Error (MAE): " + evaluation.meanAbsoluteError());
        System.out.println("Root Mean Squared Error (RMSE): " + evaluation.rootMeanSquaredError());
        System.out.println("Relative Absolute Error: " + evaluation.relativeAbsoluteError() + "%");
        System.out.println("Root Relative Squared Error: " + evaluation.rootRelativeSquaredError() + "%");
        System.out.println("Correlation Coefficient: " + evaluation.correlationCoefficient());
        System.out.println("Total Number of Instances: " + evaluation.numInstances());
    }
}