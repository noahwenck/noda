package com.wenck.noda.service;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;

@Service
public class ControllerService<T> {

    /**
     * Appends utility information to the model for use in organizing the frontend properly
     *
     * @param type The type of elements that will be returned via this endpoint
     */
    public void appendBasicsToModel(String type, Model model) {
        // Set of options that a user can search from
        model.addAttribute("options",
                List.of("Year",
                "Director",
                "Primary Language",
                "Spoken Language",
                "Studio",
                "Genre",
                "Country"));

        // Tells frontend how to properly render urls
        model.addAttribute("type", type);
    }

    /**
     * Checks the received list from the database to see if it is null or empty, if not attach list.
     *
     * @param elements Generic list of elements to be attached to model
     */
    public void appendListElementsToModel(List<T> elements, Model model) {
        if (elements == null || elements.isEmpty()) {
            model.addAttribute("error", true);
        } else {
            model.addAttribute("elements", elements);
        }
    }
}
