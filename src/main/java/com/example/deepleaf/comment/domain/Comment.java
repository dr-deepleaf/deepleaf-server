package com.example.deepleaf.comment.domain;

import com.example.deepleaf.member.domain.Member;
import com.example.deepleaf.question.domain.Question;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
public class Comment {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    private String content;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private Adopt adopt = Adopt.FALSE;

    @JoinColumn(name = "member_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @JoinColumn(name = "question_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Question question;
}
