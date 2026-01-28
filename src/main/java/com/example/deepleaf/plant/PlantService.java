package com.example.deepleaf.plant;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.example.deepleaf.plant.domain.Plant;
import com.example.deepleaf.plant.dto.PlantResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PlantService {
    private final PlantRepository plantRepository;

    public Page<PlantResponse> getPlants(Pageable pageable) {
        // 전체 식물 목록을 캐시에서 가져와 메모리에서 페이징
        List<PlantResponse> allPlants = getAllPlants();

        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), allPlants.size());
        List<PlantResponse> pageContent = allPlants.subList(start, end);

        return new PageImpl<>(pageContent, pageable, allPlants.size());
    }

    @Cacheable(value = "plantListAll")
    public List<PlantResponse> getAllPlants() {
        List<Plant> plants = plantRepository.findAll();
        return plants.stream()
            .map(this::toPlantResponse)
            .toList();
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
