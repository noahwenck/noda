package com.wenck.noda.controller;

import com.wenck.noda.entity.Director;
import com.wenck.noda.entity.FilmList;
import com.wenck.noda.entity.film.*;
import com.wenck.noda.repository.*;
import com.wenck.noda.service.ControllerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.function.Supplier;

/**
 * Controller for accessing entities and passing them along to the fronted for display
 */
@Controller
@RequestMapping("/film")
public class FilmDisplayController {

    private static final Logger LOG = LoggerFactory.getLogger(FilmDisplayController.class);
    private static final String FILMS_TYPE = "films";
    private final ControllerService controllerService;
    private final CountryRepository countryRepository;
    private final DirectorRepository directorRepository;
    private final FilmListRepository filmListRepository;
    private final FilmRepository filmRepository;
    private final GenreRepository genreRepository;
    private final LanguageRepository languageRepository;
    private final StudioRepository studioRepository;

    public FilmDisplayController(ControllerService controllerService,
                                 CountryRepository countryRepository,
                                 DirectorRepository directorRepository,
                                 FilmListRepository filmListRepository,
                                 FilmRepository filmRepository,
                                 GenreRepository genreRepository,
                                 LanguageRepository languageRepository,
                                 StudioRepository studioRepository) {
        this.controllerService = controllerService;
        this.countryRepository = countryRepository;
        this.directorRepository = directorRepository;
        this.filmListRepository = filmListRepository;
        this.filmRepository = filmRepository;
        this.genreRepository = genreRepository;
        this.languageRepository = languageRepository;
        this.studioRepository = studioRepository;
    }


    @GetMapping
    public String getAllFilms(Model model) {
        return retrieveSearchOptionList("films", filmRepository::findAll, model);
    }

    @GetMapping("/{filmName}")
    public String getFilmByName(@PathVariable String filmName,
                                Model model) {
        try {
            List<Film> films = filmRepository.findByName(filmName);
            if (films.size() == 1) {
                model.addAttribute("film", films.get(0));
                return "film";
            } else {
                controllerService.appendBasicsToModel(FILMS_TYPE, model);
                controllerService.appendListElementsToModel(films, model); // this also accounts for empty list
                return "home";
            }
        } catch (NullPointerException ex) {
            LOG.error("Failure to find films, filmName={}", filmName, ex);
            return "redirect:/";
        }
    }

    @GetMapping("/{filmName}/{year}")
    public String getFilm(@PathVariable String filmName,
                          @PathVariable String year,
                          Model model) {
        controllerService.appendBasicsToModel(null, model);
        model.addAttribute("film",
                filmRepository.findByNameAndYear(filmName, Integer.parseInt(year)));

        return "film";
    }

    @GetMapping("/year/{year}")
    public String getYear(@PathVariable int year,
                          Model model) {
        controllerService.appendBasicsToModel(FILMS_TYPE, model);
        final List<Film> films = filmRepository.findByYear(year);
        controllerService.appendListElementsToModel(films, model);
        return "home";
    }

    @GetMapping("director")
    public String getDirectorList(Model model) {
        return retrieveSearchOptionList("directors", directorRepository::findAll, model);
    }

    @GetMapping("/{directorName}")
    public String getDirector(@PathVariable String directorName,
                              Model model) {
        controllerService.appendBasicsToModel(FILMS_TYPE, model);
        try {
            final Director director = directorRepository.findByName(directorName);
            final List<Film> films = filmRepository.findByDirector(director.getId());
            controllerService.appendListElementsToModel(films, model);
            return "home";
        } catch (NullPointerException ex) {
            LOG.error("Failure to find directorName={}", directorName, ex);
            return "redirect:/";
        }
    }

    @GetMapping("language/primary")
    public String getPrimaryLanguageList(Model model) {
        controllerService.appendBasicsToModel("primaryLanguages", model);
        final List<Language> primaryLanguages = languageRepository.findPrimaryLanguages();
        controllerService.appendListElementsToModel(primaryLanguages, model);
        return "home";
    }

    @GetMapping("language/primary/{languageName}")
    public String getFilmsByPrimaryLanguages(@PathVariable String languageName,
                                            Model model) {
        controllerService.appendBasicsToModel(FILMS_TYPE, model);
        try {
            final Language language = languageRepository.findByName(languageName);
            final List<Film> films = filmRepository.findByPrimaryLanguage(language.getName());
            controllerService.appendListElementsToModel(films, model);
            return "home";
        } catch (NullPointerException ex) {
            LOG.error("Failure to find languageName={}", languageName, ex);
            return "redirect:/";
        }
    }

