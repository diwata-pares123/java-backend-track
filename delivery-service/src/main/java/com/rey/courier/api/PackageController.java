package com.rey.courier.api;

import com.rey.courier.application.PackageOrchestrator;
import com.rey.courier.application.PackageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID; 

@RestController
@RequestMapping("/api/v1/packages")
public class PackageController {

    private final PackageOrchestrator packageOrchestrator;
    private final PackageService packageService;

    // --- NEW: A memory store to remember requests we've already processed ---
    private final Set<String> processedKeys = new HashSet<>();

    public PackageController(PackageOrchestrator packageOrchestrator, PackageService packageService) {
        this.packageOrchestrator = packageOrchestrator;
        this.packageService = packageService;
    }

    @PostMapping 
    public ResponseEntity<Object> createPackage(
            @RequestBody PackageRequest request,
            @RequestHeader(value = "Authorization", required = false) String userToken,
            // --- NEW: Require the client to send a unique tracking key ---
            @RequestHeader(value = "Idempotency-Key", required = true) String idempotencyKey) {

        // 1. CHECK IDEMPOTENCY: Have we seen this exact request before?
        if (processedKeys.contains(idempotencyKey)) {
            System.out.println("♻️ [Idempotency Guard] Duplicate request detected for key: " + idempotencyKey + ". Returning safe success response.");
            // Return a 200 OK (not 201 Created) because we didn't actually create a new one this time.
            return ResponseEntity.ok("Package already created (Idempotent Cache Hit)");
        }

        // 2. NORMAL PROCESSING (Only happens the first time)
        PackageResponse response = packageOrchestrator.orchestrateNewPackage(request, userToken);

        // 3. SAVE THE KEY: Remember that we successfully processed this request
        processedKeys.add(idempotencyKey);

        // Return 201 Created on the first successful run
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}/status")
    public ResponseEntity<String> getPackageStatus(@PathVariable UUID id) {
        String status = packageService.checkPackageFinalStatus(id);
        return ResponseEntity.ok(status);
    }

    // --- PREVIOUS TICKET: ZERO-TRUST DATA OWNERSHIP CHECK ---
    @PostMapping("/{id}/cancel")
    public ResponseEntity<String> cancelPackage(
            @PathVariable UUID id,
            @RequestHeader(value = "X-User-Id", required = true) String requestUserId,
            @RequestHeader(value = "X-Simulated-DB-Owner-Id", required = true) String packageOwnerId) {

        System.out.println("[Zero-Trust Policy] Verifying data ownership for Package: " + id);

        if (!requestUserId.equals(packageOwnerId)) {
            System.out.println("[SECURITY ALARM] User " + requestUserId + " attempted to modify a package they do not own!");
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Zero-Trust Violation: You do not have permission to modify this specific package.");
        }

        String status = packageService.cancelPackage(id);
        return ResponseEntity.ok(status);
    }

    // --- PREVIOUS TICKET: THE VIP ROOM (Kept safe!) ---
    @DeleteMapping("/{id}/force-cancel")
    public ResponseEntity<String> forceCancelPackage(
            @PathVariable UUID id, 
            @RequestHeader(value = "X-User-Role", required = true) String userRole) {
        
        System.out.println("[Delivery Service] Force Cancel requested by role: " + userRole);

        if (!"ADMIN".equals(userRole.toUpperCase())) {
            System.out.println("[SECURITY ALARM] Non-admin attempted to force cancel!");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied: Admins Only");
        }

        packageService.updatePackageStatus(id, "CANCELLED_BY_ADMIN");
        return ResponseEntity.ok("Package " + id + " forcefully cancelled by Admin.");
    }

    @GetMapping
    public List<PackageResponse> getAllPackages(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return packageService.getAllPackages(page, size);
    }
}