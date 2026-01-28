package com.example.deepleaf.disease;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.deepleaf.auth.annotation.LoginMember;
import com.example.deepleaf.disease.dto.DiseasePredictRequest;
import com.example.deepleaf.disease.dto.DiseasePredictResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/disease")
@RequiredArgsConstructor
public class DiseaseController {

    private final DiseaseService diseaseService;

    @PostMapping("/predict")
    public ResponseEntity<DiseasePredictResponse> predictDisease(@LoginMember Long memberId, @ModelAttribute DiseasePredictRequest request) {
        DiseasePredictResponse response = diseaseService.predictDisease(memberId, request);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/history")
    public ResponseEntity<Page<DiseasePredictResponse>> getDiseaseHistory(
            @LoginMember Long memberId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        Page<DiseasePredictResponse> history = diseaseService.getDiseaseHistory(memberId, PageRequest.of(page, size));
        return ResponseEntity.ok().body(history);
    }
  
}
