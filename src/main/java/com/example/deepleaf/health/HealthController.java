package com.example.deepleaf.health;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetAddress;
import java.net.UnknownHostException;

@RestController
public class HealthController {

    // 환경 변수에서 값을 읽어오고, 없으면 기본값으로 UNKNOWN 설정
    @Value("${EC2_PRIVATE_IP:UNKNOWN}")
    private String ec2Ip;

    @GetMapping("/health")
    public String health() {
        // 이제 복잡한 Java 코드 없이 변수만 리턴하면 됩니다.
        return ec2Ip;
    }
}
