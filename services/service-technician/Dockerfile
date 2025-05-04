FROM amazoncorretto:21-alpine
WORKDIR /app
COPY target/service-technician-1.0-SNAPSHOT.jar app.jar
EXPOSE 8086
CMD ["java", "-jar", "app.jar"]