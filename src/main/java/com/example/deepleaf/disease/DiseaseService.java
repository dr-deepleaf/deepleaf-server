package com.example.deepleaf.disease;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.deepleaf.disease.domain.Disease;
import com.example.deepleaf.disease.dto.DiseasePredictRequest;
import com.example.deepleaf.disease.dto.DiseasePredictResponse;
import com.example.deepleaf.member.domain.Member;
import com.example.deepleaf.member.exception.MemberNotFound;
import com.example.deepleaf.member.repository.MemberRepository;
import com.example.deepleaf.storage.service.StorageService;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class DiseaseService {

    private final DiseaseRepository diseaseRepository;
    private final RestTemplate restTemplate;
    private final StorageService storageService;
    private final MemberRepository memberRepository;

    @Value("${disease.predict.api.url}")
    private String predictApiUrl;

    public DiseasePredictResponse predictDisease(Long memberId, DiseasePredictRequest request) {
        // Member 조회 (이메일 정보를 얻기 위해)
        Member member = memberRepository.findById(memberId)
            .orElseThrow(MemberNotFound::new);

        // 이미지 파일을 S3에 업로드하고 URL 획득 (이메일별로 저장)
        String imageUrl = storageService.uploadFileByEmail(
            request.getImage(), 
            "disease", 
            member.getEmail()
        );

        log.debug("S3 업로드 완료. 이미지 URL: {}, 이메일: {}", imageUrl, member.getEmail());

        // 외부 API 호출을 위한 요청 body 생성
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("image_url", imageUrl);
        requestBody.put("crop", request.getCrop());

        log.debug("외부 API 호출 URL: {}", predictApiUrl);
        log.debug("요청 body: {}", requestBody);

        // HTTP 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // HTTP 엔티티 생성
        HttpEntity<Map<String, String>> httpEntity = new HttpEntity<>(requestBody, headers);

        try {
            // 외부 API 호출 - 먼저 String으로 받아서 확인
            ResponseEntity<String> stringResponse = restTemplate.postForEntity(
                predictApiUrl,
                httpEntity,
                String.class
            );

            log.debug("외부 API 응답 상태: {}", stringResponse.getStatusCode());
            log.debug("외부 API 응답 body: {}", stringResponse.getBody());

            // String 응답을 DiseasePredictResponse로 변환
            if (stringResponse.getBody() != null && !stringResponse.getBody().isEmpty()) {
                // Jackson을 사용하여 JSON 파싱
                ObjectMapper objectMapper = new ObjectMapper();
                DiseasePredictResponse response = objectMapper.readValue(
                    stringResponse.getBody(), 
                    DiseasePredictResponse.class
                );
                log.debug("파싱된 응답: result={}, confidence={}", response.getResult(), response.getConfidence());

                // 질병 진단 기록 저장 (member는 이미 조회됨)
                Disease disease = Disease.createDisease(response.getResult(), response.getConfidence(), imageUrl);
                member.addDisease(disease);
                Disease savedDisease = diseaseRepository.save(disease);

                log.debug("질병 진단 기록 저장 완료: memberId={}, result={}, confidence={}, createdAt={}", 
                    memberId, response.getResult(), response.getConfidence(), savedDisease.getCreatedAt());

                // 응답에 생성 날짜 및 이미지 URL 포함
                response.setCreatedAt(savedDisease.getCreatedAt());
                response.setImageUrl(savedDisease.getImageUrl());

                return response;
            } else {
                log.warn("외부 API 응답이 null이거나 비어있습니다.");
                return new DiseasePredictResponse(null, null, null, null);
            }
        } catch (IOException e) {
            log.error("JSON 파싱 중 오류 발생", e);
            throw new RuntimeException("응답 파싱 실패: " + e.getMessage(), e);
        } catch (Exception e) {
            log.error("외부 API 호출 중 오류 발생", e);
            throw new RuntimeException("외부 API 호출 실패: " + e.getMessage(), e);
        }
    }

    public Page<DiseasePredictResponse> getDiseaseHistory(Long memberId, Pageable pageable) {
        // Member 조회
        Member member = memberRepository.findById(memberId)
            .orElseThrow(MemberNotFound::new);

        // 해당 회원의 진단 기록 조회 (최신순 정렬, 페이징)
        Page<Disease> diseases = diseaseRepository.findByMemberOrderByCreatedAtDesc(member, pageable);

        log.debug("진단 기록 조회 완료: memberId={}, count={}, totalElements={}", 
            memberId, diseases.getNumberOfElements(), diseases.getTotalElements());

        // Disease 엔티티를 DiseasePredictResponse DTO로 변환
        return diseases.map(disease -> new DiseasePredictResponse(
            disease.getResult(),
            disease.getConfidence(),
            disease.getImageUrl(),
            disease.getCreatedAt()
        ));
    }
}
