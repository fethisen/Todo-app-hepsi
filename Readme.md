
Todo App

Description

This is a simple Spring Boot application that uses Couchbase as its database. The application is containerized using Docker and includes integration with Docker Compose for managing dependencies.

Instructions

1. Build Your App
   To build the application, make sure you have Docker and Maven installed. Follow these steps:

Using Docker:

docker build -t todo-app .
Without Docker:

If you prefer to build the app locally without Docker:

mvn clean package -DskipTests
2. Run the Test Suite
   To execute the test suite, run the following command:

Using Maven:

mvn test
With Docker (Optional):

If you want to run tests during the Docker build process, remove -DskipTests from the Dockerfile build step.

3. Run Your App with Dependencies
   The application uses Docker Compose to manage dependencies (e.g., Couchbase). To start the app and its dependencies:

Start the application with Docker Compose:
docker-compose up
Access the application:
The application will be available at: http://localhost:8080
Couchbase admin interface: http://localhost:8091
