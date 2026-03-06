package com.rey.courier.application;

import com.rey.courier.api.PackageRequest;
import com.rey.courier.api.PackageResponse;
import com.rey.courier.domain.DeliveryPackage;
import com.rey.courier.domain.User;
import com.rey.courier.domain.repository.PackageRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*; 
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.transaction.annotation.Transactional; 
import java.util.UUID; 
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

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Correlation-ID", correlationId);
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        // --- CORRELATION ID END ---

        User sender;
        try {
            ResponseEntity<User> response = restTemplate.exchange(
                url, HttpMethod.GET, entity, User.class
            );
            sender = response.getBody();
        } catch (HttpClientErrorException.NotFound e) {
            throw new IllegalArgumentException("Invalid Sender ID: User does not exist.");
        } catch (Exception e) {
            throw new RuntimeException("Identity Service unavailable.");
        }

        // --- PHASE 1: INITIAL LOCAL TRANSACTION ---
        DeliveryPackage pkg = new DeliveryPackage();
        
        // ✅ FIXED: We now set the Sender ID (UUID) instead of the User object
        pkg.setSenderId(sender.getId()); 
        
        pkg.setWeight(request.getWeight());
        pkg.setDestinationAddress(request.getDestinationAddress());
        pkg.setStatus("PENDING"); // Set initial status
        
        DeliveryPackage saved = saveToDatabase(pkg); 

        // --- PHASE 2: SAGA COMPENSATING ACTION SIMULATION ---
        try {
            // SIMULATE calling Operations Service to assign a van
            // String opsUrl = "http://localhost:8082/api/v1/vans/assign";
            
            // SIMULATE A CRASH (No vans available)
            throw new RuntimeException("Operations Service: No vans available!");
            
        } catch (Exception e) {
            // THE COMPENSATING ACTION
            // We explicitely update the record instead of rolling back the DB
            saved.setStatus("CANCELLED_NO_VANS");
            saveToDatabase(saved); // Save the explicit cancellation
            
            // Rethrow so the user/Postman sees the error
            throw new RuntimeException("Package creation failed: " + e.getMessage());
        }
    }

    @Transactional
    protected DeliveryPackage saveToDatabase(DeliveryPackage pkg) {
        return packageRepository.save(pkg);
    }

    public List<PackageResponse> getAllPackages(int page, int size) {
        return packageRepository.findAll().stream()
            .map(p -> new PackageResponse(p.getId(), p.getDestinationAddress(), p.getWeight()))
            .collect(Collectors.toList());
    }
}