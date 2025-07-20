package com.example.deepleaf.question.service;

import com.example.deepleaf.question.domain.Question;
import com.example.deepleaf.question.dto.request.QuestionCreateRequest;
import com.example.deepleaf.question.dto.response.QuestionCreateResponse;
import com.example.deepleaf.storage.service.StorageManager;
import com.example.deepleaf.storage.service.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class QuestionService {
    private final StorageService storageService;
    public QuestionCreateResponse createQuestion(Long memberId, QuestionCreateRequest questionCreateRequest) {
        String imageUrl = storageService.uploadFile(questionCreateRequest.getImage(), "questionImage", memberId);
        Question question = Question.createQuestion(questionCreateRequest,imageUrl);
        return QuestionCreateResponse.create(question);
    }
}
