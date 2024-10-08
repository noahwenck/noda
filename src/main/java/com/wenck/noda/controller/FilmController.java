package com.wenck.noda.controller;

import com.wenck.noda.entity.Film;
import com.wenck.noda.entity.Language;
import com.wenck.noda.repository.DirectorRepository;
import com.wenck.noda.repository.FilmRepository;
import com.wenck.noda.repository.LanguageRepository;
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
    private final DirectorRepository directorRepository;
    private final FilmRepository filmRepository;
    private final LanguageRepository languageRepository;

    public FilmController(ControllerService controllerService,
                          DirectorRepository directorRepository,
                          FilmRepository filmRepository,
                          LanguageRepository languageRepository) {
        this.controllerService = controllerService;
        this.directorRepository = directorRepository;
        this.filmRepository = filmRepository;
        this.languageRepository = languageRepository;
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
        if (films == null || films.isEmpty()) {
            model.addAttribute("error", true);
        } else {
            model.addAttribute("elements", films);
        }

        return "home";
    }

    @GetMapping("language/primary")
    public String getPrimaryLanguageList(Model model) {

        controllerService.appendBasicsToModel("primaryLanguages", model);

        List<Language> primaryLanguages = languageRepository.findPrimaryLanguages();
        if (primaryLanguages.isEmpty()) {
            model.addAttribute("error", true);
        } else {
            model.addAttribute("elements", primaryLanguages);
        }

        return "home";
    }

    @GetMapping("language/primary/{languageName}")
    public String getFilmsByPrimaryLanguages(@PathVariable String languageName,
                                            Model model) {

        controllerService.appendBasicsToModel(TYPE, model);

        Language language = languageRepository.findByName(languageName);
        if (languageName == null) {
            model.addAttribute("error", true);
        } else {
            model.addAttribute("elements",
                    filmRepository.findyBySpokenLanguage(language.getName()));
        }

        return "home";
    }

    @GetMapping("language/spoken")
    public String getSpokenLanguageList(Model model) {

        controllerService.appendBasicsToModel("spokenLanguages", model);

        List<Language> spokenLanguages = languageRepository.findAll();
        if (spokenLanguages.isEmpty()) {
            model.addAttribute("error", true);
        } else {
            model.addAttribute("elements", spokenLanguages);
        }

        return "home";
    }

    @GetMapping("language/spoken/{languageName}")
    public String getFilmsBySpokenLanguages(@PathVariable String languageName,
                                            Model model) {

        controllerService.appendBasicsToModel(TYPE, model);

        Language language = languageRepository.findByName(languageName);
        if (languageName == null) {
            model.addAttribute("error", true);
        } else {
            model.addAttribute("elements",
                    filmRepository.findyBySpokenLanguage(language.getName()));
        }

        return "home";
    }

}
