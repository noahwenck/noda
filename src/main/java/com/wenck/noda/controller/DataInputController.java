package com.wenck.noda.controller;

import com.wenck.noda.service.DataInputService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for handling data input operations
 */
@Controller
@RequestMapping("input")
public class DataInputController {

    private static final Logger LOG = LoggerFactory.getLogger(DataInputController.class);
    private final DataInputService dataInputService;

    public DataInputController(DataInputService dataInputService) {
        this.dataInputService = dataInputService;
    }

    /**
     * Access point for web scraper to send films in JSON
     *
     * @param films list JSON films
     */
    @PostMapping("/films")
    public String parseFilmsFromJSON(@RequestBody String films) {

        dataInputService.parseFromJSON(films, false);

        return "redirect:/";
    }

    @PostMapping("/list")
    public String parseListFromJSON(@RequestBody String list) {

        dataInputService.parseFromJSON(list, true);

        return "redirect:/";
    }
}
