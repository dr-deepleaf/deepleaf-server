package com.example.deepleaf.plant;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.deepleaf.plant.dto.PlantResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/plant")
@RequiredArgsConstructor
public class PlantController {
    
    private final PlantService plantService;

    @GetMapping("")
    public ResponseEntity<Page<PlantResponse>> getPlants(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        Page<PlantResponse> plants = plantService.getPlants(PageRequest.of(page, size));
        return ResponseEntity.ok().body(plants);
    }

    /** 캐시 미사용 API (캐시 vs 비캐시 비교용) */
    @GetMapping("/no-cache")
    public ResponseEntity<Page<PlantResponse>> getPlantsWithoutCache(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        Page<PlantResponse> plants = plantService.getPlantsWithoutCache(PageRequest.of(page, size));
        return ResponseEntity.ok().body(plants);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<PlantResponse>> getPlantsByCommonName(
            @RequestParam(value = "commonName") String commonName,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        Page<PlantResponse> plants = plantService.getPlantsByCommonName(commonName, PageRequest.of(page, size));
        return ResponseEntity.ok().body(plants);
    }

    // genus, family 둘 다/하나만으로도 검색 가능 (둘 다 들어오면 AND 조건)
    @GetMapping("/search/taxonomy")
    public ResponseEntity<Page<PlantResponse>> getPlantsByTaxonomy(
            @RequestParam(value = "genus", required = false) String genus,
            @RequestParam(value = "family", required = false) String family,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        Page<PlantResponse> plants = plantService.getPlantsByTaxonomy(genus, family, PageRequest.of(page, size));
        return ResponseEntity.ok().body(plants);
    }
}
