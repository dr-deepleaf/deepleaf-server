package com.example.deepleaf.member.dto.response;

import com.example.deepleaf.member.domain.Member;
import com.example.deepleaf.member.dto.request.MemberCreateDto;
import com.example.deepleaf.member.dto.request.MemberLoginDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberLoginResponseDto {
    private Long id;
    private String accessToken;

    public static MemberLoginResponseDto createWith(Long id, String accessToken){
        return new MemberLoginResponseDto(id, accessToken);
    }
}
