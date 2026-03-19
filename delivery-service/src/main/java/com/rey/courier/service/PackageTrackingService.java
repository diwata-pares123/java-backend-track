package com.rey.courier.service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict; // NEW IMPORT
import org.springframework.stereotype.Service;

@Service
public class PackageTrackingService {

    @Cacheable(value = "packageStatus", key = "#trackingId")
    public String getStatus(String trackingId) {
        try {
            System.out.println("🐢 [Database] Searching disk for tracking ID: " + trackingId + " (This is slow...)");
            Thread.sleep(3000); 
        } catch (InterruptedException e) { 
            e.printStackTrace(); 
        }
        return "IN_TRANSIT"; // Default mock status
    }

    // --- NEW METHOD ---
    // This tells Spring: "Go to the 'packageStatus' cache and delete the entry for this specific trackingId"
    @CacheEvict(value = "packageStatus", key = "#trackingId")
    public String updateStatus(String trackingId, String newStatus) {
        System.out.println("💾 [Database] Updating DB record for " + trackingId + " to: " + newStatus);
        System.out.println("🧹 [Redis] Cache evicted for tracking ID: " + trackingId);
        // In a real app, you would save to a Repository here.
        return newStatus;
    }
}