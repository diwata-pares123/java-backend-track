package com.rey.courier.service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class DashboardService {

    // We use a literal string "'totalPackages'" as the key
    @Cacheable(value = "dashboardStats", key = "'totalPackages'", sync = true)
    public String getGlobalPackageCount() {
        try {
            System.out.println("🔥 [Database] Running heavy COUNT(*) aggregate query...");
            Thread.sleep(5000); // Simulate a massive 5-second database calculation
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        return "1,492,031 Packages Delivered";
    }
}