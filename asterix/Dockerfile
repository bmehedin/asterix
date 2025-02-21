FROM openjdk:17-jdk-slim

WORKDIR /app

COPY build/libs/*.jar encoder-app.jar

COPY data/ /data

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "encoder-app.jar"]
