package com.wenck.noda.service;

import com.wenck.noda.NodaProperties;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

/**
 * Test class for {@link HealthCheckService}
 */
@ExtendWith(MockitoExtension.class)
class HealthCheckServiceTest {
    @InjectMocks
    private HealthCheckService healthCheckService;
    @Mock
    private GoogleCloudStorageService googleCloudStorageService;
    @Mock
    private NodaProperties nodaProperties;
    @Mock
    private RestTemplate restTemplate;

    @Test
    void checkShinodaHealthSuccess() {
        final ResponseEntity<String> response = new ResponseEntity<>(HttpStatusCode.valueOf(200));
        final String shinodaUrl = "http://shinoda";

        when(nodaProperties.getShinodaUrl()).thenReturn(shinodaUrl);
        when(restTemplate.getForEntity(shinodaUrl + "/health/check", String.class)).thenReturn(response);

        final boolean healthy = healthCheckService.checkShinodaHealth();

        assertTrue(healthy);
    }

    @Test
    void checkShinodaHealthFailure() {
        final ResponseEntity<String> response = new ResponseEntity<>(HttpStatusCode.valueOf(400));
        final String shinodaUrl = "http://shinoda";

        when(nodaProperties.getShinodaUrl()).thenReturn(shinodaUrl);
        when(restTemplate.getForEntity(shinodaUrl + "/health/check", String.class)).thenReturn(response);

        final boolean healthy = healthCheckService.checkShinodaHealth();

        assertFalse(healthy);
    }

    @Test
    void checkGoogleCloudImageBucketHealthSuccess() {
        when(googleCloudStorageService.checkImageBucketExists()).thenReturn(true);

        final boolean healthy = healthCheckService.checkGoogleCloudImageBucketHealth();

        assertTrue(healthy);
    }

    @Test
    void checkGoogleCloudImageBucketHealthFailure() {
        when(googleCloudStorageService.checkImageBucketExists()).thenReturn(false);

        final boolean healthy = healthCheckService.checkGoogleCloudImageBucketHealth();

        assertFalse(healthy);
    }
}
