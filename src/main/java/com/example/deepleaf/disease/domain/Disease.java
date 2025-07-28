package com.example.deepleaf.disease.domain;

import com.example.deepleaf.disease.dto.response.AiServerResponse;
import com.example.deepleaf.member.domain.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
public class Disease {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "disease_id")
    private Long id;
    private String result;
    private String diseaseInfo;
    @Enumerated(EnumType.STRING)
    private Crop crop;
    private LocalDateTime createdAt;
    private Double confidence;
    @JoinColumn(name = "member_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;
    private Disease (AiServerResponse aiServerResponse, String plant){
        this.result = aiServerResponse.getResult();
        this.diseaseInfo = aiServerResponse.getDiseaseInfo();
        this.confidence = aiServerResponse.getConfidence();
        this.crop = Crop.valueOf(plant.toUpperCase());
    }
    public static Disease createWith(AiServerResponse aiServerResponse, String plant) {
        return new Disease(aiServerResponse, plant);
    }

    public void setMember(Member member) {
        this.member = member;
    }
}
