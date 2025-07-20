package com.example.deepleaf.storage.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

public class StorageManagerTest {
    private StorageManager storageManager;

    @BeforeEach
    void setUp(){
        storageManager = new StorageManager();
    }

    @Test
    void generateImageFileName_filename_filepath_userId로_이미지_파일_이름을_생성한다() {
        String filename = "avatar.jpg";
        String filePath = "user_uploads";
        Long userId = 123L;

        String result = storageManager.generateImageFileName(filename, filePath, userId);

        assertNotNull(result);
        assertTrue(result.startsWith("images/" + filePath + "/" + userId + "-profile-"));

        String extension = filename.substring(filename.lastIndexOf("."));
        assertTrue(result.endsWith(extension));

        // 정규식으로 시간 형식과 UUID 확인
        String pattern = "images/" + filePath + "/" + userId + "-profile-\\d{8}T\\d{6}-[a-f0-9\\-]{36}" + extension;
        assertTrue(Pattern.matches(pattern, result), "Generated filename does not match expected format");
    }


    @Test
    void extractKeyFromUrl_S3객체_키를_추출할때_선행_슬레시를_제거한다() {
        String url = "https://s3.amazonaws.com/bucket/images/user_uploads/123-profile-20230719T153045-uuid.jpg";
        String expectedKey = "bucket/images/user_uploads/123-profile-20230719T153045-uuid.jpg";

        String key = storageManager.extractKeyFromUrl(url);

        assertEquals(expectedKey, key);
    }

    @Test
    void extractKeyFromUrl_유효하지_않은_url에_대해서_예외를_반환한다() {
        String invalidUrl = "ht!tp://invalid-url";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            storageManager.extractKeyFromUrl(invalidUrl);
        });

        assertTrue(exception.getMessage().contains("잘못된 S3 URL 형식입니다"));
    }

    @Test
    void getFileExtension_파일의_확장자를_리턴한다() {
        String filename = "image.png";
        String ext = storageManager.getFileExtension(filename);

        assertEquals(".png", ext);
    }

    @Test
    void getFileExtension_dot이_없는_확장자에_대해서_예외가_발생한다() {
        String invalidFilename = "imagefilewithoutextension";

        assertThrows(StringIndexOutOfBoundsException.class, () -> {
            storageManager.getFileExtension(invalidFilename);
        });
    }
}
