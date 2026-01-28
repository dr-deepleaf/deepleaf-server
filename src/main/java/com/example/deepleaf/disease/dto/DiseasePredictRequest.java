package com.example.deepleaf.disease.dto;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotNull;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DiseasePredictRequest {

    @NotNull(message = "이미지는 필수 입력 값입니다.")
    private MultipartFile image;

    @NotNull(message = "작물 이름은 필수 입력 값입니다.")  
    private String crop;
}
