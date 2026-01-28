package com.example.deepleaf.disease;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.example.deepleaf.disease.domain.Disease;
import com.example.deepleaf.disease.dto.DiseasePredictResponse;
import com.example.deepleaf.member.domain.Member;
import com.example.deepleaf.member.exception.MemberNotFound;
import com.example.deepleaf.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class DiseaseCacheService {

  private final DiseaseRepository diseaseRepository;
  private final MemberRepository memberRepository;

  /**
   * 회원 전체 질병 이력 리스트를 캐싱.
   * - key: memberId
   * - value: List<DiseasePredictResponse>
   */
  @Cacheable(value = "diseaseHistory", key = "#memberId")
  public List<DiseasePredictResponse> getDiseaseHistoryList(Long memberId) {
    log.info("💾 [CACHE MISS] DB에서 조회 시작: memberId={}", memberId);

    Member member = memberRepository.findById(memberId)
        .orElseThrow(MemberNotFound::new);

    List<Disease> diseases = diseaseRepository.findByMemberOrderByCreatedAtDesc(member);

    log.info("✅ [CACHE MISS] DB 조회 완료 및 캐시 저장: memberId={}, count={}",
        memberId, diseases.size());

    return diseases.stream()
        .map(disease -> new DiseasePredictResponse(
            disease.getResult(),
            disease.getConfidence(),
            disease.getImageUrl(),
            disease.getCreatedAt()
        ))
        .collect(Collectors.toList());
  }
}

