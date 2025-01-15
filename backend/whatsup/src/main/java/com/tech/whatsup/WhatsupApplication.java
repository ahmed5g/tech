package com.tech.whatsup;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class WhatsupApplication {

    public static void main(String[] args) {
        SpringApplication.run(WhatsupApplication.class, args);
    }

}
