package com.example.deepleaf.disease;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.deepleaf.disease.domain.Disease;
import com.example.deepleaf.member.domain.Member;

public interface DiseaseRepository extends JpaRepository<Disease, Long> {
    
    Page<Disease> findByMemberOrderByCreatedAtDesc(Member member, Pageable pageable);
}
