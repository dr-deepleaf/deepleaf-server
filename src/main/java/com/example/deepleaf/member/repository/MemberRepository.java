package com.example.deepleaf.member.repository;

import com.example.deepleaf.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
