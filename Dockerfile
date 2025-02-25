# Use an official OpenJDK runtime as a parent image
FROM openjdk:20-jre-slim

# Set the working directory
WORKDIR /app

# Copy the application JAR file to the container
COPY target/noda-*.jar /app/noda.jar

# Expose the port the application runs on
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "/app/noda.jar"]