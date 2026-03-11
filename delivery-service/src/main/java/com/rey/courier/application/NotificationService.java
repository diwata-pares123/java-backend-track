package com.rey.courier.application;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    @Async // This tells Spring to run this in a background thread pool
    public void sendDriverDispatchSms(String packageInfo) {
        System.out.println("[Notification-Thread] Preparing to send SMS to driver for: " + packageInfo + "...");
        try {
            // Simulate a slow 5-second network call to an SMS provider
            Thread.sleep(5000); 
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("[Notification-Thread] ✅ SMS successfully sent for: " + packageInfo);
    }
}