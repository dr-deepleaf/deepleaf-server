package com.example.deepleaf.disease.controller;

import com.example.deepleaf.disease.domain.Crop;
import com.example.deepleaf.disease.dto.request.DetectionRequest;
import com.example.deepleaf.disease.dto.request.QueryRequest;
import com.example.deepleaf.disease.dto.response.DetectionResponse;
import com.example.deepleaf.disease.dto.response.QueryResponse;
import com.example.deepleaf.disease.fixture.DiseaseTestFixture;
import com.example.deepleaf.helper.MockBeanInjection;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(DiseaseController.class)
class DiseaseControllerWebMvcTest extends MockBeanInjection {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private final String token = "Bearer validToken";

    @Test
    void 질병_진단_요청() throws Exception {
        //given
        MockMultipartFile mockImage = DiseaseTestFixture.createMockImage();
        DetectionRequest detectionRequest = DiseaseTestFixture.detectionRequest(); // apple request
        DetectionResponse detectionResponse = DiseaseTestFixture.detectionResponse(); // apple response

        //when
        when(diseaseService.predict(any(), any(DetectionRequest.class))).thenReturn(detectionResponse);

        //then
        mockMvc.perform(multipart("/api/disease/predict")
                .file(mockImage)
                .param("plant",detectionRequest.getPlant())
                .header("AUTHORIZATION", token))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.crop").value(Crop.APPLE.toString()));

    }

    @Test
    void 질병_진단_기록_요청() throws Exception {
        //given
        QueryRequest queryRequest = DiseaseTestFixture.queryRequest();
        ArrayList<QueryResponse> list = new ArrayList<>(20);
        for(int i=0; i < 20; i++){
            list.add(DiseaseTestFixture.queryResponse());

        }
        PageRequest pageRequest = PageRequest.of(0, 20);
        Page<QueryResponse> queryResponses = new PageImpl<>(list, pageRequest, 200);


        //when
        when(diseaseService.getDiseaseRecord(any(),any(Pageable.class),any(QueryRequest.class)))
                .thenReturn(queryResponses);

        //then
        mockMvc.perform(post("/api/disease/record")
                        .param("page", "0")
                        .param("size", "20")
                        .content(objectMapper.writeValueAsString(queryRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("AUTHORIZATION", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(20))
                .andExpect(jsonPath("$.content[0].crop").value(Crop.APPLE.toString()));
    }
}