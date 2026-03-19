package com.rey.courier.api;

import com.rey.courier.service.PackageTrackingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/tracking")
public class TrackingController {

    private final PackageTrackingService trackingService;

    public TrackingController(PackageTrackingService trackingService) {
        this.trackingService = trackingService;
    }

    @GetMapping("/{trackingId}")
    public ResponseEntity<String> getTrackingStatus(@PathVariable String trackingId) {
        long startTime = System.currentTimeMillis();
        String status = trackingService.getStatus(trackingId);
        long endTime = System.currentTimeMillis();
        
        return ResponseEntity.ok("Status: " + status + " | Time taken: " + (endTime - startTime) + "ms");
    }

    // --- NEW ENDPOINT ---
    @PutMapping("/{trackingId}")
    public ResponseEntity<String> updateTrackingStatus(
            @PathVariable String trackingId, 
            @RequestParam String status) { // Notice this is a Query Param!
        
        String updatedStatus = trackingService.updateStatus(trackingId, status);
        return ResponseEntity.ok("Successfully updated to: " + updatedStatus + " (Cache evicted!)");
    }
}