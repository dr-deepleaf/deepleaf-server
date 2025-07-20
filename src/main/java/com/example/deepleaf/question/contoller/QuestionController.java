package com.example.deepleaf.question.contoller;


import com.example.deepleaf.auth.annotation.LoginMember;
import com.example.deepleaf.question.dto.request.QuestionCreateRequest;
import com.example.deepleaf.question.dto.response.QuestionCreateResponse;
import com.example.deepleaf.question.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/question")
@RequiredArgsConstructor
public class QuestionController {
    private final QuestionService questionService;

    // 질문 생성
    @PostMapping("/create")
    public ResponseEntity<QuestionCreateResponse> questionCreate(@LoginMember Long memberId, @ModelAttribute QuestionCreateRequest questionCreateRequest) {
        QuestionCreateResponse response = questionService.createQuestion(memberId, questionCreateRequest);
        return ResponseEntity.ok().body(response);
    }

}
