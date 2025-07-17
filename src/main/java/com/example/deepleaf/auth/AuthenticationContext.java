package com.example.deepleaf.auth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.util.Objects;

@Component
@RequestScope
@Slf4j
public class AuthenticationContext {
    private static final String ANONYMOUS_USERNAME = "UNKNOWN";
    private Long memberId;

    public void setAuthentication(Long memberId) {
        this.memberId = memberId;
    }

    public void setAnonymousUsername(){
        this.memberId = 0L;
    }

    public Long getPrincipal() {
        if (Objects.isNull(this.memberId)) {
            throw new RuntimeException("memberId is null");
        }
        log.debug("authcontext memberId = {}", memberId);
        return memberId;
    }
}
