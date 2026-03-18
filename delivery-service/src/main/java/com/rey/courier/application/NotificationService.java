package com.rey.courier.application;

import com.rey.courier.event.PackageCreatedEvent;
// import org.springframework.context.event.EventListener;
// import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    // --- DISABLED: We are using RabbitMQ (@RabbitListener) now! ---
    // @Async("smsTaskExecutor") 
    // @EventListener
    public void handlePackageCreatedEvent(PackageCreatedEvent event) {
        
        System.out.println("🎧 [" + Thread.currentThread().getName() + "] Listener heard an event! Package ID: " + event.getPackageId());
        
        try {
            // Simulate slow SMS delay
            Thread.sleep(5000); 
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        System.out.println("[" + Thread.currentThread().getName() + "] ✅ SMS successfully sent for: Package ID " + event.getPackageId());
    }
}