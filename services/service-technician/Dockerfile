FROM amazoncorretto:21-alpine
WORKDIR /app
COPY target/service-technician-1.0-SNAPSHOT.jar app.jar
EXPOSE 8087
CMD ["java", "-jar", "app.jar"]