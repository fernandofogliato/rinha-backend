FROM openjdk:17-jdk-alpine

WORKDIR /app

# Copy the packaged JAR file into the container at /app
COPY build/libs/rinhabackend-0.0.1-SNAPSHOT.jar /app/rinha-backend.jar

# Specify the command to run your application
CMD  ["java", "-jar", "rinha-backend.jar"]