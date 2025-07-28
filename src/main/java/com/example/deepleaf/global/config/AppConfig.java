package com.example.deepleaf.global.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Configuration
@Getter
public class AppConfig {

    @Bean
    public RestTemplate restTestTemplate(){
        return new RestTemplate();
    }

    @Value("${ai.ec2}")
    public static String aiServerAddress;

}
