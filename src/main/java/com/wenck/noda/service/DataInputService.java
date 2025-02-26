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

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Service for handling data input operations
 */
@Service
public class DataInputService {

    private static final Logger LOG = LoggerFactory.getLogger(DataInputService.class);
    private static final String IMAGE_BUCKET_URL = "https://storage.cloud.google.com/noda-images/";
    private final CountryRepository countryRepository;
    private final DirectorRepository directorRepository;
    private final FilmListRepository filmListRepository;
    private final FilmRepository filmRepository;
    private final GoogleCloudStorageService googleCloudStorageService;
    private final GenreRepository genreRepository;
    private final LanguageRepository languageRepository;
    private final StudioRepository studioRepository;

    public DataInputService(CountryRepository countryRepository,
                            DirectorRepository directorRepository,
                            FilmListRepository filmListRepository,
                            FilmRepository filmRepository,
                            GenreRepository genreRepository,
                            GoogleCloudStorageService googleCloudStorageService,
                            LanguageRepository languageRepository,
                            StudioRepository studioRepository) {
        this.countryRepository = countryRepository;
        this.directorRepository = directorRepository;
        this.filmListRepository = filmListRepository;
        this.filmRepository = filmRepository;
        this.genreRepository = genreRepository;
        this.googleCloudStorageService = googleCloudStorageService;
        this.languageRepository = languageRepository;
        this.studioRepository = studioRepository;
    }

    /**
     * Parse a JSON object containing films, may be a list.
     * Parses each attribute and creating/saving/updating entities as needed.
     *
     * @param json String representation of json object containing list of films or a list
     * @param list whether the object being parsed is a lsit ro not
     * @return true if successful
     */
    public boolean parseFromJSON(String json, boolean list) {
        final ObjectMapper objectMapper = new ObjectMapper();
        int newFilmsCount = 0;
        int existingFilmsCount = 0;
        final long startTime = System.currentTimeMillis();
        FilmList listFoundIn = null;

        try {
            final List<Map<String, Object>> data = objectMapper.readValue(json, new TypeReference<>() {});

            if (list) {
                final Map<String, Object> jsonifiedName = data.remove(0);
                // For file creation we set spaces to dashes, revert for a more readable list title
                final String listName = ((String) jsonifiedName.get("List Name")).replace('-', ' ');
                listFoundIn = new FilmList(listName);
                // todo: Duplicate list names are fine, but ok with importing a list that may already exist?
                filmListRepository.save(listFoundIn);
                LOG.info("List successfully saved, listName={}", listName);
            }

            for (Map<String, Object> jsonifiedFilm : data) {
                final ParsedFilmResult parsedFilmResult = parseFilmFromJSON(jsonifiedFilm, listFoundIn);
                if (parsedFilmResult.existing()) {
                    existingFilmsCount++;
                } else {
                    newFilmsCount++;
                }
            }
        } catch (JsonProcessingException ex) {
            LOG.error("Failed to process JSON object", ex);
            return false;
        }

        final long durationMs = System.currentTimeMillis() - startTime;
        LOG.info("Processed JSON object, durationMs={}", durationMs);

        if (newFilmsCount > 0) {
            LOG.info("New films added, newFilmsCounts={}", newFilmsCount);
        }
        if (existingFilmsCount > 0) {
            LOG.info("Duplicate films found, existingFilmsCount={}", existingFilmsCount);
        }

        return true;
    }

    private <T> ParsedFilmResult parseFilmFromJSON(Map<String, Object> jsonifiedFilm,
                                   FilmList listFoundIn) {
        Film newFilm = new Film();
        String base64Poster = null;
        for (String key : jsonifiedFilm.keySet()) {
            switch (key) {
                case "Name" ->
                        newFilm.setName((String) jsonifiedFilm.get(key));
                case "Director" ->
                        newFilm.setDirector(parseDirector((List<Object>) jsonifiedFilm.get(key)));
                case "Year" ->
                        newFilm.setYear((Integer) jsonifiedFilm.get(key));
                case "Synopsis" ->
                        newFilm.setSynopsis((String) jsonifiedFilm.get(key));
                case "Primary Language" ->
                        newFilm.setPrimaryLanguage(parseSingleLanguage((String) jsonifiedFilm.get(key), true));
                case "Spoken Language" ->
                        newFilm.setSpokenLanguage(parseSpokenLanguage((List<Object>) jsonifiedFilm.get(key)));
                case "Country" ->
                        newFilm.setCountry(parseCountry((List<Object>) jsonifiedFilm.get(key)));
                case "Runtime" ->
                        newFilm.setRuntime((Integer) jsonifiedFilm.get(key));
                case "Average Rating" -> {
                    final T avgRating = (T) jsonifiedFilm.get(key);
                    if (avgRating instanceof Integer) {
                        newFilm.setAverageRating(((Integer) avgRating).doubleValue());
                    } else {
                        newFilm.setAverageRating((Double) avgRating);
                    }
                }
                case "Genre" ->
                        newFilm.setGenre(parseGenre((List<Object>) jsonifiedFilm.get(key)));
                case "Studio" ->
                        newFilm.setStudio(parseStudio((List<Object>) jsonifiedFilm.get(key)));
                case "Base64 Poster" ->
                    base64Poster = (String) jsonifiedFilm.get(key);
            }
        }

        // todo: do this earlier (parse name and year first, check if existing, parse after?) - assuming we don't need to update anything besides list.
        final Film existingFilm = filmRepository.findByNameAndYear(newFilm.getName(), newFilm.getYear());

        if (existingFilm == null) {
            // Wait until we have the film name + year to name the file to save the poster to
            // Also make sure we only save this for new films to save cloud space
            if (base64Poster != null) {
                final String fileName = newFilm.getName() + "-" + newFilm.getYear();
                googleCloudStorageService.uploadBase64Image(fileName, base64Poster);
                newFilm.setPosterUrl(IMAGE_BUCKET_URL + fileName);
            }

            if (listFoundIn != null) {
                newFilm.setListsFoundIn(new HashSet<>(Set.of(listFoundIn)));
            } else {
                // Since this is a field that can be added onto, make sure there is a hashset set up
                newFilm.setListsFoundIn(new HashSet<>());
            }
            filmRepository.save(newFilm);
            return new ParsedFilmResult(false, newFilm);
        } else {
            if (listFoundIn != null &&
                    existingFilm.getListsFoundIn().stream().noneMatch(list -> list.getName().equals(listFoundIn.getName()))) {
                existingFilm.getListsFoundIn().add(listFoundIn);
                filmRepository.save(existingFilm);
            }
            return new ParsedFilmResult(true, existingFilm);
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
            if (primaryLanguage) {
                existingLanguage.setPrimaryLanguage(true);
                languageRepository.save(existingLanguage);
            }
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

    private record ParsedFilmResult(boolean existing, Film film) {}

}
