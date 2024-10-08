package com.wenck.noda.service;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;

@Service
public class ControllerService {

    public void appendBasicsToModel(String type, Model model) {
        // Set of options that a user can search from
        model.addAttribute("options", List.of("Year", "Director", "Primary Language", "Spoken Language"));

        // Tells frontend how to properly render urls
        model.addAttribute("type", type);
    }
}
