# Dockerfile for the Entreprenly Platform
#
# Multi-stage build: compiles the Spring Boot application with the Maven Wrapper
# and runs it on a lightweight Temurin 26 JRE. The 'prod' profile is active at
# runtime and the service listens on the port defined by the PORT variable.

# Step 1: Build the application using a Maven image that bundles Maven and a Temurin 26 JDK.
# Using the official Maven image avoids downloading the Maven distribution at build time
# (the previous Maven Wrapper bootstrap pulled it from repo.maven.apache.org on every build,
# which intermittently returned HTTP 403 and broke the deploy).
FROM maven:3.9.16-eclipse-temurin-26 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn clean package -DskipTests -B

# Step 2: Create the runtime image
FROM eclipse-temurin:26-jre AS runtime
ENV SPRING_PROFILES_ACTIVE=prod
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Step 3: Configure and run the application
EXPOSE 8092
ENTRYPOINT ["java", "-jar", "app.jar"]

# Environment variables required by the 'prod' profile (define them in the hosting provider):
# - CLOUD_SQL_CONNECTION_NAME  Cloud SQL instance connection name (project:region:instance).
# - DATABASE_NAME      Name of the MySQL database to connect to (default daop-entreprenly).
# - DATABASE_USER      Username for the database connection.
# - DATABASE_PASSWORD  Password for the database connection.
# - JWT_SECRET         Secret used to sign JWT tokens.
# - PORT               Port the application listens on (default 8092).
# - SPRING_PROFILES_ACTIVE  Active Spring profile (must be 'prod' for runtime configuration).
#
# In production the app connects to Cloud SQL through the Java socket factory, so the
# host/port are not used; the instance is identified by CLOUD_SQL_CONNECTION_NAME.
