package com.rey.courier.api;

import com.rey.courier.application.PackageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid; // ✅ M1: The essential validation import
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/packages")
@Tag(name = "Package Controller", description = "Operations related to courier package lifecycle management")
public class PackageController {

    private final PackageService packageService;

    public PackageController(PackageService packageService) {
        this.packageService = packageService;
    }

    @GetMapping("/health")
    @Operation(summary = "Health Check", description = "Confirms the V1 API is reachable and running")
    public com.rey.courier.api.ApiResponse<String> healthCheck() {
        return new com.rey.courier.api.ApiResponse<>(true, "Courier API V1 is running", null);
    }

    @PostMapping
    @Operation(summary = "Register a new package", description = "Accepts package details and generates a tracking number")
    @ApiResponse(responseCode = "201", description = "Package successfully created and tracked")
    // 🔥 THE WALL IS HERE: Notice the @Valid annotation right before @RequestBody
    public com.rey.courier.api.ApiResponse<PackageResponse> createPackage(@Valid @RequestBody PackageRequest request) {
        PackageResponse responsePayload = packageService.registerNewPackage(request);
        return new com.rey.courier.api.ApiResponse<>(true, "Package created successfully", responsePayload);
    }

    @GetMapping
    @Operation(summary = "List all packages", description = "Returns a paginated list of packages.")
    public com.rey.courier.api.ApiResponse<List<PackageResponse>> getPackages(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        int limitedSize = Math.min(size, 100);
        List<PackageResponse> packages = packageService.getAllPackages(page, limitedSize);
        
        return new com.rey.courier.api.ApiResponse<>(true, "Packages retrieved successfully", packages);
    }
}