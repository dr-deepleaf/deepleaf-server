package com.example.deepleaf.member.domain;

import com.example.deepleaf.comment.domain.Comment;
import com.example.deepleaf.disease.domain.Disease;
import com.example.deepleaf.question.domain.Question;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Member {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Builder.Default // 빌더 패턴 활용 시 디폴트 값 설정
    private Role role = Role.USER;

    @Builder.Default
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Question> questions = new ArrayList<Question>();

    @Builder.Default
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<Comment>();

    @Builder.Default
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Disease> diseases = new ArrayList<Disease>();

    public void addQuestion(Question question){
        this.questions.add(question);
        question.setMember(this);
    }

    public void addComment(Comment comment) {
        this.comments.add(comment);
        comment.setMember(this);
    }

    public void addDisease(Disease disease) {
        this.diseases.add(disease);
        disease.setMember(this);
    }
}
