package org.example.basic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class basicSpringApplication {
    public static void main(String[] args) {
        SpringApplication.run(basicSpringApplication.class, args);
    }
}