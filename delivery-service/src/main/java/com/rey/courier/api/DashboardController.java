package com.rey.courier.api;

import com.rey.courier.service.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/stats")
    public ResponseEntity<String> getStats() {
        long startTime = System.currentTimeMillis();
        
        String stats = dashboardService.getGlobalPackageCount();
        
        long timeTaken = System.currentTimeMillis() - startTime;
        
        return ResponseEntity.ok("Global Stats: " + stats + " | Time: " + timeTaken + "ms");
    }
}