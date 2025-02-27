# Noda - A Letterboxd Clone

Welcome to Noda, a clone of the popular movie cataloging service [Letterboxd](https://letterboxd.com/),
with data collected from my [letterboxdWebScraper](https://github.com/noahwenck/letterboxdWebScraper).

![](home.jpg)

## Tech Stack
- **Languages**: Java, JavaScript, HTML, CSS, Python (letterboxdWebScraper)
- **Frameworks**: Spring Boot, Bootstrap, Thymeleaf, Flask (letterboxdWebScraper)
- **Misc**: MariaDB, MySQL, Docker, Gradle, Google Cloud

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


## Running

1. Download the service key for the noda default compute service Google Cloud IAM account. (Contact me)
2. Create a `GOOGLE_APPLICATION_CREDENTIALS` env variable pointing to the service key from step 4.
3. In `application.properties` switch the `spring.profiles.active` to `local`, delete or comment out the `prod` profile:
   ```
   spring.profiles.active=local
   # spring.profiles.active=prod
   ```
4. With Docker Desktop running and in the project's root directory, start up your MariaDB pod by running:
   ```
   docker compose up
   ```
5. To start the application, you can either:
   1. Run the NodaApplication run configuration in your IDE.
   2. Run:
        ```
        ./gradlew bootRun
        ```
6. To start up the Shinoda Flask App (necessary to import data), follow the 
[steps found here](https://github.com/noahwenck/letterboxdWebScraper?tab=readme-ov-file#connect-to-noda).
7. Navigate to [localhost:8080](http://localhost:8080).

## Deploying

I intend to continue to commit to this often, so CD is not set-up for this project to prevent spamming deployments.

To manually deploy when necessary:

1. Update version in `build.gradle` if desired. Also update version in `Dockerfile`.
2. Navigate to the project's root directory.
3. Run:
   ```
   ./gradlew build
   ```
4. Build a docker image locally:
   ```
   docker build -t us-central1-docker.pkg.dev/noda-452022/noda-image/noda .
   ```
5. Push the docker image to Google Cloud:
   ```
   docker push us-central1-docker.pkg.dev/noda-452022/noda-image/noda
   ```
   This will create a new version of the `noda` image in the `noda-image` repository in Artifact Registry.
6. Navigate to Artifact Registry and refresh. You will know the image was pushed if the Update Time of the image tagged
`latest` is recent (around when you pushed).
7. In Google Cloud Run
   1. Navigate to noda (Service) -> Edit & Deploy New Revision.
   2. In Containers, select the latest image (the one just pushed) as the Container Image URL.
   3. Hit Deploy.
8. Test successful deployment at: https://noda-683705523587.us-central1.run.app/.