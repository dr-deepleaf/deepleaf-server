package com.example.deepleaf.comment.domain;

import com.example.deepleaf.comment.dto.request.CommentRequest;
import com.example.deepleaf.member.domain.Member;
import com.example.deepleaf.question.domain.Question;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Comment {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    private String content;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @JoinColumn(name = "member_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @JoinColumn(name = "question_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Question question;

    private Comment (CommentRequest commentRequest){
        this.content = commentRequest.getContent();
        this.createdAt = LocalDateTime.now();
    }

    public static Comment creatWith(CommentRequest commentRequest) {
        return new Comment(commentRequest);
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public void setMember(Member member) {
        this.member = member;
    }
}
