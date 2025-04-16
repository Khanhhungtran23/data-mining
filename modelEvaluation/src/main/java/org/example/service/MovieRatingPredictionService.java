package org.example.service;

import org.example.dto.MovieRequestDTO;
import org.example.exception.ModelPredictionException;
import org.example.util.DataPreparationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import weka.classifiers.Classifier;
import weka.classifiers.trees.M5P;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.classifiers.trees.RandomForest;

import javax.annotation.PostConstruct;
import java.util.ArrayList;

/**
 * Service for movie rating prediction
 */
@Service
public class MovieRatingPredictionService {

    private static final Logger logger = LoggerFactory.getLogger(MovieRatingPredictionService.class);

    private Classifier model;
    private ArrayList<Attribute> attributes;
    private String modelType;
    private Instances datasetStructure;

    /**
     * Initialize the prediction model when the service starts
     */
    @PostConstruct
    public void initModel() {
        try {
            logger.info("Loading and training the prediction model...");

            // Load dataset to learn the structure
            DataSource source = new DataSource("/Users/hungtran/Downloads/Projects/data-mining/modelEvaluation/data/output.csv");
            Instances dataset = source.getDataSet();

            // Set the class index (vote_average)
            String targetColumnName = "vote_average";
            int classIndex = dataset.attribute(targetColumnName).index();
            dataset.setClassIndex(classIndex);

            // Store the dataset structure for later use
            this.datasetStructure = new Instances(dataset, 0);

            // Extract the attribute information
            this.attributes = new ArrayList<>();
            for (int i = 0; i < dataset.numAttributes(); i++) {
                attributes.add(dataset.attribute(i));
            }

            // Choose and train the model
            // We're using M5P based on the assumption that it's a better model for our use case
//            model = new M5P();
//            modelType = "M5P Decision Tree";

            // Choose and train the model
            model = new RandomForest();
            // Cấu hình các tham số cho Random Forest
            ((RandomForest) model).setNumIterations(100); // Số cây = 100
            ((RandomForest) model).setMaxDepth(0);   // 0 = không giới hạn độ sâu
            ((RandomForest) model).setSeed(1);       // Giá trị seed cố định để kết quả có thể tái lập
            modelType = "Random Forest";

            logger.info("Training the {} model...", modelType);
            model.buildClassifier(dataset);

            logger.info("Model training completed successfully");
        } catch (Exception e) {
            logger.error("Failed to initialize the prediction model", e);
            throw new ModelPredictionException("Model initialization failed", e);
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
            // Use utility class to prepare the data for prediction
            Instances predictionDataset = DataPreparationUtil.prepareInstanceForPrediction(
                    movieRequestDTO, datasetStructure);

            // Get the first (and only) instance from our prediction dataset
            DenseInstance instance = (DenseInstance) predictionDataset.instance(0);

            // Make prediction
            double prediction = model.classifyInstance(instance);

            // Ensure prediction is within reasonable limits for a movie rating (0-10)
            prediction = Math.max(0, Math.min(10, prediction));

            logger.info("Predicted rating for movie '{}': {}", movieRequestDTO.getTitle(), prediction);
            return prediction;

        } catch (Exception e) {
            logger.error("Error predicting movie rating", e);
            throw new ModelPredictionException("Failed to predict movie rating", e);
        }
    }

    /**
     * Get the name of the model being used for predictions
     */
    public String getModelType() {
        return modelType;
    }
}