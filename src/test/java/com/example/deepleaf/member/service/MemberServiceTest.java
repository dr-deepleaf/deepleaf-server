package com.example.deepleaf.member.service;

import com.example.deepleaf.member.domain.Member;
import com.example.deepleaf.member.dto.MemberInfoResDto;
import com.example.deepleaf.member.fixture.MemberTestFixture;
import com.example.deepleaf.member.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {

    @InjectMocks
    MemberService memberService;

    @Mock
    MemberRepository memberRepository;

    @Test
    void memberId로_회원_객체를_찾는다() {
        //given
        Member mockMember = MemberTestFixture.mockMember();
        Long mockMemberId = mockMember.getId();
        when(memberRepository.findById(mockMemberId)).thenReturn(Optional.of(mockMember));

        //when
        MemberInfoResDto memberInfo = memberService.getMemberInfo(mockMemberId);

        //then
        Assertions.assertThat(memberInfo.getId()).isEqualTo(mockMemberId);

    }
}
