package com.example.deepleaf.disease.dto.response;

import com.example.deepleaf.disease.domain.Crop;
import com.example.deepleaf.disease.domain.Disease;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetectionResponse {

    private Long memberId;
    private Long diseaseId;

    private String result;
    private String diseaseInfo;
    private Crop crop;
    private LocalDateTime createdAt;


    private Double confidence;

    public DetectionResponse (Disease disease, Long memberId){
        this.memberId = memberId;
        this.diseaseId = disease.getId();
        this.result = disease.getResult();
        this.diseaseInfo = disease.getDiseaseInfo();
        this.crop = disease.getCrop();
        this.createdAt = disease.getCreatedAt();
    }
    public static DetectionResponse createWith(Disease disease, Long memberId) {
       return new DetectionResponse(disease,memberId);
    }
}

