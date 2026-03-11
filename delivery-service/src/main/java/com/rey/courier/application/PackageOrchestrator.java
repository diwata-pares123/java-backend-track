package com.rey.courier.application;

import com.rey.courier.api.PackageRequest;
import com.rey.courier.api.PackageResponse;
import com.rey.courier.domain.User;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import java.util.UUID;

@Service
public class PackageOrchestrator {
    
    private final PackageService packageService;
    private final RestTemplate restTemplate;
    private final NotificationService notificationService; // Added

    // Updated Constructor
    public PackageOrchestrator(PackageService packageService, RestTemplate restTemplate, NotificationService notificationService) {
        this.packageService = packageService;
        this.restTemplate = restTemplate;
        this.notificationService = notificationService; // Added
    }

    public PackageResponse orchestrateNewPackage(PackageRequest request, String userToken) {
        System.out.println("[Orchestrator] Starting workflow for new package...");

        String correlationId = UUID.randomUUID().toString();
        String url = "http://localhost:8080/api/v1/internal/users/" + request.getSenderId();
        
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Correlation-ID", correlationId);
        headers.set("X-Internal-Service-Key", "super-secret-delivery-key-2026"); 

        if (userToken != null) {
            headers.set("Authorization", userToken);
            System.out.println("[Orchestrator] Propagating user token downstream...");
        }

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        User sender;
        try {
            ResponseEntity<User> response = restTemplate.exchange(
                url, HttpMethod.GET, entity, User.class
            );
            sender = response.getBody();
        } catch (HttpClientErrorException.Unauthorized e) {
            throw new RuntimeException("Security Error: Identity Service rejected our key.");
        } catch (HttpClientErrorException.NotFound e) {
            throw new IllegalArgumentException("Invalid Sender ID: User does not exist.");
        } catch (Exception e) {
            throw new RuntimeException("Identity Service unavailable.");
        }

        PackageResponse savedResponse = packageService.saveInitialPackage(request, sender.getId());

        // --- NEW: Trigger the ASYNC Notification ---
        // We use the ID or Tracking Number from the savedResponse.
        // The orchestrator will NOT wait 5 seconds for this to finish!
        notificationService.sendDriverDispatchSms("Package ID: " + savedResponse.getId());

        try {
             System.out.println("[Orchestrator] Operations check passed!");
        } catch (Exception e) {
             packageService.updatePackageStatus(savedResponse.getId(), "CANCELLED_NO_VANS");
             throw new RuntimeException("Orchestration failed at Operations step.");
        }

        return savedResponse;
    }
}