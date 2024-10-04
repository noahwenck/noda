package com.wenck.noda.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class BaseController {

    @Autowired
    public BaseController() {
    }

    @GetMapping("/")
    public String listUploadedFiles() {

        return "redirect:/film";
    }

}