package com.wenck.noda.service;

import com.wenck.noda.NodaProperties;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

/**
 * Test classs for {@link ControllerService}
 */
@ExtendWith(MockitoExtension.class)
class ControllerServiceTest {
    @InjectMocks
    ControllerService controllerService;
    @Mock
    HealthCheckService healthCheckService;
    @Mock
    NodaProperties nodaProperties;

    // Values used in Unit Tests
    final List<String> searchOptions = List.of("Year",
                                        "Director",
                                        "Primary Language",
                                        "Spoken Language",
                                        "Studio",
                                        "Genre",
                                        "Country",
                                        "Film",
                                        "List");
    final String type = "films";

    @Test
    void testAppendBasicsToModelNoErrors() {
        Model model = new ConcurrentModel();
        final String noda = "nodaUrl";
        final String shinoda = "shinodaUrl";

        when(nodaProperties.getNodaUrl()).thenReturn(noda);
        when(nodaProperties.getShinodaUrl()).thenReturn(shinoda);
        when(healthCheckService.checkShinodaHealth()).thenReturn(true);
        when(healthCheckService.checkGoogleCloudImageBucketHealth()).thenReturn(true);

        controllerService.appendBasicsToModel(type, model);

        assertEquals(searchOptions, model.getAttribute("options"));
        assertEquals(type, model.getAttribute("type"));
        assertEquals(noda, model.getAttribute(noda));
        assertEquals(shinoda, model.getAttribute(shinoda));
        assertNull(model.getAttribute("healthError"));
        assertNull(model.getAttribute("cloudError"));
    }

    @Test
    void testAppendBasicsToModelBothErrors() {
        Model model = new ConcurrentModel();
        final String noda = "nodaUrl";
        final String shinoda = "shinodaUrl";

        when(nodaProperties.getNodaUrl()).thenReturn(noda);
        when(nodaProperties.getShinodaUrl()).thenReturn(shinoda);
        when(healthCheckService.checkShinodaHealth()).thenReturn(false);
        when(healthCheckService.checkGoogleCloudImageBucketHealth()).thenReturn(false);

        controllerService.appendBasicsToModel(type, model);

        assertEquals(searchOptions, model.getAttribute("options"));
        assertEquals(type, model.getAttribute("type"));
        assertEquals(noda, model.getAttribute(noda));
        assertEquals(shinoda, model.getAttribute(shinoda));
        assertNotNull(model.getAttribute("healthError"));
        if (model.containsAttribute("healthError")) {
        assertTrue((Boolean) model.getAttribute("healthError"));
        }
        assertNotNull(model.getAttribute("cloudError"));
        assertTrue((Boolean) model.getAttribute("cloudError"));
    }

    @Test
    void testAppendListElementsToModel() {
        Model model = new ConcurrentModel();
        final List<String> elements = List.of("List", "of", "Elements");

        controllerService.appendListElementsToModel(elements, model);

        assertEquals(elements, model.getAttribute("elements"));
    }

    @Test
    void testAppendListElementsToModelNull() {
        Model model = new ConcurrentModel();
        final List<String> elements = null;

        controllerService.appendListElementsToModel(elements, model);

        assertNotNull(model.getAttribute("error"));
        assertTrue((Boolean) model.getAttribute("error"));
    }

    @Test
    void testAppendListElementsToModelEmpty() {
        Model model = new ConcurrentModel();
        final List<String> elements = List.of();

        controllerService.appendListElementsToModel(elements, model);

        assertNotNull(model.getAttribute("error"));
        assertTrue((Boolean) model.getAttribute("error"));
    }
}
