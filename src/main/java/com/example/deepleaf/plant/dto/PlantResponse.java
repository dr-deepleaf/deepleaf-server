package com.example.deepleaf.plant.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlantResponse {

    private Long id;

    
    private String imageUrl;

    
    private String commonName;

    private String genus;

    private String family;
}
