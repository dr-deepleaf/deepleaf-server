package com.example.deepleaf.question.service;

import com.example.deepleaf.member.domain.Member;
import com.example.deepleaf.member.exception.MemberNotFound;
import com.example.deepleaf.member.repository.MemberRepository;
import com.example.deepleaf.question.domain.Question;
import com.example.deepleaf.question.dto.request.QuestionCreateRequest;
import com.example.deepleaf.question.dto.request.QuestionUpdateRequest;
import com.example.deepleaf.question.dto.response.QuestionCreateResponse;
import com.example.deepleaf.question.dto.response.QuestionResponse;
import com.example.deepleaf.question.repository.QuestionRepository;
import com.example.deepleaf.storage.service.StorageManager;
import com.example.deepleaf.storage.service.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuestionService {
    private final StorageService storageService;
    private final QuestionRepository questionRepository;
    private final MemberRepository memberRepository;
    public QuestionCreateResponse createQuestion(Long memberId, QuestionCreateRequest questionCreateRequest) {
        String imageUrl = storageService.uploadFile(questionCreateRequest.getImage(), "questionImage", memberId);
        Question question = Question.createQuestion(questionCreateRequest,imageUrl);
        Question savedQuestion = questionRepository.save(question);

        Member member = memberRepository.findById(memberId).orElseThrow(MemberNotFound::new);
        member.addQuestion(question);

        return QuestionCreateResponse.create(savedQuestion);
    }

    public Page<QuestionResponse> questionList(Pageable pageable) {
        return null;
    }

    public QuestionResponse modifyQuestion(Long questionId, QuestionUpdateRequest questionUpdateRequest) {
        return null;
    }

    public void deleteQuestion(Long memberId, Long questionId) {
    }
}
