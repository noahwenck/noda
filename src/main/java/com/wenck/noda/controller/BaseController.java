package com.wenck.noda.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BaseController {


    public BaseController() {
    }

    @GetMapping("/")
    public String listUploadedFiles() {

        return "redirect:/film";
    }

}