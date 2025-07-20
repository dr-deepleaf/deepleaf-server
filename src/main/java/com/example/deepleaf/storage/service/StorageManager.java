package com.example.deepleaf.storage.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class StorageManager {
    // 이미지 파일명 생성
    public String generateImageFileName(String filename, String filePath, Long userId) {
        String ext = getFileExtension(filename);
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss"));
        String uuid = UUID.randomUUID().toString();
        return "images/" + filePath + "/" + userId + "-profile-" + timestamp + "-" + uuid + ext;
    }
    public String extractKeyFromUrl(String url) {
        try {
            URI uri = new URI(url);
            return uri.getPath().substring(1);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("잘못된 S3 URL 형식입니다: " + url);
        }

    }
    // 확장자를 얻는다
    public String getFileExtension(String filename) {
        try {
            return filename.substring(filename.lastIndexOf("."));
        } catch (StringIndexOutOfBoundsException e) {
            throw e;
        }
    }
}
