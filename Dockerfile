# ---------------------
# Build Stage
# ---------------------
FROM maven:3.8.5-openjdk-11 AS build

WORKDIR /app

COPY modelEvaluation/pom.xml .
COPY modelEvaluation/src ./src
COPY modelEvaluation/data ./data

# Tạo thư mục logs nếu cần
RUN mkdir -p /app/logs

# Build project
RUN mvn clean package -DskipTests

# ---------------------
# Runtime Stage
# ---------------------
FROM openjdk:11-jdk-slim

WORKDIR /app

COPY --from=build /app/target/movie-rating-prediction-1.0-SNAPSHOT.jar app.jar
COPY --from=build /app/data /app/data
COPY --from=build /app/logs /app/logs

ENV JAVA_OPTS="-Xms512m -Xmx1g"
ENV PORT=8080

EXPOSE ${PORT}

ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -Dserver.port=${PORT} -jar app.jar"]
