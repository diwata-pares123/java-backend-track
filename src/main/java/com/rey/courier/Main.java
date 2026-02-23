package com.rey.courier;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        // This single line replaces all the manual "wiring" 
        // and starts the web server, database connection, and DI container.
        SpringApplication.run(Main.class, args);
        
        System.out.println("--- Courier Microservice is Live ---");
    }
}