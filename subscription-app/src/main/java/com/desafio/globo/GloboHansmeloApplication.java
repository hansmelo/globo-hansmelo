package com.desafio.globo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class GloboHansmeloApplication {
    public static void main(String[] args) {
        SpringApplication.run(GloboHansmeloApplication.class, args);
    }
}
