package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.example.dto.MovieRequestDTO;
import org.example.dto.PredictionResponseDTO;
import org.example.service.PMMLModelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/movies")
@Validated
public class MovieRatingController {

    private static final Logger logger = LoggerFactory.getLogger(MovieRatingController.class);

    private final PMMLModelService predictionService;

    public MovieRatingController(PMMLModelService predictionService) {
        this.predictionService = predictionService;
    }

    @PostMapping("/predict-rating")
    @Operation(summary = "Predict a movie's rating based on its attributes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rating prediction successful",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = PredictionResponseDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<PredictionResponseDTO> predictRating(@Valid @RequestBody MovieRequestDTO movieRequestDTO) {
        logger.info("Received request to predict rating for movie: {}", movieRequestDTO.getTitle());

        long startTime = System.currentTimeMillis();

        try {
            double predictedRating = predictionService.predictRating(movieRequestDTO);
            long processingTime = System.currentTimeMillis() - startTime;

            PredictionResponseDTO response = PredictionResponseDTO.builder()
                    .movieTitle(movieRequestDTO.getTitle())
                    .predictedRating(predictedRating)
                    .message("Rating prediction successful")
                    .processingTimeMs(processingTime)
                    .modelUsed(predictionService.getModelType())
                    .build();

            logger.info("Rating prediction for '{}' completed in {} ms. Predicted rating: {}",
                    movieRequestDTO.getTitle(), processingTime, predictedRating);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            long processingTime = System.currentTimeMillis() - startTime;
            logger.error("Error predicting rating for movie: {}", movieRequestDTO.getTitle(), e);

            PredictionResponseDTO response = PredictionResponseDTO.builder()
                    .movieTitle(movieRequestDTO.getTitle())
                    .message("Error predicting rating: " + e.getMessage())
                    .processingTimeMs(processingTime)
                    .modelUsed(predictionService.getModelType())
                    .build();

            return ResponseEntity.internalServerError().body(response);
        }
    }

    @GetMapping("/health")
    @Operation(summary = "Check if the prediction service is up and running")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Movie Rating Prediction Service is running. Model: " +
                predictionService.getModelType());
    }
}