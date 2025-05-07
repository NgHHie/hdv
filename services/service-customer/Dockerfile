FROM amazoncorretto:21-alpine
WORKDIR /app
COPY target/service-customer-1.0-SNAPSHOT.jar app.jar
CMD ["java", "-jar", "app.jar"]