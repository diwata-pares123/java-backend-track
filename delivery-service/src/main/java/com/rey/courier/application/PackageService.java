package com.rey.courier.application;

import com.rey.courier.api.PackageRequest;
import com.rey.courier.api.PackageResponse;
import com.rey.courier.domain.DeliveryPackage;
import com.rey.courier.domain.User;
import com.rey.courier.domain.repository.PackageRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*; // Updated to include HttpHeaders, HttpEntity, etc.
import org.springframework.web.client.HttpClientErrorException;
import java.util.UUID; // Added for Correlation ID
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PackageService {
    
    private final PackageRepository packageRepository;
    private final RestTemplate restTemplate;

    public PackageService(PackageRepository packageRepository, RestTemplate restTemplate) {
        this.packageRepository = packageRepository;
        this.restTemplate = restTemplate;
    }

    public PackageResponse registerNewPackage(PackageRequest request) {
        // --- CORRELATION ID START ---
        String correlationId = UUID.randomUUID().toString();
        System.out.println("[Delivery Service] Generated Correlation ID: " + correlationId);

        String url = "http://localhost:8080/api/v1/users/" + request.getSenderId();

        // 1. Set the Header
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Correlation-ID", correlationId);

        // 2. Wrap headers in an HttpEntity
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        // --- CORRELATION ID END ---

        User sender;

        try {
            // 3. Changed from getForEntity to exchange() to send headers
            ResponseEntity<User> response = restTemplate.exchange(
                url, 
                HttpMethod.GET, 
                entity, 
                User.class
            );
            sender = response.getBody();
            
        } catch (HttpClientErrorException.NotFound e) {
            throw new IllegalArgumentException("Invalid Sender ID: User does not exist in Identity system.");
        } catch (Exception e) {
            throw new RuntimeException("Identity Service is currently unavailable. Please try again later.");
        }

        DeliveryPackage pkg = new DeliveryPackage();
        pkg.setSender(sender);
        pkg.setWeight(request.getWeight());
        pkg.setDestinationAddress(request.getDestinationAddress());
        
        DeliveryPackage saved = packageRepository.save(pkg);
        
        return new PackageResponse(saved.getId(), saved.getDestinationAddress(), saved.getWeight());
    }

    public List<PackageResponse> getAllPackages(int page, int size) {
        return packageRepository.findAll().stream()
            .map(p -> new PackageResponse(p.getId(), p.getDestinationAddress(), p.getWeight()))
            .collect(Collectors.toList());
    }
}