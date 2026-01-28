package com.example.deepleaf.disease.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DiseasePredictResponse {

    private String result;

    private Double confidence;

    private String imageUrl;

    private LocalDateTime createdAt;
}
