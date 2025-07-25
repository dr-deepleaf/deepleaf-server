package com.example.deepleaf.disease.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
public class DetectionRequest {
    @NotBlank(message = "식물 진단 기능의 필수 값 입니다.")
    MultipartFile file;

    @NotBlank(message = "식물 진단 기능의 필수 값 입니다.")
    String plant;
}
