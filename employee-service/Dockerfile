# Base Image
FROM eclipse-temurin:17-jdk

# Working Directory
WORKDIR /app

# Copy the JAR into the container
COPY target/employee-service-0.0.1-SNAPSHOT.jar app.jar

# Expose Spring Boot Port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]