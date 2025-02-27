package com.wenck.noda;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Component for noda-specific properties (not including gcp properties)
 */
@Component
public class NodaProperties {

    @Value("${shinoda.url}")
    private String shinodaUrl;
    @Value("${noda.url}")
    private String nodaUrl;

    public String getNodaUrl() {
        return nodaUrl;
    }

    public String getShinodaUrl() {
        return shinodaUrl;
    }
}
