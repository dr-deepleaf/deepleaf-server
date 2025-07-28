package com.example.deepleaf.disease.dto.response;

import com.example.deepleaf.disease.domain.Crop;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QueryResponse {
    private Long diseaseId;
    private String result;
    private String diseaseInfo;
    private Crop crop;
    private LocalDateTime createdAt;
    private Double confidence;
}
