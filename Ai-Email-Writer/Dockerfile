# ----------- Build Stage -------------
FROM maven:3.8.7-openjdk-17 AS build

# Set the working directory inside the container
WORKDIR /app

# Copy everything from the app's root directory
COPY . .

# Go into the subdirectory that contains the pom.xml
WORKDIR /app/Ai-Email-Writer

# Build the Spring Boot project (skip tests to speed it up)
RUN mvn clean package -DskipTests

# ----------- Run Stage -------------
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the built JAR file from the build stage
COPY --from=build /app/Ai-Email-Writer/target/Ai-Email-Writer-0.0.1-SNAPSHOT.jar app.jar

# Run the app
ENTRYPOINT ["java", "-jar", "app.jar"]
