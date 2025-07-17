package com.example.deepleaf.auth.fixture;

import com.example.deepleaf.auth.dto.request.MemberCreateReqDto;
import com.example.deepleaf.auth.dto.request.MemberLoginReqDto;
import com.example.deepleaf.auth.dto.response.MemberLoginResDto;
import com.example.deepleaf.member.domain.Member;

public class AuthTestFixture {
    public static Member createMockMember(){
        return Member.builder().email("auth-email").name("auth-name").password("auth-password").build();
    }
    public static MemberCreateReqDto memberCreateDto(){
        return MemberCreateReqDto.builder()
                .name("auth-name")
                .email("auth-email")
                .password("auth-password").build();
    }
    public static MemberLoginReqDto memberLoginDto(){
        return MemberLoginReqDto.builder()
                .email("auth-email")
                .password("auth-password").build();
    }

    public static MemberLoginResDto memberLoginResponseDto(){
        return MemberLoginResDto.createWith(1L, "accessToken");
    }
}
