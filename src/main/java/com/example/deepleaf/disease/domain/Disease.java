package com.example.deepleaf.disease.domain;

import com.example.deepleaf.member.domain.Member;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
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
}
