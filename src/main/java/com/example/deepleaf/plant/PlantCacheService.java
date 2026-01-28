package com.example.deepleaf.plant;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.deepleaf.plant.domain.Plant;
import com.example.deepleaf.plant.dto.PlantResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlantCacheService {

    private final PlantRepository plantRepository;

    /**
     * 식물 목록을 페이지 단위로 캐싱.
     * key: "page:size"
     * value: PlantPage (content + totalElements)
     */
    @Cacheable(
        value = "plantList",
        key = "#pageable.pageNumber + ':' + #pageable.pageSize"
    )
    public PlantPage getPlantPage(Pageable pageable) {
        log.info("💾 [CACHE MISS] 식물 목록 DB 조회 시작: page={}, size={}",
            pageable.getPageNumber(), pageable.getPageSize());

        Page<Plant> plants = plantRepository.findAll(pageable);

        List<PlantResponse> content = plants.stream()
            .map(plant -> new PlantResponse(
                plant.getId(),
                plant.getImageUrl(),
                plant.getCommonName(),
                plant.getGenus(),
                plant.getFamily()
            ))
            .toList();

        log.info("✅ [CACHE MISS] 식물 목록 DB 조회 완료 및 캐시 저장: page={}, size={}, pageElements={}, totalElements={}",
            pageable.getPageNumber(),
            pageable.getPageSize(),
            plants.getNumberOfElements(),
            plants.getTotalElements()
        );

        return new PlantPage(content, plants.getTotalElements());
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PlantPage {
        private List<PlantResponse> content;
        private long totalElements;
    }
}
