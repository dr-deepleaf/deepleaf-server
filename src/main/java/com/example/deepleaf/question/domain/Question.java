package com.example.deepleaf.question.domain;

import com.example.deepleaf.comment.domain.Comment;
import com.example.deepleaf.member.domain.Member;
import com.example.deepleaf.question.dto.request.QuestionCreateRequest;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
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

    private Question(QuestionCreateRequest questionCreateRequest, String imageUrl) {
        this.content = questionCreateRequest.getContent();
        this.title = questionCreateRequest.getTitle();
        this.point = questionCreateRequest.getPoint();
        this.createdAt = LocalDateTime.now();
        this.image = imageUrl;
    }

    public static Question createQuestion(QuestionCreateRequest questionCreateRequest, String imageUrl) {
        return new Question(questionCreateRequest,imageUrl);
    }

    public void setMember(Member member){
        this.member = member;
    }


}
