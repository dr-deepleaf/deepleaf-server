package com.example.deepleaf.plant.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class Plant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "plant_id")
    private Long id;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "common_name")
    private String commonName;

    private String genus;

    private String family;
}
