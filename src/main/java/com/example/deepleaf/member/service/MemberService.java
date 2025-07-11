package com.example.deepleaf.member.service;

import com.example.deepleaf.member.common.auth.JwtTokenProvider;
import com.example.deepleaf.member.domain.Member;
import com.example.deepleaf.member.dto.request.MemberCreateDto;
import com.example.deepleaf.member.dto.request.MemberLoginDto;
import com.example.deepleaf.member.dto.response.MemberLoginResponseDto;
import com.example.deepleaf.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public Member create(MemberCreateDto memberCreateDto){
        Member member = Member.builder()
                .email(memberCreateDto.getEmail()).
                name(memberCreateDto.getName())
                .password(passwordEncoder.encode(memberCreateDto.getPassword()))
                .build();

        return memberRepository.save(member);
    }

    public MemberLoginResponseDto login(MemberLoginDto memberLoginDto){
        Optional<Member> member = memberRepository.findByEmail(memberLoginDto.getEmail());

        if(!member.isPresent()){
            throw new IllegalArgumentException("email이 존재하지 않습니다");
        }

        Member loginMember = member.get();

        // matches 메서드가 dto password를 자동으로 암호화 시켜 비교한다.
        if(!passwordEncoder.matches(memberLoginDto.getPassword(), loginMember.getPassword())){
            throw new IllegalArgumentException("password가 일치하지 않습니다");
        }

        String token = jwtTokenProvider.createToken(loginMember.getEmail(), loginMember.getRole().toString());

        return MemberLoginResponseDto.createWith(loginMember.getId(), token);

    }
}
