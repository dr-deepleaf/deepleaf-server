package com.example.deepleaf.question.domain;

import com.example.deepleaf.comment.domain.Comment;
import com.example.deepleaf.member.domain.Member;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
public class Question {

    @Id  @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_id")
    private Long id;
    private String content;
    private String title;
    private Long point;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
    private String image;
    @Enumerated(EnumType.STRING)
    private Done done = Done.FALSE;
    @JoinColumn(name = "member_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<Comment>();

}
