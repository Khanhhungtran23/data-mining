package org.example.service;

import org.dmg.pmml.FieldName;
import org.dmg.pmml.PMML;
import org.example.dto.MovieRequestDTO;
import org.example.exception.ModelPredictionException;
import org.jpmml.evaluator.*;
import org.jpmml.model.PMMLUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.jpmml.evaluator.mining.MiningModelEvaluator;

@Service
public class PMMLModelService {

    private static final Logger logger = LoggerFactory.getLogger(PMMLModelService.class);

    private Evaluator evaluator;
    private String modelType = "Random Forest (PMML)";
    private List<? extends InputField> inputFields;
    private List<? extends TargetField> targetFields;

    @PostConstruct
    public void initModel() {
        try {
            logger.info("Loading PMML model...");

            // Load PMML model from resources
            ClassPathResource resource = new ClassPathResource("src/main/resources/models/m5p_model.pmml");
            InputStream inputStream = resource.getInputStream();

            // Parse PMML
            PMML pmml = PMMLUtil.unmarshal(inputStream);

            // Create model evaluator
//            ModelEvaluatorFactory modelEvaluatorFactory = ModelEvaluatorFactory.newInstance();
            evaluator = new MiningModelEvaluator(pmml);


            // Get input and target fields
            inputFields = evaluator.getInputFields();
            targetFields = evaluator.getTargetFields();

            logger.info("PMML format - M5P model loaded successfully. Model type: {}", modelType);
            logger.info("Model has {} input fields and {} target fields",
                    inputFields.size(), targetFields.size());

        } catch (Exception e) {
            logger.error("Failed to initialize m5p model", e);
            throw new ModelPredictionException("Model initialization failed", e);
        }
    }

    public double predictRating(MovieRequestDTO movieRequestDTO) {
        try {
            // Prepare input data
            Map<FieldName, Object> inputData = new HashMap<>();

            for (InputField inputField : inputFields) {
                FieldName fieldName = inputField.getName();
                String fieldNameString = fieldName.getValue();

                // Map DTO fields to PMML model fields
                Object value = mapDtoFieldToModelInput(fieldNameString, movieRequestDTO);

                if (value != null) {
                    inputData.put(fieldName, value);
                }
            }

            // Evaluate model
            Map<FieldName, ?> results = evaluator.evaluate(inputData);

            // Get result
            List<? extends TargetField> targetFields = evaluator.getTargetFields();
            TargetField targetField = targetFields.get(0); // Assuming single target

            Object targetValue = results.get(targetField.getName());

            // Extract and scale prediction value (from 0-1 to 0-10 if needed)
            double prediction;
            if (targetValue instanceof Computable) {
                Object result = ((Computable) targetValue).getResult();
                if (result instanceof Number) {
                    prediction = ((Number) result).doubleValue();
                } else {
                    prediction = Double.parseDouble(result.toString());
                }
                // Scale if needed (uncomment if your model outputs 0-1 values)
                 prediction = prediction * 10;
            } else {
                prediction = Double.parseDouble(targetValue.toString());
                // Scale if needed (uncomment if your model outputs 0-1 values)
                 prediction = prediction * 10;
            }

            logger.info("Predicted rating for movie '{}': {}", movieRequestDTO.getTitle(), prediction);
            return prediction;

        } catch (Exception e) {
            logger.error("Error predicting movie rating", e);
            throw new ModelPredictionException("Failed to predict movie rating", e);
        }
    }

    private Object mapDtoFieldToModelInput(String fieldName, MovieRequestDTO dto) {
        switch (fieldName) {
            case "id":
                return dto.getId().doubleValue();
            case "vote_count":
                return dto.getVoteCount().doubleValue();
            case "revenue":
                return dto.getRevenue().doubleValue();
            case "runtime":
                return dto.getRuntime().doubleValue();
            case "budget":
                return dto.getBudget().doubleValue();
            case "popularity":
                return dto.getPopularity();
            case "vote_average": // This will be used as input now, not the target
                return dto.getVoteAverage();
            case "numVotes":
                return dto.getNumVotes().doubleValue();
            // Add others as needed based on the PMML model fields
            default:
                logger.warn("Field {} not found in DTO, returning null", fieldName);
                return null;
        }
    }

    public String getModelType() {
        return modelType;
    }
}