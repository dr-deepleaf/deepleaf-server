package com.example.deepleaf.member.dto;

import com.example.deepleaf.member.domain.Member;
import com.example.deepleaf.member.domain.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberInfoResDto {

    private Long id;

    private String name;

    private String email;

    private String password;

    private Role role;

    public MemberInfoResDto(Member member) {
        this.id = member.getId();
        this.name = member.getName();
        this.email = member.getEmail();
        this.password = member.getPassword();
        this.role = member.getRole();
    }

    static public MemberInfoResDto createWith(Member member){
        return new MemberInfoResDto(member);
    }
}
