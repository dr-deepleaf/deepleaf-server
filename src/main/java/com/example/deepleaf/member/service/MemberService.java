package com.example.deepleaf.member.service;

import com.example.deepleaf.member.domain.Member;
import com.example.deepleaf.member.dto.MemberInfoResDto;
import com.example.deepleaf.member.exception.MemberNotFound;
import com.example.deepleaf.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    public MemberInfoResDto getMemberInfo(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(MemberNotFound::new);
        return MemberInfoResDto.createWith(member);
    }
}
