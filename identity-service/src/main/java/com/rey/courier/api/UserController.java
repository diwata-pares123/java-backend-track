package com.rey.courier.api;

import com.rey.courier.domain.User;
import com.rey.courier.domain.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
public class UserController {

    private final UserRepository userRepository; 

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/api/v1/public/status")
    public String getStatus() {
        return "Identity Service is UP (Public)";
    }

    @PostMapping("/api/v1/internal/users")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User savedUser = userRepository.save(user);
        System.out.println("[Identity Service] Successfully created internal user with ID: " + savedUser.getId());
        return ResponseEntity.status(201).body(savedUser);
    }

    @GetMapping("/api/v1/internal/users/{id}")
    public ResponseEntity<User> getInternalUser(
        @PathVariable UUID id,
        @RequestHeader(value = "X-Correlation-ID", required = false) String correlationId,
        @RequestHeader(value = "X-Internal-Service-Key", required = false) String serviceKey,
        // --- NEW: Read the propagated token! ---
        @RequestHeader(value = "Authorization", required = false) String userToken) {
        
        String EXPECTED_SECRET = "super-secret-delivery-key-2026";
        
        if (serviceKey == null || !serviceKey.equals(EXPECTED_SECRET)) {
            System.out.println("[SECURITY ALARM] Unauthorized access attempt blocked!");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); 
        }

        System.out.println("[Identity Service] Processing Internal request for User ID: " + id + 
                           " | Correlation ID: " + correlationId);
                           
        // --- NEW: Log the token to prove it arrived! ---
        if (userToken != null) {
            System.out.println("[Identity Service] Propagated User Token: " + userToken);
        }

        return userRepository.findById(id)
                .map(foundUser -> ResponseEntity.ok().body(foundUser))
                .orElse(ResponseEntity.notFound().build()); 
    }
}