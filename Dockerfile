# ---------------------
# Build Stage
# ---------------------
FROM maven:3.8.5-openjdk-11 AS build

WORKDIR /app

COPY modelEvaluation/pom.xml .
COPY modelEvaluation/src ./src

# Build project
RUN mvn clean package

# ---------------------
# Runtime Stage
# ---------------------
FROM openjdk:11-jdk-slim

WORKDIR /app

# Copy JAR file từ build stage
COPY --from=build /app/target/movie-rating-prediction-1.0-SNAPSHOT.jar app.jar

# Copy resources chứa model PMML nếu cần
COPY --from=build /app/src/main/resources /app/resources

# Tạo thư mục logs
RUN mkdir -p /app/logs

# Thiết lập port mặc định là 8080 nếu không được cung cấp
ENV PORT=8080

# Expose port
EXPOSE ${PORT}

# Định nghĩa volume cho logs
VOLUME ["/app/logs"]

# Chạy ứng dụng
ENTRYPOINT ["sh", "-c", "java -Dserver.port=${PORT} -Dspring.profiles.active=prod -jar app.jar"]