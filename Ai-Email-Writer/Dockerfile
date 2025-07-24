# ----------- Build Stage -------------
FROM maven:3.8-eclipse-temurin-17 AS build

# Set working directory in container
WORKDIR /app

# Copy everything to container
COPY . .

# Go to the correct directory where pom.xml is (update if needed)
WORKDIR /app/Ai-Email-Writer

# Build the Spring Boot application
RUN mvn clean package -DskipTests

# ----------- Run Stage -------------
FROM eclipse-temurin:17-jdk

# Set working directory for runtime container
WORKDIR /app

# Copy jar from the build stage
COPY --from=build /app/Ai-Email-Writer/target/Ai-Email-Writer-0.0.1-SNAPSHOT.jar app.jar

# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]
