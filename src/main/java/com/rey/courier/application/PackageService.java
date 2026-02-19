package com.rey.courier.application;

import org.springframework.stereotype.Service;
import com.rey.courier.api.PackageRequest;
import com.rey.courier.api.PackageResponse;
import com.rey.courier.domain.delivery.DeliveryPackage;

import java.time.LocalDateTime;
import java.util.List; // Added for Pagination
import java.util.UUID;

@Service
public class PackageService {

    public PackageResponse registerNewPackage(PackageRequest request) {
        // 1. Map Request DTO -> Domain Entity
        UUID newId = UUID.randomUUID();
        DeliveryPackage domainEntity = new DeliveryPackage(
            newId, 
            request.getSenderId(), 
            "PENDING", 
            LocalDateTime.now()
        );

        // 2. Map Domain Entity -> Response DTO
        return new PackageResponse(
            domainEntity.getId(),
            domainEntity.getStatus(),
            "TRK-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase()
        );
    }

    public PackageResponse updatePackageStatus(UUID packageId, String newStatus) {
        return new PackageResponse(packageId, newStatus, "TRK-EXISTING");
    }

    // NEW: Mock method for Module 3 Pagination
    public List<PackageResponse> getAllPackages(int page, int size) {
        return List.of(
            new PackageResponse(UUID.randomUUID(), "PENDING", "TRK-MOCK-1"),
            new PackageResponse(UUID.randomUUID(), "IN_TRANSIT", "TRK-MOCK-2")
        );
    }
}