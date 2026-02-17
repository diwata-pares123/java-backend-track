package com.rey.courier.application;

import com.rey.courier.api.PackageRequest;
import com.rey.courier.api.PackageResponse;
import com.rey.courier.domain.delivery.DeliveryPackage;
import java.time.LocalDateTime;
import java.util.UUID;

public class PackageService {

    /**
     * Updated: Now accepts a DTO and returns a DTO.
     */
    public PackageResponse registerNewPackage(PackageRequest request) {
        // 1. Map Request DTO -> Domain Entity
        UUID newId = UUID.randomUUID();
        DeliveryPackage domainEntity = new DeliveryPackage(
            newId, 
            request.getSenderId(), 
            "PENDING", 
            LocalDateTime.now()
        );

        // 2. In a real app, we would save domainEntity to a DB here.

        // 3. Map Domain Entity -> Response DTO (The "Shield")
        return new PackageResponse(
            domainEntity.getId(),
            domainEntity.getStatus(),
            "TRK-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase()
        );
    }

    public PackageResponse updatePackageStatus(UUID packageId, String newStatus) {
        // Just a skeleton for now
        return new PackageResponse(packageId, newStatus, "TRK-EXISTING");
    }
}