package com.rey.courier;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder; // Added Builder
import java.time.Duration; // Added Time rules

@SpringBootApplication
public class Main {
    
    public static void main(String[] args) {
        // This single line replaces all the manual "wiring" 
        // and starts the web server, database connection, and DI container.
        SpringApplication.run(Main.class, args);
        
        System.out.println("--- Delivery Microservice is Live ---");
    }

    // THE NEW 3-SECOND RULE BEAN
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder
                .setConnectTimeout(Duration.ofSeconds(3)) // Give up if Identity doesn't answer the phone in 3s
                .setReadTimeout(Duration.ofSeconds(3))    // Give up if Identity answers but takes longer than 3s to send data
                .build();
    }
}