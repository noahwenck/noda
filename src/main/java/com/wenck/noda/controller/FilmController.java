package com.wenck.noda.controller;

import com.wenck.noda.entity.*;
import com.wenck.noda.repository.*;
import com.wenck.noda.service.ControllerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/film")
public class FilmController {

    private static final Logger LOG = LoggerFactory.getLogger(FilmController.class);
    private static final String TYPE = "films";
    private final ControllerService controllerService;
    private final CountryRepository countryRepository;
    private final DirectorRepository directorRepository;
    private final FilmRepository filmRepository;
    private final GenreRepository genreRepository;
    private final LanguageRepository languageRepository;
    private final StudioRepository studioRepository;

    public FilmController(ControllerService controllerService,
                          CountryRepository countryRepository,
                          DirectorRepository directorRepository,
                          FilmRepository filmRepository,
                          GenreRepository genreRepository,
                          LanguageRepository languageRepository,
                          StudioRepository studioRepository) {
        this.controllerService = controllerService;
        this.countryRepository = countryRepository;
        this.directorRepository = directorRepository;
        this.filmRepository = filmRepository;
        this.genreRepository = genreRepository;
        this.languageRepository = languageRepository;
        this.studioRepository = studioRepository;
    }


    @GetMapping
    public String getAllFilms(Model model) {

        controllerService.appendBasicsToModel(TYPE, model);

        model.addAttribute("elements", filmRepository.findAll());

        return "home";
    }

    @GetMapping("/{name}-{year}")
    public String getFilm(@PathVariable String name,
                          @PathVariable String year,
                          Model model) {

        controllerService.appendBasicsToModel(null, model);

        model.addAttribute("film", filmRepository.findByNameAndYear(name, Integer.parseInt(year)));

        return "film";
    }

    @GetMapping("/year/{year}")
    public String getYear(@PathVariable int year,
                          Model model) {

        controllerService.appendBasicsToModel(TYPE, model);

        List<Film> films = filmRepository.findByYear(year);
        controllerService.appendListElementsToModel(films, model);

        return "home";
    }

    @GetMapping("language/primary")
    public String getPrimaryLanguageList(Model model) {

        controllerService.appendBasicsToModel("primaryLanguages", model);

        List<Language> primaryLanguages = languageRepository.findPrimaryLanguages();
        controllerService.appendListElementsToModel(primaryLanguages, model);

        return "home";
    }

    @GetMapping("language/primary/{languageName}")
    public String getFilmsByPrimaryLanguages(@PathVariable String languageName,
                                            Model model) {

        controllerService.appendBasicsToModel(TYPE, model);

        Language language = languageRepository.findByName(languageName);
        List<Film> films = filmRepository.findBySpokenLanguage(language.getName());
        controllerService.appendListElementsToModel(films, model);

        return "home";
    }

    @GetMapping("language/spoken")
    public String getSpokenLanguageList(Model model) {

        controllerService.appendBasicsToModel("spokenLanguages", model);

        List<Language> spokenLanguages = languageRepository.findAll();
        controllerService.appendListElementsToModel(spokenLanguages, model);

        return "home";
    }

    @GetMapping("language/spoken/{languageName}")
    public String getFilmsBySpokenLanguages(@PathVariable String languageName,
                                            Model model) {

        controllerService.appendBasicsToModel(TYPE, model);

        Language language = languageRepository.findByName(languageName);
        List<Film> films = filmRepository.findBySpokenLanguage(language.getName());
        controllerService.appendListElementsToModel(films, model);

        return "home";
    }

    @GetMapping("genre")
    public String getGenreList(Model model) {

        controllerService.appendBasicsToModel("genre", model);

        List<Genre> genres = genreRepository.findAll();
        controllerService.appendListElementsToModel(genres, model);

        return "home";
    }

    @GetMapping("genre/{genreName}")
    public String getFilmsByGenre(@PathVariable String genreName,
                                  Model model) {

        controllerService.appendBasicsToModel(TYPE, model);
        //todo need validation here, and catch NPE
        Genre genre = genreRepository.findByName(genreName);
        List<Film> films = filmRepository.findByGenre(genre.getName());
        controllerService.appendListElementsToModel(films, model);

        return "home";
    }

    @GetMapping("studio")
    public String getStudioList(Model model) {

        controllerService.appendBasicsToModel("studio", model);

        List<Studio> studios = studioRepository.findAll();
        controllerService.appendListElementsToModel(studios, model);

        return "home";
    }

    @GetMapping("studio/{studioName}")
    public String getFilmsByStudio(@PathVariable String studioName,
                                  Model model) {

        controllerService.appendBasicsToModel(TYPE, model);
        //todo need validation here, and catch NPE
        Studio studio = studioRepository.findByName(studioName);
        List<Film> films = filmRepository.findByStudio(studio.getName());
        controllerService.appendListElementsToModel(films, model);

        return "home";
    }

    @GetMapping("country")
    public String getCountryList(Model model) {

        controllerService.appendBasicsToModel("country", model);

        List<Country> countries = countryRepository.findAll();
        controllerService.appendListElementsToModel(countries, model);

        return "home";
    }

    @GetMapping("country/{countryName}")
    public String getFilmsByCountry(@PathVariable String countryName,
                                  Model model) {

        controllerService.appendBasicsToModel(TYPE, model);
        //todo need validation here, and catch NPE
        Country country = countryRepository.findByName(countryName);
        List<Film> films = filmRepository.findByCountry(country.getName());
        controllerService.appendListElementsToModel(films, model);

        return "home";
    }

}
