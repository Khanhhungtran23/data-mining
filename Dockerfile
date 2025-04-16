FROM openjdk:11-jdk-slim

# Set working directory
WORKDIR /app

# Copy the jar file
COPY target/movie-rating-prediction-*.jar app.jar

# Copy data directory
COPY data/ /app/data/

# Create logs directory
RUN mkdir -p /app/logs

# JVM arguments
ENV JAVA_OPTS="-Xms512m -Xmx1g"

# Expose the application port
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]