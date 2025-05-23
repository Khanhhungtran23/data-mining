# Movie Rating Prediction API

This Spring Boot application serves as a backend API for predicting movie ratings. It uses a machine learning model trained on movie attributes to predict ratings for new movies.

## Features

- RESTful API for movie rating prediction
- Uses Weka machine learning library with M5P Decision Tree model
- Swagger UI documentation
- Docker-ready for easy deployment

## Prerequisites

- Java 11 or higher
- Maven 3.6 or higher
- Docker and Docker Compose (optional, for containerized deployment)

## Project Structure

```
movie-rating-prediction/
├── data/
│   ├── cleaned_data.csv (original data file)
│   └── output.csv (processed data file)
├── src/
│   └── main/
│       ├── java/
│       │   └── org/
│       │       └── example/
│       │           ├── controller/
│       │           │   └── MovieRatingController.java
│       │           ├── dto/
│       │           │   ├── MovieRequestDTO.java
│       │           │   └── PredictionResponseDTO.java
│       │           ├── exception/
│       │           │   ├── GlobalExceptionHandler.java
│       │           │   └── ModelPredictionException.java
│       │           ├── service/
│       │           │   └── MovieRatingPredictionService.java
│       │           ├── util/
│       │           │   └── DataPreparationUtil.java
│       │           └── MovieRatingPredictionApplication.java
│       └── resources/
│           └── application.properties
├── Dockerfile
├── docker-compose.yml
└── pom.xml
```