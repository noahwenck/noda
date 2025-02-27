package com.wenck.noda.service;

import com.wenck.noda.NodaProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Service for checking the health of external services
 */
@Service
public class HealthCheckService {
    private static final Logger LOG = LoggerFactory.getLogger(HealthCheckService.class);

    private final NodaProperties nodaProperties;
    private final RestTemplate restTemplate;

    public HealthCheckService(NodaProperties nodaProperties,
                              RestTemplate restTemplate) {
        this.nodaProperties = nodaProperties;
        this.restTemplate = restTemplate;
    }

    /**
     * Quick check to see if the web scraper (shinoda app) is up and running
     *
     * @return true if the shinoda app is up and running
     */
    public boolean checkHealthWebScraper() {
        final UriComponents shinodaUriComponents = UriComponentsBuilder.fromHttpUrl(nodaProperties.getShinodaUrl())
                .pathSegment("health", "check")
                .build();

        try {
            LOG.info("Attempting to ping shinoda app. URI={}", shinodaUriComponents.toUriString());

            final ResponseEntity<String> response =
                    restTemplate.getForEntity(shinodaUriComponents.toUriString(), String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                LOG.info("Successfully pinged shinoda app .");
                return true;
            }
        } catch (RestClientException e) {
            LOG.error("Exception while pinging shinoda app", e);
        }

        LOG.error("Failure to ping shinoda app.");
        return false;
    }
}
