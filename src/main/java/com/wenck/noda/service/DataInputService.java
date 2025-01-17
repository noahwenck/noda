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

    public boolean handleJSONParse(String JsonObject) {

        ObjectMapper objectMapper = new ObjectMapper();
        int newFilmsCount = 0;
        int existingFilmsCount = 0;
        final long startTime = System.currentTimeMillis();
        try {
            List<Map<String, Object>> data = objectMapper.readValue(JsonObject, new TypeReference<>() {});
            for (Map<String, Object> map : data) {
                Film film = new Film();
                Set<String> directorNames = new HashSet<>();
                for (String key : map.keySet()) {
                    switch (key) {
                        case "Name" ->
                                film.setName((String) map.get(key));
                        case "Director" ->
                                directorNames = new HashSet<>((Collection) map.get(key));
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

                Set<Director> directorSet = new HashSet<>();
                for (String name : directorNames) {
                    Set<Film> set = new HashSet<>();
                    set.add(film);

                    Director existingDirector = directorRepository.findByName(name);
                    if (existingDirector == null) {
                        Director director = new Director(name, set);
                        director.setName(name);
                        directorRepository.save(director);
                        directorSet.add(director);
                    } else {
                        directorSet.add(existingDirector);
                    }
                }
                // Want to keep this connected to director code, but as early as possible
                film.setDirector(directorSet);

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

    // todo somehow combine these methods, too much repetition for such basic functionality

    private Set<Language> parseSpokenLanguage(List<Object> json) {
        Set<Language> spokenLanguage = new HashSet<>();
        for (Object language : json) {
            spokenLanguage.add(parseSingleLanguage((String) language, false));
        }
        return spokenLanguage;
    }

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

    private Set<Country> parseCountry(List<Object> json) {
        Set<Country> countrySet = new HashSet<>();
        for (Object country : json) {
            Country existingCountry = countryRepository.findByName((String) country);
            if (existingCountry == null) {
                Country newCountry = new Country((String) country);
                countryRepository.save(newCountry);
                countrySet.add(newCountry);
            } else {
                countrySet.add(existingCountry);
            }
        }
        return countrySet;
    }

    private Set<Genre> parseGenre(List<Object> json) {
        Set<Genre> genreSet = new HashSet<>();
        if (json != null) { // todo do this for rest
            for (Object genre : json) {
                Genre existingGenre = genreRepository.findByName((String) genre);
                if (existingGenre == null) {
                    Genre newGenre = new Genre((String) genre);
                    genreRepository.save(newGenre);
                    genreSet.add(newGenre);
                } else {
                    genreSet.add(existingGenre);
                }
            }
        } else {
            genreSet = new HashSet<>();
        }

        return genreSet;
    }

    private Set<Studio> parseStudio(List<Object> json) {
        Set<Studio> studioSet = new HashSet<>();
        for (Object studio : json) {
            Studio existingStudio = studioRepository.findByName((String) studio);
            if (existingStudio == null) {
                Studio newStudio = new Studio((String) studio);
                studioRepository.save(newStudio);
                studioSet.add(newStudio);
            } else {
                studioSet.add(existingStudio);
            }
        }
        return studioSet;
    }

    //todo: investigate if this can be done like this
    private void JsonToFilmObject(ObjectMapper objectMapper, String films) throws JsonProcessingException {
        List<Film> filmlist = objectMapper.readValue(films, new TypeReference<>(){});
        for (Film film : filmlist) {
            System.out.println(film.getName());
        }
    }

    public void purge() {

        // burn it all
        filmRepository.deleteAll();
        directorRepository.deleteAll();
        languageRepository.deleteAll();
    }

}
