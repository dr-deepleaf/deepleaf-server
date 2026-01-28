package com.example.deepleaf.plant;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.example.deepleaf.plant.domain.Plant;
import com.example.deepleaf.plant.dto.PlantResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class PlantService {
    private final PlantRepository plantRepository;

    public Page<PlantResponse> getPlants(Pageable pageable) {
        Page<Plant> plants = plantRepository.findAll(pageable);
        return plants.map(this::toPlantResponse);
    }

    public Page<PlantResponse> getPlantsByCommonName(String commonName, Pageable pageable) {
        Page<Plant> plants = plantRepository.findByCommonNameContainingIgnoreCase(commonName, pageable);
        return plants.map(this::toPlantResponse);
    }

    public Page<PlantResponse> getPlantsByGenus(String genus, Pageable pageable) {
        Page<Plant> plants = plantRepository.findByGenusContainingIgnoreCase(genus, pageable);
        return plants.map(this::toPlantResponse);
    }

    public Page<PlantResponse> getPlantsByFamily(String family, Pageable pageable) {
        Page<Plant> plants = plantRepository.findByFamilyContainingIgnoreCase(family, pageable);
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
