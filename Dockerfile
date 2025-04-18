FROM openjdk:11-jdk-slim

WORKDIR /app

COPY target/movie-rating-prediction-*.jar app.jar

RUN mkdir -p /app/logs

# Giảm bộ nhớ heap để tránh vượt quá giới hạn Render
ENV JAVA_OPTS="-Xms64m -Xmx240m -XX:+UseSerialGC -XX:+UseCompressedOops"
ENV PORT=8080

EXPOSE ${PORT}

ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -Dserver.port=${PORT} -jar app.jar"]