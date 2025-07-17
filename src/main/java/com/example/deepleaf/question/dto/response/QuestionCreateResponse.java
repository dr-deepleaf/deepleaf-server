package com.example.deepleaf.question.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuestionCreateResponse {

    //member
    private Long memberId;
    private String email;

    //question
    private Long questionId;
    private String title;
    private String content;
    private String image_url;
    private Long point;
}
