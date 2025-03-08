package com.wenck.noda.service;

import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.wenck.noda.GoogleCloudStorageProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.util.Base64;

/**
 * Service for handling interactions with Google cloud storage
 */
@Service
public class GoogleCloudStorageService {
    private static final Logger LOG = LoggerFactory.getLogger(GoogleCloudStorageService.class);
    private static final String CLOUD_STORAGE_URL = "https://storage.cloud.google.com/";
    private final GoogleCloudStorageProperties googleCloudStorageProperties;
    private Storage storage;

    public GoogleCloudStorageService(GoogleCloudStorageProperties googleCloudStorageProperties) {
        this.googleCloudStorageProperties = googleCloudStorageProperties;
        this.storage = StorageOptions
                .newBuilder()
                .setProjectId(googleCloudStorageProperties.getProjectId())
                .build()
                .getService();
    }

    /**
     * Take the base64 encoded string of the movie poster, and save it to our Google cloud bucket
     *
     * @param fileName film name + year to as the filename
     * @param base64Image base64 encoded string of the image
     */
    public String uploadBase64Image(String fileName, String base64Image) {
        final byte[] imageBytes = Base64.getDecoder().decode(base64Image);
        final Bucket bucket = storage.get(googleCloudStorageProperties.getBucketName());
        bucket.create(fileName, new ByteArrayInputStream(imageBytes), "image/png");
        return CLOUD_STORAGE_URL + googleCloudStorageProperties.getBucketName() + "/" + fileName;
        // todo: messes with titles with odd characters, ref. What did the Lady Forget?
    }

    /**
     * Checks to see if the Image Bucket exists
     *
     * @return true if the image bucket exists
     */
    public boolean checkImageBucketExists() {
        LOG.info("Checking health of bucket, bucketName={}", googleCloudStorageProperties.getBucketName());
        final Bucket bucket = storage.get(googleCloudStorageProperties.getBucketName());
        return bucket != null;
    }
}
