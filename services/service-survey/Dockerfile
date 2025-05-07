FROM amazoncorretto:21-alpine
WORKDIR /app
COPY target/service-survey-1.0-SNAPSHOT.jar app.jar
CMD ["java", "-jar", "app.jar"]