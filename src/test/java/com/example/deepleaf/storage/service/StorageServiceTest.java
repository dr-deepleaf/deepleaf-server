package com.example.deepleaf.storage.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StorageServiceTest {

    @Mock
    private AmazonS3 amazonS3;

    @Mock
    private StorageManager storageManager;

    @Mock
    private MultipartFile multipartFile;

    @InjectMocks
    private StorageService storageService;

    private final String bucketName = "test-bucket";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // manually inject @Value field
        storageService = new StorageService(amazonS3, storageManager);
        // simulate @Value injection
        try {
            java.lang.reflect.Field bucketField = StorageService.class.getDeclaredField("bucket");
            bucketField.setAccessible(true);
            bucketField.set(storageService, bucketName);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testUploadFile_업로드한_후에_url을_리턴한다() throws IOException {
        String originalFilename = "test.jpg";
        String filePath = "profile";
        Long userId = 123L;
        String generatedFileName = "images/profile/123-profile-20230719T153045-uuid.jpg";
        byte[] fileContent = "fake-image-data".getBytes();

        when(multipartFile.getOriginalFilename()).thenReturn(originalFilename);
        when(multipartFile.getSize()).thenReturn((long) fileContent.length);
        when(multipartFile.getContentType()).thenReturn("image/jpeg");
        when(multipartFile.getInputStream()).thenReturn(new ByteArrayInputStream(fileContent));

        when(storageManager.generateImageFileName(originalFilename, filePath, userId))
                .thenReturn(generatedFileName);

        URL fakeUrl = new URL("https://s3.amazonaws.com/test-bucket/" + generatedFileName);
        when(amazonS3.getUrl(bucketName, generatedFileName)).thenReturn(fakeUrl);

        String resultUrl = storageService.uploadFile(multipartFile, filePath, userId);

        assertEquals(fakeUrl.toString(), resultUrl);
        verify(amazonS3, times(1)).putObject(argThat((PutObjectRequest request) ->
                request.getBucketName().equals(bucketName) &&
                        request.getKey().equals(generatedFileName)
        ));
    }

    @Test
    void testUploadFile_IOException_발생_테스트() throws IOException {
        when(multipartFile.getOriginalFilename()).thenReturn("file.jpg");
        when(multipartFile.getSize()).thenReturn(100L);
        when(multipartFile.getContentType()).thenReturn("image/jpeg");
        when(multipartFile.getInputStream()).thenThrow(new IOException("mocked IO error"));
        when(storageManager.generateImageFileName(anyString(), anyString(), anyLong()))
                .thenReturn("images/path/1-profile-x.jpg");

        assertThrows(RuntimeException.class, () ->
                storageService.uploadFile(multipartFile, "path", 1L));
    }

    @Test
    void testDeleteFile_AmazonS3Delete을_호출한다() {
        String url = "https://s3.amazonaws.com/test-bucket/images/profile/123.jpg";
        String objectKey = "images/profile/123.jpg";

        when(storageManager.extractKeyFromUrl(url)).thenReturn(objectKey);

        storageService.deleteFile(url);

        verify(amazonS3, times(1)).deleteObject(bucketName, objectKey);
    }

    @Test
    void testGetS3ImageUrl_url을_리턴한다() throws Exception {
        String filename = "images/path/abc.jpg";
        URL url = new URL("https://s3.amazonaws.com/test-bucket/" + filename);

        when(amazonS3.getUrl(bucketName, filename)).thenReturn(url);

        String result = storageService.getS3ImageUrl(filename);

        assertEquals(url.toString(), result);
    }
}
