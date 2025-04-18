package org.example.service;

import org.example.dto.MovieRequestDTO;
import org.example.exception.ModelPredictionException;
import org.example.util.DataPreparationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import weka.classifiers.Classifier;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.classifiers.trees.RandomForest;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Service for movie rating prediction
 */
@Service
public class MovieRatingPredictionService {

    private static final Logger logger = LoggerFactory.getLogger(MovieRatingPredictionService.class);

    private Classifier model;
    private ArrayList<Attribute> attributes;
    private String modelType = "Not loaded yet";
    private Instances datasetStructure;
    private final AtomicBoolean modelTrained = new AtomicBoolean(false);
    private final AtomicBoolean trainingInProgress = new AtomicBoolean(false);

    /**
     * Initialize basic structure and start async model training
     */
    @PostConstruct
    public void initModel() {
        logger.info("Initializing prediction service...");

        // Start model training in background
        CompletableFuture.runAsync(this::trainModelAsync);
    }

    /**
     * Trains the model asynchronously to allow the application to start
     */
    private void trainModelAsync() {
        // Prevent multiple training attempts
        if (trainingInProgress.getAndSet(true)) {
            logger.info("Model training already in progress");
            return;
        }

        try {
            logger.info("Starting background model training...");

            // Load dataset to learn the structure
            DataSource source = new DataSource("data/output.csv");
            Instances dataset = source.getDataSet();

            // Set the class index (averageRating)
            String targetColumnName = "averageRating";
            int classIndex = dataset.attribute(targetColumnName).index();
            dataset.setClassIndex(classIndex);

            // Store the dataset structure for later use
            this.datasetStructure = new Instances(dataset, 0);

            // Extract the attribute information
            this.attributes = new ArrayList<>();
            for (int i = 0; i < dataset.numAttributes(); i++) {
                attributes.add(dataset.attribute(i));
            }

            // Configure a more memory-efficient RandomForest
            model = new RandomForest();
            // Configure RandomForest with memory constraints
            ((RandomForest) model).setNumIterations(30);  // Reduced from 10 to 5 trees
            ((RandomForest) model).setMaxDepth(10);       // Reduced depth for memory efficiency
            ((RandomForest) model).setNumFeatures(5);    // Fewer features per tree
            ((RandomForest) model).setBatchSize("50");   // Smaller batch size
            ((RandomForest) model).setSeed(1);
            modelType = "RandomForest (Memory-Optimized)";

            logger.info("Training the {} model...", modelType);
            model.buildClassifier(dataset);

            modelTrained.set(true);
            logger.info("Model training completed successfully");
        } catch (Exception e) {
            logger.error("Failed to train the prediction model", e);
            modelType = "Training Failed - Using Fallback";
            // We won't throw exception here to allow app to continue running
        } finally {
            trainingInProgress.set(false);
        }
    }

    /**
     * Make a prediction for a movie's rating
     *
     * @param movieRequestDTO DTO containing movie data
     * @return The predicted rating
     */
    public double predictRating(MovieRequestDTO movieRequestDTO) {
        try {
            if (!modelTrained.get()) {
                logger.warn("Model not fully trained yet - using fallback prediction");
                return 7.0; // Return a reasonable default value
            }

            // Use utility class to prepare the data for prediction
            Instances predictionDataset = DataPreparationUtil.prepareInstanceForPrediction(
                    movieRequestDTO, datasetStructure);

            // Get the first (and only) instance from our prediction dataset
            DenseInstance instance = (DenseInstance) predictionDataset.instance(0);

            // Make prediction
            double prediction = model.classifyInstance(instance);

            // Ensure prediction is within reasonable limits for a movie rating (0-10)
            prediction = Math.max(0, Math.min(10, prediction));

            logger.info("Predicted averageRating for movie '{}': {}", movieRequestDTO.getTitle(), prediction);
            return prediction * 10 + 1;

        } catch (Exception e) {
            logger.error("Error predicting movie averageRating", e);
            throw new ModelPredictionException("Failed to predict movie averageRating", e);
        }
    }

    /**
     * Get the name of the model being used for predictions
     */
    public String getModelType() {
        return modelType + (modelTrained.get() ? " (Trained)" : " (Training in progress)");
    }

    /**
     * Check if model is trained and ready
     */
    public boolean isModelTrained() {
        return modelTrained.get();
    }
}