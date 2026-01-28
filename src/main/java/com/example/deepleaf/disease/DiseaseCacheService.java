package com.example.deepleaf.disease;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.deepleaf.disease.domain.Disease;
import com.example.deepleaf.disease.dto.DiseasePredictResponse;
import com.example.deepleaf.member.domain.Member;
import com.example.deepleaf.member.exception.MemberNotFound;
import com.example.deepleaf.member.repository.MemberRepository;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class DiseaseCacheService {

  private final DiseaseRepository diseaseRepository;
  private final MemberRepository memberRepository;

  /**
   * 회원 질병 이력 페이지 단위로 캐싱.
   * key: "memberId:page:size"
   * value: DiseaseHistoryPage (content + totalElements)
   */
  @Cacheable(
      value = "diseaseHistory",
      key = "#memberId + ':' + #pageable.pageNumber + ':' + #pageable.pageSize"
  )
  public DiseaseHistoryPage getDiseaseHistoryPage(Long memberId, Pageable pageable) {
    log.info("💾 [CACHE MISS] 질병 이력 DB 조회 시작: memberId={}, page={}, size={}",
        memberId, pageable.getPageNumber(), pageable.getPageSize());

    Member member = memberRepository.findById(memberId)
        .orElseThrow(MemberNotFound::new);

    Page<Disease> diseases =
        diseaseRepository.findByMemberOrderByCreatedAtDesc(member, pageable);

    List<DiseasePredictResponse> content = diseases.stream()
        .map(disease -> new DiseasePredictResponse(
            disease.getResult(),
            disease.getConfidence(),
            disease.getImageUrl(),
            disease.getCreatedAt()
        ))
        .collect(Collectors.toList());

    log.info("✅ [CACHE MISS] 질병 이력 DB 조회 완료 및 캐시 저장: memberId={}, page={}, size={}, pageElements={}, totalElements={}",
        memberId,
        pageable.getPageNumber(),
        pageable.getPageSize(),
        diseases.getNumberOfElements(),
        diseases.getTotalElements()
    );

    return new DiseaseHistoryPage(content, diseases.getTotalElements());
  }

  @Getter
  @AllArgsConstructor
  @lombok.NoArgsConstructor
  public static class DiseaseHistoryPage {
    private List<DiseasePredictResponse> content;
    private long totalElements;
  }
}

