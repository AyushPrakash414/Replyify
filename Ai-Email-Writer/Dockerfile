# Use a multi-stage build to reduce image size

# --- Build Stage ---
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# Copy pom and source files
COPY . .

# Build the application and skip tests
RUN mvn clean package -DskipTests

# --- Run Stage ---
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copy the built jar from the build stage
COPY --from=build /app/target/Ai-Email-Writer-0.0.1-SNAPSHOT.jar app.jar

# Expose the default Spring Boot port
EXPOSE 8080

# Start the app
ENTRYPOINT ["java", "-jar", "app.jar"]
