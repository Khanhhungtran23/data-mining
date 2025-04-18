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
ENV PORT=8080

# Expose the application port
EXPOSE ${PORT}

# Command to run the application
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -Dserver.port=${PORT} -jar app.jar"]