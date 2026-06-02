# Dockerfile for the Entreprenly Platform
#
# Multi-stage build: compiles the Spring Boot application with the Maven Wrapper
# and runs it on a lightweight Temurin 26 JRE. The 'prod' profile is active at
# runtime and the service listens on the port defined by the PORT variable.

# Step 1: Build the application using the Maven Wrapper on a Temurin 26 JDK
FROM eclipse-temurin:26-jdk AS build
WORKDIR /app
COPY .mvn .mvn
COPY mvnw .
COPY pom.xml .
RUN chmod +x mvnw
RUN ./mvnw dependency:go-offline
COPY src ./src
RUN ./mvnw clean package -DskipTests

# Step 2: Create the runtime image
FROM eclipse-temurin:26-jre AS runtime
ENV SPRING_PROFILES_ACTIVE=prod
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Step 3: Configure and run the application
EXPOSE 8092
ENTRYPOINT ["java", "-jar", "app.jar"]

# Environment variables required by the 'prod' profile (define them in the hosting provider):
# - DATABASE_NAME      Name of the MySQL database to connect to.
# - DATABASE_USER      Username for the database connection.
# - DATABASE_PASSWORD  Password for the database connection.
# - DATABASE_URL       Database host name or address.
# - DATABASE_PORT      Port of the database to connect to.
# - JWT_SECRET         Secret used to sign JWT tokens.
# - PORT               Port the application listens on (default 8092).
# - SPRING_PROFILES_ACTIVE  Active Spring profile (must be 'prod' for runtime configuration).
