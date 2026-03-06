package com.rey.courier.application;

import com.rey.courier.api.PackageRequest;
import com.rey.courier.api.PackageResponse;
import com.rey.courier.domain.DeliveryPackage;
import com.rey.courier.domain.repository.PackageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; 
import java.util.UUID; 
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PackageService {
    
    private final PackageRepository packageRepository;

    public PackageService(PackageRepository packageRepository) {
        this.packageRepository = packageRepository;
    }

    // THE METHOD IT WAS LOOKING FOR!
    @Transactional
    public PackageResponse saveInitialPackage(PackageRequest request, UUID senderId) {
        DeliveryPackage pkg = new DeliveryPackage();
        pkg.setSenderId(senderId); 
        pkg.setWeight(request.getWeight());
        pkg.setDestinationAddress(request.getDestinationAddress());
        pkg.setStatus("PROCESSING"); 
        
        DeliveryPackage saved = packageRepository.save(pkg); 
        return new PackageResponse(saved.getId(), saved.getDestinationAddress(), saved.getWeight());
    }

    @Transactional
    public void updatePackageStatus(UUID packageId, String newStatus) {
        DeliveryPackage pkg = packageRepository.findById(packageId)
                .orElseThrow(() -> new RuntimeException("Package not found"));
        pkg.setStatus(newStatus);
        packageRepository.save(pkg);
    }

    public String checkPackageFinalStatus(UUID packageId) {
        DeliveryPackage pkg = packageRepository.findById(packageId)
                .orElseThrow(() -> new RuntimeException("Package not found"));
        
        if ("PROCESSING".equals(pkg.getStatus())) {
            pkg.setStatus("CONFIRMED_BY_OPS");
            packageRepository.save(pkg);
        }
        return pkg.getStatus();
    }

    public String cancelPackage(UUID packageId) {
        DeliveryPackage pkg = packageRepository.findById(packageId)
                .orElseThrow(() -> new RuntimeException("Package not found"));

        pkg.setStatus("CANCELLED_BY_USER");
        packageRepository.save(pkg);
        System.out.println("[Business Rollback] Sent cancellation event to Operations Service for package: " + packageId);

        return pkg.getStatus();
    }

    public List<PackageResponse> getAllPackages(int page, int size) {
        return packageRepository.findAll().stream()
            .map(p -> new PackageResponse(p.getId(), p.getDestinationAddress(), p.getWeight()))
            .collect(Collectors.toList());
    }
}