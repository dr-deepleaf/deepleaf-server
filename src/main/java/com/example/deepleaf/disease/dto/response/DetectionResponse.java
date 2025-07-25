package com.example.deepleaf.disease.dto.response;

import com.example.deepleaf.disease.domain.Crop;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class DetectionResponse {

    private Long memberId;
    private Long diseaseId;

    private String result;
    private String diseaseInfo;
    private Crop crop;
    private LocalDateTime createdAt;


    private Double confidence;
}

