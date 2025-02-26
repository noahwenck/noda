package com.wenck.noda.service;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

/**
 * Test class for {@link HealthCheckService}
 */
@ExtendWith(MockitoExtension.class)
class HealthCheckServiceTest {
    @InjectMocks
    private HealthCheckService healthCheckService;
    @Mock
    private RestTemplate restTemplate;

    @Disabled // todo: fix after hosting complete
    @Test
    void checkHealthWebScraperSuccess() {
        final ResponseEntity<String> response = new ResponseEntity<>(HttpStatusCode.valueOf(200));

        when(restTemplate.getForEntity(anyString(), eq(String.class))).thenReturn(response);

        final boolean check = healthCheckService.checkHealthWebScraper();

        assertTrue(check);
    }

    @Disabled // todo: fix after hosting complete
    @Test
    void checkHealthWebScraperFailure() {
        final ResponseEntity<String> response = new ResponseEntity<>(HttpStatusCode.valueOf(400));

        when(restTemplate.getForEntity(anyString(), eq(String.class))).thenReturn(response);

        final boolean check = healthCheckService.checkHealthWebScraper();

        assertFalse(check);
    }
}
