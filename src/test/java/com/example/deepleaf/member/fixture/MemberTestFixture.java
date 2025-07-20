package com.example.deepleaf.member.fixture;

import com.example.deepleaf.member.domain.Member;
import com.example.deepleaf.member.dto.MemberInfoResDto;

public class MemberTestFixture {
    public static Member mockMember() {
        Member member = Member.builder()
                .id(1L)
                .name("auth-name")
                .email("auth-email")
                .password("auth-password")
                .build();
        return member;
    }

    public static MemberInfoResDto memberInfoResDto() {
        Member member = mockMember();
        return MemberInfoResDto.createWith(member);
    }
}
