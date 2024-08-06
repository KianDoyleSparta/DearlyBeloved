FROM maven:3.9.7-eclipse-temurin-21 AS build
COPY . .
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jdk
COPY --from=build /target/*.jar dearlybeloved.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","dearlybeloved.jar"]