    @GetMapping("language/spoken")
    public String getSpokenLanguageList(Model model) {
        return retrieveSearchOptionList("spokenLanguages", languageRepository::findAll, model);
    }

    @GetMapping("language/spoken/{languageName}")
    public String getFilmsBySpokenLanguages(@PathVariable String languageName,
                                            Model model) {
        controllerService.appendBasicsToModel(FILMS_TYPE, model);
        try {
            final Language language = languageRepository.findByName(languageName);
            final List<Film> films = filmRepository.findBySpokenLanguage(language.getName());
            controllerService.appendListElementsToModel(films, model);
            return "home";
        } catch (NullPointerException ex) {
            LOG.error("Failure to find languageName={}", languageName, ex);
            return "redirect:/";
        }
    }

    @GetMapping("genre")
    public String getGenreList(Model model) {
        return retrieveSearchOptionList("genre", genreRepository::findAll, model);
    }

    @GetMapping("genre/{genreName}")
    public String getFilmsByGenre(@PathVariable String genreName,
                                  Model model) {
        controllerService.appendBasicsToModel(FILMS_TYPE, model);
        try {
            final Genre genre = genreRepository.findByName(genreName);
            final List<Film> films = filmRepository.findByGenre(genre.getName());
            controllerService.appendListElementsToModel(films, model);
            return "home";
        } catch (NullPointerException ex) {
            LOG.error("Failure to find genreName={}", genreName, ex);
            return "redirect:/";
        }
    }

    @GetMapping("studio")
    public String getStudioList(Model model) {
        return retrieveSearchOptionList("studio", studioRepository::findAll, model);
    }

    @GetMapping("studio/{studioName}")
    public String getFilmsByStudio(@PathVariable String studioName,
                                  Model model) {
        controllerService.appendBasicsToModel(FILMS_TYPE, model);
        try {
            final Studio studio = studioRepository.findByName(studioName);
            final List<Film> films = filmRepository.findByStudio(studio.getName());
            controllerService.appendListElementsToModel(films, model);
            return "home";
        } catch (NullPointerException ex) {
            LOG.error("Failure to find studioName={}", studioName, ex);
            return "redirect:/";
        }
    }

    @GetMapping("country")
    public String getCountryList(Model model) {
        return retrieveSearchOptionList("country", countryRepository::findAll, model);
    }

    @GetMapping("country/{countryName}")
    public String getFilmsByCountry(@PathVariable String countryName,
                                  Model model) {
        controllerService.appendBasicsToModel(FILMS_TYPE, model);
        try {
            final Country country = countryRepository.findByName(countryName);
            final List<Film> films = filmRepository.findByCountry(country.getName());
            controllerService.appendListElementsToModel(films, model);
            return "home";
        } catch (NullPointerException ex) {
            LOG.error("Failure to find countryName={}", countryName, ex);
            return "redirect:/";
        }
    }

    @GetMapping("list")
    public String getFilmListList(Model model) {
        return retrieveSearchOptionList("list", filmListRepository::findAll, model);
    }

    @GetMapping("list/{listName}")
    public String getFilmsByFilmList(@PathVariable String listName,
                                     Model model) {
        controllerService.appendBasicsToModel(FILMS_TYPE, model);
        model.addAttribute("isList", true);
        model.addAttribute("listName", listName);
        try {
            final List<FilmList> list = filmListRepository.findByName(listName);
            if (list.size() == 1) {
                final List<Film> films = filmRepository.findByListsFoundIn(listName);
                controllerService.appendListElementsToModel(films, model);
            } else {
                // todo: duplicate list names SHOULD be ok, but realistically how can I deal with this?
            }
            return "home";
        } catch (NullPointerException ex) {
            LOG.error("Failure to find listName={}", listName, ex);
            return "redirect:/";
        }
    }

    private <T> String retrieveSearchOptionList(String type, Supplier<List<T>> findAll, Model model) {
        controllerService.appendBasicsToModel(type, model);
        final List<T> elements = findAll.get();
        controllerService.appendListElementsToModel(elements, model);
        return "home";
    }
}
