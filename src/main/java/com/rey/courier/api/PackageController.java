package com.rey.courier.api;

import com.rey.courier.application.PackageService;
import org.springframework.web.bind.annotation.*;

import java.util.List; // Added for Pagination

@RestController
@RequestMapping("/api/packages")
public class PackageController {

    private final PackageService packageService;

    public PackageController(PackageService packageService) {
        this.packageService = packageService;
    }

    @GetMapping("/health")
    public ApiResponse<String> healthCheck() {
        return new ApiResponse<>(true, "Courier API is running", null);
    }

    @PostMapping
    public ApiResponse<PackageResponse> createPackage(@RequestBody PackageRequest request) {
        PackageResponse responsePayload = packageService.registerNewPackage(request);
        return new ApiResponse<>(true, "Package created successfully", responsePayload);
    }

    // NEW: Paginated GET endpoint
    @GetMapping
    public ApiResponse<List<PackageResponse>> getPackages(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        List<PackageResponse> packages = packageService.getAllPackages(page, size);
        
        return new ApiResponse<>(
            true, 
            "Packages retrieved successfully (Page: " + page + ")", 
            packages
        );
    }
}