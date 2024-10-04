package com.wenck.noda.service;

import com.wenck.noda.entity.Director;
import com.wenck.noda.entity.Film;
import com.wenck.noda.entity.Language;
import com.wenck.noda.repository.DirectorRepository;
import com.wenck.noda.repository.FilmRepository;
import com.wenck.noda.repository.LanguageRepository;
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
    private final DirectorRepository directorRepository;
    private final FilmRepository filmRepository;
    private final LanguageRepository languageRepository;

    public DataInputService(DirectorRepository directorRepository,
                            FilmRepository filmRepository,
                            LanguageRepository languageRepository) {
        this.directorRepository = directorRepository;
        this.filmRepository = filmRepository;
        this.languageRepository = languageRepository;
    }

    public boolean handleJSONParse(String JsonObject) {

        ObjectMapper objectMapper = new ObjectMapper();
        int existingFilmsCount = 0;
        final long startTime = System.currentTimeMillis();
        try {
            List<Map<String, Object>> data = objectMapper.readValue(JsonObject, new TypeReference<>(){});
            for (Map<String, Object> map : data) {
                Film film = new Film();
                Set<String> directorNames = new HashSet<>();
                for (String key : map.keySet()) {
                    switch (key) {
                        case "Name" -> film.setName((String) map.get(key));
                        case "Director" -> directorNames = new HashSet<>((Collection) map.get(key));
                        case "Year" -> film.setYear((Integer) map.get(key));
                        case "Primary Language" -> film.setPrimaryLanguage((String) map.get(key));
                        case "Spoken Language" -> film.setSpokenLanguage(parseSpokenLanguage(map, key));
                        case "Country" -> film.setCountry(new HashSet<>((Collection) map.get(key)));
                        case "Runtime" -> film.setRuntime((Integer) map.get(key));
                        case "Average Rating" -> film.setAverageRating((Double) map.get(key));
                        case "Genre" -> {
                            film.setGenre(new HashSet<>((Collection) map.get(key)));
                            //todo figure out why this enum aint working
//                            System.out.println(map.get(key));
//                            Set<Genre> genres = new HashSet<>();
//                            for (Object genre : (Collection<String>) map.get(key)) {
//                                try {
//                                    genres.add(Genre.valueOf(genre.toString()));
//                                } catch (IllegalArgumentException ex) {
//                                    LOG.warn("Unknown genre found, genre={}", genre);
//                                }
//                            }
//                            film.setGenre(genres);
                        }
                        case "Studio" -> film.setStudio(new HashSet<>((Collection) map.get(key)));
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

        if (existingFilmsCount > 0) {
            LOG.info("Duplicate films found, existingFilmsCount={}", existingFilmsCount);
        }

        return true;
    }

    private Language parsePrimaryLanguage(String languageName) {
        Language existingLanguage = languageRepository.findByName(languageName);
        if (existingLanguage == null) {
            Language newLanguage = new Language(languageName);
            languageRepository.save(newLanguage);
            return newLanguage;
        } else {
            return existingLanguage;
        }
    }

    // todo: change params they ugly
    private Set<Language> parseSpokenLanguage(Map<String, Object> map, String key) {
        Set<Language> spokenLanguage = new HashSet<>();
        for (Object language : (Collection) map.get(key)) {
            Language existingLanguage = languageRepository.findByName((String) language);
            if (existingLanguage == null) {
                Language newLanguage = new Language((String) language);
                languageRepository.save(newLanguage);
                spokenLanguage.add(newLanguage);
            } else {
                spokenLanguage.add(existingLanguage);
            }
        }
        return spokenLanguage;
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
