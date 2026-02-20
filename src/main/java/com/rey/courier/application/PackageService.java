package com.rey.courier.application;

import com.rey.courier.api.PackageRequest;
import com.rey.courier.api.PackageResponse;
import com.rey.courier.domain.DeliveryPackage;
import com.rey.courier.domain.PackageRepository;
import com.rey.courier.domain.UserRepository; // ✅ New Import
import com.rey.courier.domain.User;           // ✅ New Import
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PackageService {

    private final PackageRepository packageRepository;
    private final UserRepository userRepository; // ✅ Added UserRepository

    // Constructor Injection for both repositories
    public PackageService(PackageRepository packageRepository, UserRepository userRepository) {
        this.packageRepository = packageRepository;
        this.userRepository = userRepository;
    }

    public PackageResponse registerNewPackage(PackageRequest request) {
        // 1. 🔥 NEW: Find the User by the senderId from the request
        User sender = userRepository.findById(request.getSenderId())
                .orElseThrow(() -> new RuntimeException("Sender not found with ID: " + request.getSenderId()));

        // 2. Generate a tracking number
        String trackingNumber = "TRK-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        // 3. Map Request -> Entity (Passing the found 'sender' object)
        DeliveryPackage newPackage = new DeliveryPackage(trackingNumber, "PENDING", sender);

        // 4. Save to Supabase
        DeliveryPackage savedPackage = packageRepository.save(newPackage);

        // 5. Map Saved Entity -> Response DTO
        return new PackageResponse(
            savedPackage.getPackageId(),
            savedPackage.getStatus(),
            savedPackage.getTrackingNumber()
        );
    }

    public List<PackageResponse> getAllPackages(int page, int size) {
        return packageRepository.findAll(PageRequest.of(page, size))
                .getContent()
                .stream()
                .map(p -> new PackageResponse(p.getPackageId(), p.getStatus(), p.getTrackingNumber()))
                .collect(Collectors.toList());
    }

    public PackageResponse updatePackageStatus(UUID packageId, String newStatus) {
        return packageRepository.findById(packageId).map(existingPackage -> {
            // In a real app, you'd call a method on the entity to change status
            return new PackageResponse(existingPackage.getPackageId(), newStatus, existingPackage.getTrackingNumber());
        }).orElseThrow(() -> new RuntimeException("Package not found"));
    }
}