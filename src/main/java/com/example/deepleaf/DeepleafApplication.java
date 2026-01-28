package com.example.deepleaf;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class DeepleafApplication {

    public static void main(String[] args) {
        SpringApplication.run(DeepleafApplication.class, args);
    }

}
