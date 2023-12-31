package org.example.interact;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class interactSpringApplication {
    public static void main(String[] args) {
        SpringApplication.run(interactSpringApplication.class,args);
    }
}