package com.rey.courier.api;

import com.rey.courier.application.PackageOrchestrator;
import com.rey.courier.application.PackageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID; 

@RestController
@RequestMapping("/api/v1/packages")
public class PackageController {

    private final PackageOrchestrator packageOrchestrator;
    private final PackageService packageService;

    // Inject BOTH the Orchestrator and the Service
    public PackageController(PackageOrchestrator packageOrchestrator, PackageService packageService) {
        this.packageOrchestrator = packageOrchestrator;
        this.packageService = packageService;
    }

    @PostMapping 
    public PackageResponse createPackage(@RequestBody PackageRequest request) {
        // --- MODULE 5 CHANGE ---
        // Hand off the complex workflow to the Orchestrator!
        return packageOrchestrator.orchestrateNewPackage(request);
    }

    @GetMapping("/{id}/status")
    public ResponseEntity<String> getPackageStatus(@PathVariable UUID id) {
        String status = packageService.checkPackageFinalStatus(id);
        return ResponseEntity.ok(status);
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<String> cancelPackage(@PathVariable UUID id) {
        String status = packageService.cancelPackage(id);
        return ResponseEntity.ok(status);
    }

    @GetMapping
    public List<PackageResponse> getAllPackages(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return packageService.getAllPackages(page, size);
    }
}