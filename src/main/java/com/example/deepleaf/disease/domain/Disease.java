package com.example.deepleaf.disease.domain;

import com.example.deepleaf.member.domain.Member;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class Disease {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "disease_id")
    private Long id;

    private String result;

    @Column(name = "disease_info")
    private String diseaseInfo;
    private String watering;
    private String environment;
    private String nutrition;
    private Double confidence;

    @JoinColumn(name = "member_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;
}
