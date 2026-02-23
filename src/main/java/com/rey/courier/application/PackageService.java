package com.rey.courier.application;

import com.rey.courier.api.PackageRequest;
import com.rey.courier.api.PackageResponse;
import com.rey.courier.domain.DeliveryPackage;
import com.rey.courier.domain.User;
import com.rey.courier.domain.repository.PackageRepository;
import com.rey.courier.domain.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PackageService {
    private final PackageRepository packageRepository;
    private final UserRepository userRepository;

    public PackageService(PackageRepository packageRepository, UserRepository userRepository) {
        this.packageRepository = packageRepository;
        this.userRepository = userRepository;
    }

    public PackageResponse registerNewPackage(PackageRequest request) {
        User sender = userRepository.findById(request.getSenderId())
            .orElseThrow(() -> new RuntimeException("User not found"));

        DeliveryPackage pkg = new DeliveryPackage();
        pkg.setSender(sender);
        pkg.setWeight(request.getWeight());
        pkg.setDestinationAddress(request.getDestinationAddress());
        
        DeliveryPackage saved = packageRepository.save(pkg);
        
        // Return the DTO, not the Entity
        return new PackageResponse(saved.getId(), saved.getDestinationAddress(), saved.getWeight());
    }

    public List<PackageResponse> getAllPackages(int page, int size) {
        return packageRepository.findAll().stream()
            .map(p -> new PackageResponse(p.getId(), p.getDestinationAddress(), p.getWeight()))
            .collect(Collectors.toList());
    }
}