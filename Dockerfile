FROM openjdk:20
WORKDIR /app
COPY ./build/libs/noda-1.0.0.jar /app
EXPOSE 8080
CMD ["java", "-jar", "noda-1.0.0.jar"]