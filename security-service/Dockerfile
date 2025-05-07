FROM amazoncorretto:21-alpine
WORKDIR /app
COPY target/service-security-1.0-SNAPSHOT.jar app.jar
EXPOSE 8088
CMD ["java", "-jar", "app.jar"]