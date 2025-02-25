package com.wenck.noda.service;

import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.util.Base64;

/**
 * Service for handling interactions with Google cloud storage
 */
@Service
public class GoogleCloudStorageService {

    private final Storage storage;
    private final String bucketName;

    public GoogleCloudStorageService(@Value("${spring.cloud.gcp.project-id}") String projectId,
                                     @Value("${gcp.storage.bucket}") String bucketName) {
        this.storage = StorageOptions.newBuilder().setProjectId(projectId).build().getService();
        this.bucketName = bucketName;
    }

    /**
     * Take the base64 encoded string of the movie poster, and save it to our Google cloud bucket
     * @param fileName film name + year to as the filename
     * @param base64Image base64 encoded string of the image
     */
    public void uploadBase64Image(String fileName, String base64Image) {
        byte[] imageBytes = Base64.getDecoder().decode(base64Image);
        Bucket bucket = storage.get(bucketName);
        bucket.create(fileName, new ByteArrayInputStream(imageBytes), "image/png");
    }
}
