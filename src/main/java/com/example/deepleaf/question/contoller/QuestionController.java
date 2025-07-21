package com.example.deepleaf.question.contoller;


import com.example.deepleaf.auth.annotation.LoginMember;
import com.example.deepleaf.question.dto.request.QuestionCreateRequest;
import com.example.deepleaf.question.dto.request.QuestionUpdateRequest;
import com.example.deepleaf.question.dto.response.QuestionCreateResponse;
import com.example.deepleaf.question.dto.response.QuestionResponse;
import com.example.deepleaf.question.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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


    // 질문 조회
    @GetMapping("")
    public ResponseEntity<Page<QuestionResponse>> getQuestions(
            @LoginMember Long memberId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ){
        Page<QuestionResponse> questionResponses = questionService.questionList(PageRequest.of(page, size));
        return ResponseEntity.ok().body(questionResponses);
    }

    // 질문 수정
    @PutMapping("/{question_id}")
    public ResponseEntity<QuestionResponse> questionModify(
            @LoginMember Long memberId,
            @PathVariable(value = "question_id") Long questionId,
            @ModelAttribute QuestionUpdateRequest questionUpdateRequest
    ) {
        QuestionResponse questionResponse = questionService.modifyQuestion(memberId, questionId, questionUpdateRequest);
        return ResponseEntity.ok().body(questionResponse);
    }

    //질문 삭제
    @DeleteMapping("/{question_id}")
    public ResponseEntity<String> questionDelete(
            @LoginMember Long memberId,
            @PathVariable(value = "question_id") Long questionId)
    {
        questionService.deleteQuestion(memberId, questionId);
        return ResponseEntity.ok().body("Deleted Success");
    }
}
