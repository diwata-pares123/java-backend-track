package com.rey.courier;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.scheduling.annotation.EnableAsync; // Added
import java.time.Duration;

@SpringBootApplication
@EnableAsync // --- NEW: Tells Spring to look for @Async tasks! ---
public class Main {
    
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
        System.out.println("--- Delivery Microservice is Live ---");
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder
                .setConnectTimeout(Duration.ofSeconds(3))
                .setReadTimeout(Duration.ofSeconds(3))
                .build();
    }
}