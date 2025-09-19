package com.koreait.short_pj_25_09;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ShortPj2509Application {

    public static void main(String[] args) {
        SpringApplication.run(ShortPj2509Application.class, args);
    }

}
