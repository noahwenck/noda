package com.wenck.noda.controller;

import com.wenck.noda.service.ControllerService;
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
    private final ControllerService controllerService;
    private final DataInputService dataInputService;

    public DataInputController(ControllerService controllerService,
                               DataInputService dataInputService) {
        this.controllerService = controllerService;
        this.dataInputService = dataInputService;
    }

    /**
     * Access point for web scraper to send films in JSON
     *
     * @param films list JSON films
     */
    @PostMapping("/films")
    public String parseFilmsFromJSON(@RequestParam String films) {

        dataInputService.parseFilmsFromJSON(films);

        return "redirect:/";
    }

    /**
     * Mapping for me to easily nuke db (tables will be empty not totally nuked)
     *
     * todo: remove before i forget
     */
    @GetMapping("/purge")
    public String purge() {

        dataInputService.purge();

        return "redirect:/";
    }
}
