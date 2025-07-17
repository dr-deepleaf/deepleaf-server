package com.example.deepleaf.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberLoginResDto {
    private Long id;
    private String accessToken;

    public static MemberLoginResDto createWith(Long id, String accessToken){
        return new MemberLoginResDto(id, accessToken);
    }
}
