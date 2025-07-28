package com.example.deepleaf.disease.service;

import com.example.deepleaf.disease.domain.Crop;
import com.example.deepleaf.disease.domain.Disease;
import com.example.deepleaf.disease.dto.request.DetectionRequest;
import com.example.deepleaf.disease.dto.request.QueryRequest;
import com.example.deepleaf.disease.dto.response.AiServerResponse;
import com.example.deepleaf.disease.dto.response.DetectionResponse;
import com.example.deepleaf.disease.dto.response.QueryResponse;
import com.example.deepleaf.disease.fixture.DiseaseTestFixture;
import com.example.deepleaf.disease.repository.DiseaseRepository;
import com.example.deepleaf.member.domain.Member;
import com.example.deepleaf.member.exception.MemberNotFound;
import com.example.deepleaf.member.fixture.MemberTestFixture;
import com.example.deepleaf.member.repository.MemberRepository;
import com.example.deepleaf.storage.service.StorageService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DiseaseServiceTest {

    @InjectMocks
    private DiseaseService diseaseService;

    @Mock
    private DiseaseRepository diseaseRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private StorageService storageService;

    @Mock
    private RestTemplate restTemplate;
    @Test
    void 질병기록_페이지_조회한다() {
        // given
        Long memberId = 1L;
        Pageable pageable = PageRequest.of(0, 10);
        QueryRequest queryRequest = DiseaseTestFixture.queryRequest();
        List<QueryResponse> responseList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            responseList.add(DiseaseTestFixture.queryResponse());
        }

        Page<QueryResponse> mockPage = new PageImpl<>(responseList, pageable, 100);
        when(diseaseRepository.diseaseRecord(memberId, pageable, queryRequest)).thenReturn(mockPage);

        // when
        Page<QueryResponse> result = diseaseService.getDiseaseRecord(memberId, pageable, queryRequest);

        // then
        assertThat(result.getTotalElements()).isEqualTo(100);
        assertThat(result.getContent().get(0).getCrop()).isEqualTo(Crop.APPLE);
        verify(diseaseRepository).diseaseRecord(memberId, pageable, queryRequest);
    }

    @Test
    void 질병예측_정상처리된다() {
        Long memberId = 1L;
        DetectionRequest request = DiseaseTestFixture.detectionRequest();
        String s3ImageUrl = "https://mock-s3.com/image.jpg";

        when(storageService.uploadFile(any(), eq("disease"), eq(memberId))).thenReturn(s3ImageUrl);

        AiServerResponse aiServerResponse = DiseaseTestFixture.aiServerResponse();

        ResponseEntity<AiServerResponse> responseEntity = new ResponseEntity<>(aiServerResponse, HttpStatus.OK);

        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(AiServerResponse.class))
        ).thenReturn(responseEntity);

        Disease disease = DiseaseTestFixture.createDisease();
        when(diseaseRepository.save(any())).thenReturn(disease);

        Member member = MemberTestFixture.mockMember();
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));

        DetectionResponse result = diseaseService.predict(memberId, request);

        assertThat(result).isNotNull();
        assertThat(result.getCrop()).isEqualTo(Crop.APPLE);
    }

}