package com.example.deepleaf.disease.controller;

import com.example.deepleaf.auth.annotation.LoginMember;
import com.example.deepleaf.disease.dto.request.DetectionRequest;
import com.example.deepleaf.disease.dto.request.QueryRequest;
import com.example.deepleaf.disease.dto.response.DetectionResponse;
import com.example.deepleaf.disease.dto.response.QueryResponse;
import com.example.deepleaf.disease.service.DiseaseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/disease")
public class DiseaseController {

    private final DiseaseService diseaseService;

    @PostMapping("/predict")
    public ResponseEntity<DetectionResponse> predict(@LoginMember Long memberId, @ModelAttribute DetectionRequest detectionRequest) throws IOException {
        DetectionResponse response = diseaseService.predict(memberId, detectionRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/record")
    public ResponseEntity<Page<QueryResponse>> getRecord(
            @LoginMember Long memberId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestBody QueryRequest queryRequest
            )
    {

        Pageable pageable = PageRequest.of(page,size);
        Page<QueryResponse> response = diseaseService.getDiseaseRecord(memberId, pageable, queryRequest);
        return ResponseEntity.ok().body(response);
    }



}
