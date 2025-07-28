package com.example.deepleaf.disease.dto.request;

import com.example.deepleaf.disease.domain.Crop;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QueryRequest {
    private Crop crop;
}
