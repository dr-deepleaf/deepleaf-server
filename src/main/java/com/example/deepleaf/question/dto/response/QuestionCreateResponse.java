package com.example.deepleaf.question.dto.response;

import com.example.deepleaf.question.domain.Question;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuestionCreateResponse {

    //question
    private Long questionId;
    private String title;
    private String content;
    private String image_url;
    private Long point;

    private QuestionCreateResponse(Question question){
        this.questionId = question.getId();
        this.title = question.getTitle();
        this.content = question.getContent();
        this.image_url = question.getImage();
        this.point = question.getPoint();
    }

    public static QuestionCreateResponse create(Question question){
        return new QuestionCreateResponse(question);
    }
}
