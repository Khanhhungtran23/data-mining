# ---------------------
# Build Stage
# ---------------------
FROM maven:3.8.5-openjdk-11 AS build

WORKDIR /app

# Copy toàn bộ project vào container
COPY . .

# Build project (compile + tạo jar)
RUN mvn clean package -DskipTests

# ---------------------
# Runtime Stage
# ---------------------
FROM openjdk:11-jdk-slim

WORKDIR /app

# Copy file JAR đã build từ stage trước
COPY --from=build /app/target/movie-rating-prediction-1.0-SNAPSHOT.jar app.jar

# Copy thư mục data vào container
COPY data/ /app/data/

# Tạo thư mục logs
RUN mkdir -p /app/logs

# Cấu hình JVM
ENV JAVA_OPTS="-Xms512m -Xmx1g"
ENV PORT=8080

EXPOSE ${PORT}

# Chạy ứng dụng
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -Dserver.port=${PORT} -jar app.jar"]
