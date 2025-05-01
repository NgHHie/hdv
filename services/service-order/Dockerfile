FROM amazoncorretto:21-alpine
WORKDIR /app
COPY target/service-order-1.0-SNAPSHOT.jar app.jar
EXPOSE 8082
CMD ["java", "-jar", "app.jar"]