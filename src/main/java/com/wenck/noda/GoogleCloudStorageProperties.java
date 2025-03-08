package com.wenck.noda;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class GoogleCloudStorageProperties {

    @Value("${spring.cloud.gcp.project-id}")
    private String projectId;
    @Value("${gcp.storage.bucket}")
    private String bucketName;

    public String getProjectId() {
        return projectId;
    }

    public String getBucketName() {
        return bucketName;
    }
}
