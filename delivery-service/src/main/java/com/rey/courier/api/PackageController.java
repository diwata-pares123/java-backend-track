package com.rey.courier.api;

import com.rey.courier.application.PackageOrchestrator;
import com.rey.courier.application.PackageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID; 

@RestController
@RequestMapping("/api/v1/packages")
public class PackageController {

    private final PackageOrchestrator packageOrchestrator;
    private final PackageService packageService;

    public PackageController(PackageOrchestrator packageOrchestrator, PackageService packageService) {
        this.packageOrchestrator = packageOrchestrator;
        this.packageService = packageService;
    }

    @PostMapping 
    public PackageResponse createPackage(
            @RequestBody PackageRequest request,
            @RequestHeader(value = "Authorization", required = false) String userToken) {
        return packageOrchestrator.orchestrateNewPackage(request, userToken);
    }

    @GetMapping("/{id}/status")
    public ResponseEntity<String> getPackageStatus(@PathVariable UUID id) {
        String status = packageService.checkPackageFinalStatus(id);
        return ResponseEntity.ok(status);
    }

    // --- NEW: ZERO-TRUST DATA OWNERSHIP CHECK ---
    @PostMapping("/{id}/cancel")
    public ResponseEntity<String> cancelPackage(
            @PathVariable UUID id,
            // The ID of the human making the request (Simulating a decoded JWT)
            @RequestHeader(value = "X-User-Id", required = true) String requestUserId,
            // Simulating a database lookup: "SELECT sender_id FROM packages WHERE id = ?"
            @RequestHeader(value = "X-Simulated-DB-Owner-Id", required = true) String packageOwnerId) {

        System.out.println("[Zero-Trust Policy] Verifying data ownership for Package: " + id);

        // ZERO-TRUST CHECK: Does this user actually own this specific resource?
        if (!requestUserId.equals(packageOwnerId)) {
            System.out.println("[SECURITY ALARM] User " + requestUserId + " attempted to modify a package they do not own!");
            // 403 Forbidden: You are a valid user, but you do not own this.
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Zero-Trust Violation: You do not have permission to modify this specific package.");
        }

        // If they match, proceed with the cancellation!
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