package com.example.deepleaf.comment.controller;

import com.example.deepleaf.comment.dto.request.CommentRequest;
import com.example.deepleaf.comment.dto.response.CommentResponse;
import com.example.deepleaf.comment.fixture.CommentTestFixture;
import com.example.deepleaf.helper.MockBeanInjection;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(CommentController.class)
class CommentControllerWebMvcTest extends MockBeanInjection {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;
    private final String token = "Bearer token";

    @Test
    void 댓글_생성() throws Exception {

        //given
        CommentRequest commentRequest = CommentTestFixture.commentRequest();
        CommentResponse response = CommentTestFixture.commentResponse();

        //when
        when(commentService.commentCreate(
                any(Long.class), any(Long.class), any(CommentRequest.class)))
                .thenReturn(response);

        //then
        mockMvc.perform(post("/question/1/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentRequest))
                        .header("AUTHORIZATION", token))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.commentId").value(1L));
    }

    @Test
    void 질문_20개_조회() throws Exception {
        //given
        CommentResponse response = CommentTestFixture.commentResponse();
        List<CommentResponse> responses = new ArrayList<>(20);

        for(int i=0; i < 20; i++){
            responses.add(response);
        }

        PageRequest pageRequest = PageRequest.of(0, 20);
        PageImpl<CommentResponse> commentResponses = new PageImpl<>(responses, pageRequest, 100);

        //when
        when(commentService.getCommentList(1L,pageRequest)).thenReturn(commentResponses);

        //then
        mockMvc.perform(get("/question/1/comments?page=0&size=20")
                        .header("AUTHORIZATION", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(20));

    }

    @Test
    void deleteComment() throws Exception {
        // given & when
        doNothing().when(commentService).deleteComment(any(Long.class), any(Long.class));

        // then
        mockMvc.perform(delete("/comment/1").header("AUTHORIZATION", token))
                .andExpect(status().isOk())
                .andExpect(content().string("comment deleted"));

    }
}