FROM amazoncorretto:21-alpine
WORKDIR /app
COPY target/service-product-1.0-SNAPSHOT.jar app.jar
EXPOSE 8083
CMD ["java", "-jar", "app.jar"]