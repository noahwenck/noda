# Noda - A Letterboxd Clone

Welcome to Noda, a clone of the popular movie cataloging service [Letterboxd](https://letterboxd.com/),
with data collected from my [letterboxdWebScraper](https://github.com/noahwenck/letterboxdWebScraper).

![](home.jpg)

## Tech Stack
- **Languages**: Java, JavaScript, Python (letterboxdWebScraper)
- **Frameworks**: Spring Boot, Bootstrap, Flask (letterboxdWebScraper)
- **Misc**: MariaDB, Docker, Gradle, Google Cloud

## Installation

1. Clone the repo
   ```sh
   git clone git@github.com:noahwenck/noda.git
   ```
2. Ensure the following are installed:
   - [Java](https://www.oracle.com/java/technologies/downloads/?er=221886) (should work just as well with Java 20 or newer)
   - [Docker Desktop](https://www.docker.com/)
   - [Gradle](https://gradle.org/)
3. Run a Gradle build:
   ```sh
   ./gradlew build
   ```
4. Download the service key for the noda-service-account Google Cloud IAM account. (Contact me)
5. Create a `GOOGLE_APPLICATION_CREDENTIALS` env variable pointing to the service key from step 4. 
6. In `application.properties` switch the `spring.profiles.active` to `local`, delete or comment out the `prod` profile:
   ```
   spring.profiles.active=local
   # spring.profiles.active=prod
   ```

## Running

1. With Docker Desktop running, start up your MariaDB pod by running the following command from the project's directory:
   ```
   docker compose up
   ```
2. To start the application, you can either
   1. Run the NodaApplication run configuration in your IDE
   2. Run:
        ```
        ./gradlew bootRun
        ```
3. To start up the Shinoda Flask App (necessary to collect data), follow the 
[steps found here](https://github.com/noahwenck/letterboxdWebScraper?tab=readme-ov-file#connect-to-noda).
4. Navigate to [localhost:8080](http://localhost:8080).