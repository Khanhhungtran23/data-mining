# Using OpenJDK17 to base image
FROM openjdk:17-jdk-slim

# Info maintainer
LABEL maintainer = "tvkhhung03@gmail.com"

# Workspace in container
WORKDIR /app

# Copy file jar of application into container
COPY target/modelEvaluation-1.0-SNAPSHOT-jar-with-dependencies.jar app.jar

# Copy folder models
COPY src/main/resources/models /app/models

# Environemnt Variables
ENV MODEL_PATH=/app/models/
ENV MODEL_FILENAME=best_model.model
ENV PREPROCESSING_PARAMS_PATH=/app/models/preprocessing_params.json

# Expose port to run Spring Boot application
EXPOSE 8080

# Command to run
ENTRYPOINT ["java", "-jar", "app.jar"]