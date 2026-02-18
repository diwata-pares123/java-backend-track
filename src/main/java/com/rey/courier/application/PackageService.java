package com.rey.courier.application;

import org.springframework.stereotype.Service;
import com.rey.courier.api.PackageRequest;
import com.rey.courier.api.PackageResponse;
import com.rey.courier.domain.delivery.DeliveryPackage;
import java.time.LocalDateTime;
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
}