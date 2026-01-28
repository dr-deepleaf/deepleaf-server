package com.example.deepleaf.plant;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.example.deepleaf.plant.domain.Plant;
import com.example.deepleaf.plant.dto.PlantResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlantService {
    private final PlantRepository plantRepository;
    private final PlantCacheService plantCacheService;

    /**
     * 식물 목록을 페이지 단위로 조회.
     * - 캐시 키: "page:size"
     * - 캐시 값: PlantPage (content + totalElements)
     */
    public Page<PlantResponse> getPlants(Pageable pageable) {
        PlantCacheService.PlantPage cachedPage = plantCacheService.getPlantPage(pageable);

        return new PageImpl<>(
            cachedPage.getContent(),
            pageable,
            cachedPage.getTotalElements()
        );
    }

    public Page<PlantResponse> getPlantsByCommonName(String commonName, Pageable pageable) {
        Page<Plant> plants = plantRepository.findByCommonNameContainingIgnoreCase(commonName, pageable);
        return plants.map(this::toPlantResponse);
    }

    public Page<PlantResponse> getPlantsByTaxonomy(String genus, String family, Pageable pageable) {
        String normalizedGenus = StringUtils.hasText(genus) ? genus : null;
        String normalizedFamily = StringUtils.hasText(family) ? family : null;

        Page<Plant> plants = plantRepository.searchByGenusAndFamily(normalizedGenus, normalizedFamily, pageable);
        return plants.map(this::toPlantResponse);
    }

    private PlantResponse toPlantResponse(Plant plant) {
        return new PlantResponse(
            plant.getId(),
            plant.getImageUrl(),
            plant.getCommonName(),
            plant.getGenus(),
            plant.getFamily()
        );
    }
}
