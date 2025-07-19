package com.example.deepleaf.auth.service;

import com.example.deepleaf.auth.dto.request.MemberCreateReqDto;
import com.example.deepleaf.auth.dto.request.MemberLoginReqDto;
import com.example.deepleaf.auth.dto.response.MemberCreateResDto;
import com.example.deepleaf.auth.dto.response.MemberLoginResDto;
import com.example.deepleaf.auth.fixture.AuthTestFixture;
import com.example.deepleaf.auth.jwt.JWTUtil;
import com.example.deepleaf.auth.repository.AuthRepository;
import com.example.deepleaf.auth.service.AuthService;
import com.example.deepleaf.member.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@Slf4j
@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {
    @InjectMocks
    private AuthService authService;
    @Mock
    private AuthRepository memberRepository;
    @Mock
    private JWTUtil jwtUtil;


    @Test
    void 회원가입_요청이_들어오면_회원이_저장되고_회원ID를_반환한다 (){

        //given
        MemberCreateReqDto memberCreateReqDto = AuthTestFixture.memberCreateDto();

        Member savedMember = Member.builder()
                .id(1L) // 👉 save 이후 반환되는 member에 ID를 부여해줌
                .email(memberCreateReqDto.getEmail())
                .name(memberCreateReqDto.getName())
                .password(memberCreateReqDto.getPassword())
                .build();

        when(memberRepository.save(any(Member.class))).thenReturn(savedMember);

        //when
        MemberCreateResDto memberCreateResDto = authService.create(memberCreateReqDto);

        //then
        verify(memberRepository).save(any(Member.class));
        Assertions.assertThat(memberCreateResDto.getId()).isEqualTo(savedMember.getId());
    }


    @Test
    void 로그인_요청이_들어오고_회원의_이메일과_비밀번호로_회원정보를_검증하고_엑세스토큰을_발급한다(){
        //Given
        MemberLoginReqDto memberLoginReqDto = AuthTestFixture.memberLoginDto();
        Member mockMember = AuthTestFixture.createMockMember();

        when(memberRepository.findByEmail(memberLoginReqDto.getEmail())).thenReturn(Optional.of(mockMember));
        when(jwtUtil.createAccessToken(mockMember.getId(),mockMember.getEmail(),mockMember.getRole())).thenReturn("accessToken");

        //When
        MemberLoginResDto memberLoginResDto = authService.login(memberLoginReqDto);

        //then
        Assertions.assertThat(memberLoginResDto.getAccessToken()).isEqualTo("accessToken");
        Assertions.assertThat(memberLoginResDto.getId()).isEqualTo(1L);
    }
}
