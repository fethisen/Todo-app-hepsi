# Base image
FROM maven:3.8.5-openjdk-17 AS build

COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:17.0.1-jdk-slim

COPY --from=build target/todo-app-0.0.1.jar todo-app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","todo-app.jar"]
