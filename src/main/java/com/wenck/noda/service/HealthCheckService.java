package com.wenck.noda.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Service for checking the health of external dependency services
 */
@Service
public class HealthCheckService {
    private static final Logger LOG = LoggerFactory.getLogger(HealthCheckService.class);
    private final RestTemplate restTemplate;

    public HealthCheckService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Quick check to see if the web scraper is up and running
     *
     * @return true if the web scraper is up and running
     */
    public boolean checkHealthWebScraper() {
        UriComponents webScraperUriComponents = UriComponentsBuilder.newInstance()
                .scheme("http")
                .host("localhost")
                .port(5000)
                .pathSegment("health", "check")
                .build();

        try {
            LOG.info("Attempting to ping web scraper. URI={}", webScraperUriComponents.toUriString());

            restTemplate.getForEntity(webScraperUriComponents.toUriString(), String.class);

            LOG.info("Successfully pinged web scraper.");
            return true;
        } catch (RestClientException e) {
            // Catches 400 + 500 errors along with other failures
            LOG.error("Failure to ping web scraper.");
        }

        return false;
    }
}
