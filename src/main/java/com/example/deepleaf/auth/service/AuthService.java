package com.example.deepleaf.auth.service;

import com.example.deepleaf.auth.jwt.JWTUtil;
import com.example.deepleaf.auth.repository.AuthRepository;
import com.example.deepleaf.member.domain.Member;
import com.example.deepleaf.member.dto.request.MemberCreateDto;
import com.example.deepleaf.member.dto.request.MemberLoginDto;
import com.example.deepleaf.member.dto.response.MemberLoginResponseDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final AuthRepository memberRepository;
    private final JWTUtil jwtUtil;

    public Member create(MemberCreateDto memberCreateDto){
        Member member = Member.builder()
                .email(memberCreateDto.getEmail()).
                name(memberCreateDto.getName())
                .password(memberCreateDto.getPassword())
                .build();

        return memberRepository.save(member);
    }

    public MemberLoginResponseDto login(MemberLoginDto memberLoginDto){
        Optional<Member> member = memberRepository.findByEmail(memberLoginDto.getEmail());

        if(!member.isPresent()){
            throw new IllegalArgumentException("email이 존재하지 않습니다");
        }

        Member loginMember = member.get();

        log.debug("db login member ps: {}", memberLoginDto.getPassword());
        log.debug("try login member ps: {}", loginMember.getPassword());
        if(!memberLoginDto.getPassword().equals(loginMember.getPassword())){
            throw new IllegalArgumentException("password가 일치하지 않습니다");
        }

        String token = jwtUtil.createAccessToken(loginMember.getId(), loginMember.getEmail(), loginMember.getRole());

        return MemberLoginResponseDto.createWith(loginMember.getId(), token);

    }
}