package com.example.deepleaf.auth.service;

import com.example.deepleaf.auth.dto.request.MemberCreateReqDto;
import com.example.deepleaf.auth.dto.request.MemberLoginReqDto;
import com.example.deepleaf.auth.dto.response.MemberCreateResDto;
import com.example.deepleaf.auth.dto.response.MemberLoginResDto;
import com.example.deepleaf.auth.jwt.JWTUtil;
import com.example.deepleaf.auth.repository.AuthRepository;
import com.example.deepleaf.member.domain.Member;

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

    public MemberCreateResDto create(MemberCreateReqDto memberCreateReqDto){
        Member member = Member.builder()
                .email(memberCreateReqDto.getEmail())
                .name(memberCreateReqDto.getName())
                .password(memberCreateReqDto.getPassword())
                .build();

        memberRepository.save(member);

        return new MemberCreateResDto(member.getId());
    }

    public MemberLoginResDto login(MemberLoginReqDto memberLoginReqDto){
        Optional<Member> member = memberRepository.findByEmail(memberLoginReqDto.getEmail());

        if(!member.isPresent()){
            throw new IllegalArgumentException("email이 존재하지 않습니다");
        }

        Member loginMember = member.get();

        log.debug("db login member ps: {}", memberLoginReqDto.getPassword());
        log.debug("try login member ps: {}", loginMember.getPassword());
        if(!memberLoginReqDto.getPassword().equals(loginMember.getPassword())){
            throw new IllegalArgumentException("password가 일치하지 않습니다");
        }

        String token = jwtUtil.createAccessToken(loginMember.getId(), loginMember.getEmail(), loginMember.getRole());

        return MemberLoginResDto.createWith(loginMember.getId(), token);

    }
}