package com.example.deepleaf.disease.domain;

import com.example.deepleaf.member.domain.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
@EntityListeners(AuditingEntityListener.class)
public class Disease {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "disease_id")
    private Long id;

    private String result;

    private Double confidence;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @JoinColumn(name = "member_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    private Disease(String result, Double confidence) {
        this.result = result;
        this.confidence = confidence;
    }

    public static Disease createDisease(String result, Double confidence) {
        return new Disease(result, confidence);
    }

    public void setMember(Member member) {
        this.member = member;
    }
}
