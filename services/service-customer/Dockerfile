FROM amazoncorretto:21-alpine
WORKDIR /app
COPY target/service-customer-1.0-SNAPSHOT.jar app.jar
EXPOSE 8081
CMD ["java", "-jar", "app.jar"]