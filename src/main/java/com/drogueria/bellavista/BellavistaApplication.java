package com.drogueria.bellavista;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BellavistaApplication {

    public static void main(String[] args) {
        SpringApplication.run(BellavistaApplication.class, args);
    }
}
