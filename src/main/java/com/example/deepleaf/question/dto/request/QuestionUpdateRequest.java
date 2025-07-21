package com.example.deepleaf.question.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuestionUpdateRequest {

    @Size(max = 40, message = "제목은 40자 이하로 입력해주세요.")
    private String title;

    @Size(max = 500, message = "내용은 500자 이하로 입력해주세요.")
    private String content;

    private MultipartFile image;

    @Min(value = 0, message = "포인트는 0 이상이어야 합니다.")
    private Long point;
}
