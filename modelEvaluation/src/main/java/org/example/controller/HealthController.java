package org.example.controller;

import org.example.service.PMMLModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class HealthController {

    @Autowired
    private PMMLModelService predictionService;

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("modelType", predictionService.getModelType());
        response.put("message", "Movie Rating Prediction Service is running");

        return ResponseEntity.ok(response);
    }

    // Adding a root endpoint for easy access
    @GetMapping("/")
    public ResponseEntity<String> root() {
        return ResponseEntity.ok("Movie Rating Prediction API is running. Access /swagger-ui.html for documentation.");
    }
}