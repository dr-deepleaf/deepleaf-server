package com.example.deepleaf.disease.fixture;

import com.example.deepleaf.disease.domain.Crop;
import com.example.deepleaf.disease.domain.Disease;
import com.example.deepleaf.disease.dto.request.DetectionRequest;
import com.example.deepleaf.disease.dto.request.QueryRequest;
import com.example.deepleaf.disease.dto.response.AiServerResponse;
import com.example.deepleaf.disease.dto.response.DetectionResponse;
import com.example.deepleaf.disease.dto.response.QueryResponse;
import org.springframework.mock.web.MockMultipartFile;

public class DiseaseTestFixture {

    public static DetectionRequest detectionRequest(){
        return new DetectionRequest(createMockImage(),"apple");
    }

    public static DetectionResponse detectionResponse(){
        return DetectionResponse.builder()
                .memberId(1L)
                .diseaseId(1L)
                .crop(Crop.APPLE)
                .result("result")
                .diseaseInfo("info")
                .confidence(0.92)
                .build();
    }

    public static Disease createDisease(){
        return Disease.createWith(aiServerResponse(), "apple");
    }


    public static AiServerResponse aiServerResponse(){
        AiServerResponse aiServerResponse = new AiServerResponse();
        aiServerResponse.setResult("result");
        aiServerResponse.setConfidence(0.92);
        aiServerResponse.setDiseaseInfo("info");
        return aiServerResponse;
    }
    public static QueryRequest queryRequest(){
        return new QueryRequest(Crop.APPLE);
    }

    public static QueryResponse queryResponse(){
        return QueryResponse.builder()
                .diseaseId(1L)
                .crop(Crop.APPLE)
                .result("result")
                .diseaseInfo("info")
                .confidence(0.92)
                .build();
    }

    //가짜 이미지
    public static MockMultipartFile createMockImage(){
        return new MockMultipartFile(
                "image",
                "disease.jpg",
                "image/jpeg",
                "fake image content".getBytes()
                );
    }
}
