FROM openjdk:17-jdk-slim

WORKDIR /app

RUN apt-get update && apt-get install -y \
    libfreetype6 \
    fontconfig \
    && rm -rf /var/lib/apt/lists/*

COPY build/libs/*.jar encoder-app.jar

COPY data/ /data

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "encoder-app.jar"]
