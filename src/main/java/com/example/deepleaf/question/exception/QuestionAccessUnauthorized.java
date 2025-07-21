package com.example.deepleaf.question.exception;

import com.example.deepleaf.global.exception.GlobalException;
import org.springframework.http.HttpStatus;

public class QuestionAccessUnauthorized extends GlobalException {

    public QuestionAccessUnauthorized() {
        super(HttpStatus.FORBIDDEN, "FORBIDDEN ACCESS", "해당 질문에 대한 수정 권한이 없습니다");
    }
}
