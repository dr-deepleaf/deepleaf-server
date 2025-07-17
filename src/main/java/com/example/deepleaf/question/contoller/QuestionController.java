package com.example.deepleaf.question.contoller;

import com.example.deepleaf.member.common.util.JwtUtil;
import com.example.deepleaf.question.domain.Question;
import com.example.deepleaf.question.dto.request.QuestionCreateRequest;
import com.example.deepleaf.question.dto.response.QuestionCreateResponse;
import com.example.deepleaf.question.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class QuestionController {
    private final JwtUtil jwtUtil;
    private final QuestionService questionService;

    // 질문 생성
    @PostMapping("/create")
    public ResponseEntity<QuestionCreateResponse> questionCreate(@ModelAttribute QuestionCreateRequest questionCreateRequest) {
        //Long memberId = jwtUtil.getLoginMember().getId();
        QuestionCreateResponse response = questionService.createQuestion(1L, questionCreateRequest);
        return ResponseEntity.ok().body(response);
    }


}
