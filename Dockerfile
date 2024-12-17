# Use an official OpenJDK runtime as a parent image
FROM openjdk:21-jdk

# Set the working directory in the container
WORKDIR /app

# Copy the project’s jar file into the container at /app
COPY target/com.toel-0.0.1-SNAPSHOT.war /app/demo.war

COPY src/main/resources/firebase/firebase-service-account.json /app/src/main/resources/firebase/firebase-service-account.json

# Make port 8000 available to the world outside this container
EXPOSE 8080

# Run the jar file
ENTRYPOINT ["java", "-jar", "/app/demo.war"]
