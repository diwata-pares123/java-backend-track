package com.rey.courier.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class DashboardService {

    // 1. Instantiate the Logger
    private static final Logger log = LoggerFactory.getLogger(DashboardService.class);

    @Cacheable(value = "dashboardStats", key = "'totalPackages'", sync = true)
    public String getGlobalPackageCount() {
        try {
            // 2. Use the logger instead of System.out.println
            log.info("Running heavy COUNT(*) aggregate query for global stats...");
            Thread.sleep(5000); 
        } catch (InterruptedException e) {
            log.error("Database query was interrupted!", e);
        }
        
        return "1,492,031 Packages Delivered";
    }
}