package com.example.deepleaf.auth.repository;

import com.example.deepleaf.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);
}
