# Noda

Noda is a just a demonstration of my ability with Spring Boot, databases, etc.
Since I can't show any of my professional work (all private on GitHub),
hopefully this can show that I have _some_ experience with software development.

Noda serves as a clone of [Letterboxd](https://letterboxd.com/),
information gathered using [my own web scraper](https://github.com/noahwenck/letterboxdWebScraper).
The name comes from Ozu's longtime screenwriting collaborator, Kōgo Noda.

## Building

Build via Gradle:
```
./gradlew build
```

## Running

Docker Desktop is required for setup.

Noda uses MariaDB as its database. To start up your MariaDB pod, run the following command for the project's directory:

```
docker compose up
```

Noda can be run via the NodaApplication run configuration in your IDE, or by running `NodaApplication.main()`.

To Gather data for Noda, you will also need to have a part of my web scraper running,
[steps found here](https://github.com/noahwenck/letterboxdWebScraper?tab=readme-ov-file#connect-to-noda).