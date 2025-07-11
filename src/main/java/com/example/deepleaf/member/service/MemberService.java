package com.example.deepleaf.member.service;

import com.example.deepleaf.member.domain.Member;
import com.example.deepleaf.member.dto.request.MemberCreateDto;
import com.example.deepleaf.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public Member create(MemberCreateDto memberCreateDto){
        Member member = Member.builder()
                .email(memberCreateDto.getEmail()).
                name(memberCreateDto.getName())
                .password(memberCreateDto.getPassword())
                .build();

        return memberRepository.save(member);
    }
}
