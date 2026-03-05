package com.rey.courier.api;

import com.rey.courier.application.PackageService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/packages") // This matches Postman exactly
public class PackageController {

    private final PackageService packageService;

    public PackageController(PackageService packageService) {
        this.packageService = packageService;
    }

    @PostMapping // This makes it handle POST requests
    public PackageResponse createPackage(@RequestBody PackageRequest request) {
        return packageService.registerNewPackage(request);
    }

    @GetMapping
    public List<PackageResponse> getAllPackages(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return packageService.getAllPackages(page, size);
    }
}