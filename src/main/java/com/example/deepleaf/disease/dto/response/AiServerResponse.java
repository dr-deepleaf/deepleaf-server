package com.example.deepleaf.disease.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AiServerResponse {
    private String result;
    private Double confidence;
    private String diseaseInfo;
}
