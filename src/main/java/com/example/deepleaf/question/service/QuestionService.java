package com.example.deepleaf.question.service;

import com.example.deepleaf.member.domain.Member;
import com.example.deepleaf.member.exception.MemberNotFound;
import com.example.deepleaf.member.repository.MemberRepository;
import com.example.deepleaf.question.domain.Question;
import com.example.deepleaf.question.dto.request.QuestionCreateRequest;
import com.example.deepleaf.question.dto.request.QuestionUpdateRequest;
import com.example.deepleaf.question.dto.response.QuestionCreateResponse;
import com.example.deepleaf.question.dto.response.QuestionResponse;
import com.example.deepleaf.question.exception.QuestionAccessUnauthorized;
import com.example.deepleaf.question.exception.QuestionNotFound;
import com.example.deepleaf.question.repository.QuestionRepository;
import com.example.deepleaf.storage.service.StorageManager;
import com.example.deepleaf.storage.service.StorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class QuestionService {
    private final StorageService storageService;
    private final QuestionRepository questionRepository;
    private final MemberRepository memberRepository;
    public QuestionCreateResponse createQuestion(Long memberId, QuestionCreateRequest questionCreateRequest) {
        String imageUrl = storageService.uploadFile(questionCreateRequest.getImage(), "questionImage", memberId);
        Question question = Question.createQuestion(questionCreateRequest,imageUrl);


        Member member = memberRepository.findById(memberId).orElseThrow(MemberNotFound::new);
        member.addQuestion(question);

        Question savedQuestion = questionRepository.save(question);
        return QuestionCreateResponse.create(savedQuestion);
    }

    public Page<QuestionResponse> questionList(Pageable pageable) {
        Page<Question> all = questionRepository.findAll(pageable);
        return all.map(QuestionResponse::create);
    }

    public QuestionResponse modifyQuestion(Long memberId, Long questionId, QuestionUpdateRequest questionUpdateRequest) {
        // 권한 체크
        Question question = checkAccessAndReturnQuestion(memberId, questionId);

        // 제목, 글 수정
        Question modifiedQuestion = question.modifyQuestion(questionUpdateRequest);

        // 이미지 수정
        // 이미지 변경 요청이 있다면, 기존 s3이미지 삭제하고 새로운 이미지 업로드
        if(questionUpdateRequest.getImage() != null){
            String originalImageUrl = question.getImage();
            storageService.deleteFile(originalImageUrl); //s3 이미지 삭제

            MultipartFile image = questionUpdateRequest.getImage();
            String updatedImageUrl = storageService.uploadFile(image, "questionImage", memberId);
            modifiedQuestion.updateImageUrl(updatedImageUrl);
        }

        return QuestionResponse.create(question);
    }

    public void deleteQuestion(Long memberId, Long questionId) {
        // 권한 체크
        Question question = checkAccessAndReturnQuestion(memberId, questionId);

        // 삭제
        questionRepository.deleteById(questionId);
    }

    public Question checkAccessAndReturnQuestion(Long memberId, Long questionId){
        // 수정 대상 질문 조회 후 제목, 내용 수정
        Question question = questionRepository.findById(questionId).orElseThrow(QuestionNotFound::new);
        log.debug("memberId: {}", memberId);
        log.debug("questionId: {}", question.getId());
        log.debug("question.getMember().getId(): {}", question.getMember().getId());
        if(question.getMember().getId() != memberId) {
            throw new QuestionAccessUnauthorized();
        }
        return question;
    }
}
