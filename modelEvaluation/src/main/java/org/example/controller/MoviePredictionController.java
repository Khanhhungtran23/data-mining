package org.example.controller;

import org.example.model.MovieData;
import org.example.model.PredictionResponse;
import org.example.service.prediction.PredictionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/movies")
public class MoviePredictionController {

    private final PredictionService predictionService;

    @Autowired
    public MoviePredictionController(PredictionService predictionService) {
        this.predictionService = predictionService;
    }

    @PostMapping("/predict")
    public ResponseEntity<PredictionResponse> predictRating(@RequestBody MovieData movieData) {
        try {
            PredictionResponse prediction = predictionService.predictRating(movieData);
            return ResponseEntity.ok(prediction);
        } catch (Exception e) {
            // Log lỗi trước khi trả về response lỗi
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/trends")
    public ResponseEntity<?> getMovieTrends() {
        try {
            return ResponseEntity.ok(predictionService.getMovieTrends());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }
}