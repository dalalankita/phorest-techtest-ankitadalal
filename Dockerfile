# --- build stage: compile and package with Maven (JDK 21) ---
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

# Cache dependencies: copy the POM first, resolve, then copy sources.
# Code changes then don't re-download the whole dependency tree.
COPY pom.xml .
RUN mvn -q -B dependency:go-offline

COPY src ./src
RUN mvn -q -B clean package -DskipTests

# --- run stage: slim JRE image with just the built jar ---
FROM eclipse-temurin:21-jre
WORKDIR /app

# The Spring Boot plugin produces one executable (fat) jar in target/.
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]