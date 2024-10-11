package com.wenck.noda.controller;

import com.wenck.noda.entity.Director;
import com.wenck.noda.entity.film.Film;
import com.wenck.noda.repository.DirectorRepository;
import com.wenck.noda.repository.FilmRepository;
import com.wenck.noda.service.ControllerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/director")
public class DirectorController {

    private static final String DIRECTORS = "directors";

    private ControllerService controllerService;
    private DirectorRepository directorRepository;
    private FilmRepository filmRepository;

    public DirectorController(ControllerService controllerService,
                              DirectorRepository directorRepository,
                              FilmRepository filmRepository) {
        this.controllerService = controllerService;
        this.directorRepository = directorRepository;
        this.filmRepository = filmRepository;
    }

    @GetMapping
    public String getDirectorList(Model model) {
        controllerService.appendBasicsToModel(DIRECTORS, model);

        List<Director> directors = directorRepository.findAll();
        controllerService.appendListElementsToModel(directors, model);

        return "home";
    }

    @GetMapping("/{directorName}")
    public String getDirector(@PathVariable String directorName,
                              Model model) {

        controllerService.appendBasicsToModel("films", model);

        Director director = directorRepository.findByName(directorName);
        List<Film> films = filmRepository.findByDirector(director.getId());
        controllerService.appendListElementsToModel(films, model);

        return "home";
    }
}
