package com.wenck.noda.service;

import com.wenck.noda.entity.*;
import com.wenck.noda.entity.film.*;
import com.wenck.noda.repository.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Service for handling data input operations
 */
@Service
public class DataInputService {

    private static final Logger LOG = LoggerFactory.getLogger(DataInputService.class);
    private final CountryRepository countryRepository;
    private final DirectorRepository directorRepository;
    private final FilmRepository filmRepository;
    private final GenreRepository genreRepository;
    private final LanguageRepository languageRepository;
    private final StudioRepository studioRepository;

    public DataInputService(CountryRepository countryRepository,
                            DirectorRepository directorRepository,
                            FilmRepository filmRepository,
                            GenreRepository genreRepository,
                            LanguageRepository languageRepository,
                            StudioRepository studioRepository) {
        this.countryRepository = countryRepository;
        this.directorRepository = directorRepository;
        this.filmRepository = filmRepository;
        this.genreRepository = genreRepository;
        this.languageRepository = languageRepository;
        this.studioRepository = studioRepository;
    }

    /**
     * Parse a JSON object containing films.
     * Parses each attribute and creating/saving/updating entities as needed.
     *
     * @param filmAsJSON JSON films
     */
    public void parseFilmsFromJSON(String filmAsJSON) {

        ObjectMapper objectMapper = new ObjectMapper();
        int newFilmsCount = 0;
        int existingFilmsCount = 0;
        final long startTime = System.currentTimeMillis();
        try {
            List<Map<String, Object>> data = objectMapper.readValue(filmAsJSON, new TypeReference<>() {});
            for (Map<String, Object> map : data) {
                Film film = new Film();
                for (String key : map.keySet()) {
                    switch (key) {
                        case "Name" ->
                                film.setName((String) map.get(key));
                        case "Director" ->
                                film.setDirector(parseDirector((List<Object>) map.get(key)));
                        case "Year" ->
                                film.setYear((Integer) map.get(key));
                        case "Primary Language" ->
                                film.setPrimaryLanguage(parseSingleLanguage((String) map.get(key), true));
                        case "Spoken Language" ->
                                film.setSpokenLanguage(parseSpokenLanguage((List<Object>) map.get(key)));
                        case "Country" ->
                                film.setCountry(parseCountry((List<Object>) map.get(key)));
                        case "Runtime" ->
                                film.setRuntime((Integer) map.get(key));
                        case "Average Rating" ->
                                film.setAverageRating((Double) map.get(key));
                        case "Genre" ->
                                film.setGenre(parseGenre((List<Object>) map.get(key)));
                        case "Studio" ->
                                film.setStudio(parseStudio((List<Object>) map.get(key)));
                        case "Base64 Poster" ->
                                film.setBase64Poster((String) map.get(key));
                    }
                }

                Film existingFilm = filmRepository.findByNameAndYear(film.getName(), film.getYear());
                if (existingFilm == null) {
                    newFilmsCount++;
                    filmRepository.save(film);
                } else {
                    existingFilmsCount++;
                }
            }
        } catch (JsonProcessingException ex) {
            LOG.error("Failed to process JSON object", ex);
        }

        final long durationMs = System.currentTimeMillis() - startTime;
        LOG.info("Processed JSON object, durationMs={}", durationMs);

        if (newFilmsCount > 0) {
            LOG.info("New films added, newFilmsCounts={}", newFilmsCount);
        }
        if (existingFilmsCount > 0) {
            LOG.info("Duplicate films found, existingFilmsCount={}", existingFilmsCount);
        }
    }

    /**
     * General method for parsing a list of entities from a JSON array,
     * creating new entities if they do not exist in the database.
     *
     * @param json string list of entity names
     * @param findByName function from the entities repository to find an entity by name
     * @param createNewEntity entity constructor
     * @param saveEntity function from the entities repository to save an entity
     * @return set of entities to be added to the film
     */
    private <T> Set<T> parseEntities(List<Object> json, Function<String, T> findByName, Function<String, T> createNewEntity, Consumer<T> saveEntity) {
        Set<T> entitySet = new HashSet<>();
        if (json != null) {
            for (Object entityName : json) {
                T existingEntity = findByName.apply((String) entityName);
                if (existingEntity == null) {
                    T newEntity = createNewEntity.apply((String) entityName);
                    saveEntity.accept(newEntity);
                    entitySet.add(newEntity);
                } else {
                    entitySet.add(existingEntity);
                }
            }
        } else {
            entitySet = new HashSet<>();
        }
        return entitySet;
    }

    private Set<Director> parseDirector(List<Object> json) {
        return parseEntities(json, directorRepository::findByName, Director::new, directorRepository::save);
    }

    private Set<Country> parseCountry(List<Object> json) {
        return parseEntities(json, countryRepository::findByName, Country::new, countryRepository::save);
    }

    private Set<Studio> parseStudio(List<Object> json) {
        return parseEntities(json, studioRepository::findByName, Studio::new, studioRepository::save);
    }

    private Set<Genre> parseGenre(List<Object> json) {
        return parseEntities(json, genreRepository::findByName, Genre::new, genreRepository::save);
    }

    /**
     * Parse a list of languages from a JSON string
     *
     * @param json list of language names
     * @return list of languages
     */
    private Set<Language> parseSpokenLanguage(List<Object> json) {
        Set<Language> spokenLanguage = new HashSet<>();
        for (Object language : json) {
            spokenLanguage.add(parseSingleLanguage((String) language, false));
        }
        return spokenLanguage;
    }

    /**
     * Parse a single language from a JSON string
     *
     * @param languageName name of the language
     * @param primaryLanguage if the language is used as a primary language
     * @return language entity
     */
    private Language parseSingleLanguage(String languageName, boolean primaryLanguage) {
        Language existingLanguage = languageRepository.findByName(languageName);
        if (existingLanguage == null) {
            Language newLanguage = new Language(languageName, primaryLanguage);
            languageRepository.save(newLanguage);
            return newLanguage;
        } else {
            return existingLanguage;
        }
    }

    /**
     * Utility method to purge all data from the database.
     */
    public void purge() {
        countryRepository.deleteAll();
        directorRepository.deleteAll();
        filmRepository.deleteAll();
        genreRepository.deleteAll();
        languageRepository.deleteAll();
        studioRepository.deleteAll();
    }

}
