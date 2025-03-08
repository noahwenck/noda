package com.wenck.noda.service;

import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.wenck.noda.GoogleCloudStorageProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link GoogleCloudStorageService}
 */
@ExtendWith(MockitoExtension.class)
public class GoogleCloudStorageServiceTest {
    @InjectMocks
    GoogleCloudStorageService googleCloudStorageService;
    @Mock
    GoogleCloudStorageProperties googleCloudStorageProperties;
    @Mock
    Storage storage;

    @BeforeEach
    void setStorage() {
        googleCloudStorageService.setStorage(storage);
    }

    @Test
    void testUploadBase64Image() {
        final String fileName = "fileName";
        final String base64Image = "base64Image";
        final String expectedPosterUrl = "https://storage.cloud.google.com/bucketName/fileName";
        final Bucket bucket = mock(Bucket.class);

        when(googleCloudStorageProperties.getBucketName()).thenReturn("bucketName");
        when(storage.get("bucketName")).thenReturn(bucket);
        when(bucket.create(eq(fileName), any(ByteArrayInputStream.class), eq("image/png"))).thenReturn(null);

        final String posterUrl = googleCloudStorageService.uploadBase64Image(fileName, base64Image);

        assertEquals(expectedPosterUrl, posterUrl);
    }

    @Test
    void testCheckImageBucketExistsTrue() {
        final Bucket bucket = mock(Bucket.class);

        when(googleCloudStorageProperties.getBucketName()).thenReturn("bucketName");
        when(storage.get("bucketName")).thenReturn(bucket);

        final boolean exists = googleCloudStorageService.checkImageBucketExists();

        assertTrue(exists);
    }

    @Test
    void testCheckImageBucketExistsFalse() {
        when(googleCloudStorageProperties.getBucketName()).thenReturn("bucketName");
        when(storage.get("bucketName")).thenReturn(null);

        final boolean exists = googleCloudStorageService.checkImageBucketExists();

        assertFalse(exists);
    }
}
