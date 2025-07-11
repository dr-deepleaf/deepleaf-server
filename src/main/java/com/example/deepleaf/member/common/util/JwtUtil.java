package com.example.deepleaf.member.common.util;

import com.example.deepleaf.member.domain.Member;
import com.example.deepleaf.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JwtUtil {

    private final MemberRepository memberRepository;

    public Member getLoginMember(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return memberRepository.findByEmail(email).orElseThrow(IllegalAccessError::new);
    }
}
