package com.example.deepleaf.disease.service;

import com.example.deepleaf.disease.domain.Disease;
import com.example.deepleaf.disease.dto.request.DetectionRequest;
import com.example.deepleaf.disease.dto.request.QueryRequest;
import com.example.deepleaf.disease.dto.response.AiServerResponse;
import com.example.deepleaf.disease.dto.response.DetectionResponse;
import com.example.deepleaf.disease.dto.response.QueryResponse;
import com.example.deepleaf.disease.repository.DiseaseRepository;
import com.example.deepleaf.global.config.AppConfig;
import com.example.deepleaf.member.domain.Member;
import com.example.deepleaf.member.exception.MemberNotFound;
import com.example.deepleaf.member.repository.MemberRepository;
import com.example.deepleaf.storage.service.StorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class DiseaseService {
    private final DiseaseRepository diseaseRepository;
    private final StorageService storageService;
    private final MemberRepository memberRepository;
    private final RestTemplate restTemplate;
    private final String aiServerAddress = AppConfig.aiServerAddress;

    public DetectionResponse predict(Long memberId, DetectionRequest detectionRequest) {
        log.debug("ec2: {}", aiServerAddress);

        MultipartFile file = detectionRequest.getFile();
        String plant = detectionRequest.getPlant();

        // S3에 이미지 전송
        String s3ImageUrl = storageService.uploadFile(file,"disease", memberId);

        // AI 서버로 전송할 요청 생성
        HashMap<String, String> requestBody = new HashMap<>();
        requestBody.put("image_url", s3ImageUrl);

        // 타겟 식물
        log.debug("target crop: {}", plant);
        requestBody.put("crop", plant);


        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<HashMap<String, String>> requestEntity = new HttpEntity<>(requestBody, httpHeaders);

        // 요청 전송
        String fastApiUrl = "http://" + aiServerAddress +  "/predict";
        log.debug("fast api: {}", fastApiUrl);
        ResponseEntity<AiServerResponse> response = restTemplate.exchange(fastApiUrl, HttpMethod.POST, requestEntity, AiServerResponse.class);

        // 307 리다이렉트 처리
        if (response.getStatusCode() == HttpStatus.TEMPORARY_REDIRECT) {
            String newUrl = response.getHeaders().getLocation().toString(); // 새로운 URL 가져오기
            response = restTemplate.exchange(newUrl, HttpMethod.POST, requestEntity, AiServerResponse.class);
        }

        log.info("[resposne status code] = {}", response.getStatusCode());

        AiServerResponse aiServerResponse = response.getBody();

        aiServerResponse.setDiseaseInfo("disease info"); // GPT 대체 임시 진단


        Disease disease = diseaseRepository.save(Disease.createWith(aiServerResponse, plant));
        Member member = memberRepository.findById(memberId).orElseThrow(MemberNotFound::new);
        member.addDisease(disease);
        return DetectionResponse.createWith(disease,memberId);

    }

    public Page<QueryResponse> getDiseaseRecord(Long memberId, Pageable pageable, QueryRequest queryRequest) {
        return diseaseRepository.diseaseRecord(memberId, pageable, queryRequest);
    }
}
