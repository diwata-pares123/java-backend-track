package com.rey.courier.api;

import com.rey.courier.application.PackageService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/packages")
public class PackageController {

    private final PackageService packageService;

    public PackageController(PackageService packageService) {
        this.packageService = packageService;
    }

    @GetMapping("/health")
    public ApiResponse<String> healthCheck() {
        // Wrapping a simple String payload
        return new ApiResponse<>(true, "Courier API is running", null);
    }

    @PostMapping
    public ApiResponse<PackageResponse> createPackage(@RequestBody PackageRequest request) {
        // 1. Get the raw DTO from the service
        PackageResponse responsePayload = packageService.registerNewPackage(request);
        
        // 2. Wrap it in our standard enterprise envelope
        return new ApiResponse<>(true, "Package created successfully", responsePayload);
    }
}