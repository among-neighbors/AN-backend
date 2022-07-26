package com.knud4.an;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class AnApplication {

    public static void main(String[] args) {
        SpringApplication.run(AnApplication.class, args);
    }

}
