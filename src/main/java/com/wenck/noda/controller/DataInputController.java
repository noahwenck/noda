package com.wenck.noda.controller;

import com.wenck.noda.service.ControllerService;
import com.wenck.noda.service.DataInputService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/asJSON")
    public String handleJSONParse(@RequestParam String films) {

        boolean parsed = dataInputService.handleJSONParse(films);

        return "redirect:/";
    }

    @GetMapping("/purge")
    public String purge() {

        dataInputService.purge();

        return "redirect:/";
    }
}
