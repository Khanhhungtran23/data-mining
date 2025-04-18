package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@SpringBootApplication
public class MovieRatingPredictionApplication {

    public static void main(String[] args) {
        SpringApplication.run(MovieRatingPredictionApplication.class, args);
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Movie Rating Prediction API")
                        .version("1.1")
                        .description("API for predicting movie averageRating based on movie attributes"));
    }
}