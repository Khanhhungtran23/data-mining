# ---------------------
# Build Stage
# ---------------------
FROM maven:3.8.5-openjdk-11 AS build

WORKDIR /app

COPY modelEvaluation/pom.xml .
COPY modelEvaluation/src ./src
COPY modelEvaluation/data ./data

# Create logs directory
RUN mkdir -p /app/logs

# Build project with reduced memory usage
RUN mvn clean package -DskipTests -Dmaven.test.skip=true -Dmaven.javadoc.skip=true

# ---------------------
# Runtime Stage
# ---------------------
FROM openjdk:11-jdk-slim

WORKDIR /app

COPY --from=build /app/target/movie-rating-prediction-1.0-SNAPSHOT.jar app.jar
COPY --from=build /app/data /app/data
# Create logs directory
RUN mkdir -p /app/logs

# Optimize for memory constrained environments
ENV JAVA_OPTS="-Xms128m -Xmx400m -XX:+UseSerialGC -XX:+UseCompressedOops -XX:MaxMetaspaceSize=128m -Xss256k -XX:+AlwaysPreTouch -XX:+DisableExplicitGC"
ENV PORT=8080

# Explicitly expose the port
EXPOSE ${PORT}

# Health check to confirm app is running
#HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 CMD wget -q --spider http://localhost:${PORT}/health || exit 1

# Start the application with memory limits and ensure we bind to all interfaces
ENTRYPOINT ["sh", "-c", "echo 'Starting application with memory settings: ${JAVA_OPTS}' && java ${JAVA_OPTS} -Dserver.port=${PORT} -Dserver.address=0.0.0.0 -jar app.jar"]