package com.example.deepleaf.question.dto.response;

import com.example.deepleaf.question.domain.Question;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionResponse {

    private Long memberId;
    private Long questionId;
    private String title;
    private String content;
    private String image_url;
    private Long point;

    private QuestionResponse(Question question){
        this.questionId = question.getId();
        this.title = question.getTitle();
        this.content = question.getContent();
        this.image_url = question.getImage();
        this.point = question.getPoint();
    }

    public static QuestionResponse create(Question question){
        return new QuestionResponse(question);
    }
}
