package org.example.interact;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class interactSpringApplication {
    public static void main(String[] args) {
        System.out.println("Hello world!");
    }
}