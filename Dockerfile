#FROM maven:3.9.7-eclipse-temurin-21 AS build
#COPY . .
#RUN mvn clean package -DskipTests
#
#FROM eclipse-temurin:21-jdk-alpine
#COPY --from=build /target/*.jar dearlybeloved.jar
#ENTRYPOINT ["java","-jar","dearlybeloved.jar"]

# First stage: Build the application using Maven
FROM maven:3.9.7-eclipse-temurin-21 AS build

# Copy the entire project into the container
COPY . .

# Run Maven to clean and package the application, skipping tests
RUN mvn clean package -DskipTests

# Second stage: Create the runtime image
FROM eclipse-temurin:21-jdk-alpine

# Copy the JAR file from the build stage to the runtime image
COPY --from=build target/*.jar dearlybeloved.jar

# Run the application
ENTRYPOINT ["java","-jar","dearlybeloved.jar"]
