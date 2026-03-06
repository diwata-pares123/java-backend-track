package com.rey.courier.api;

import com.rey.courier.domain.User;
import com.rey.courier.domain.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserRepository userRepository; 

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User savedUser = userRepository.save(user);
        System.out.println("[Identity Service] Successfully created new user with ID: " + savedUser.getId());
        return ResponseEntity.status(201).body(savedUser);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(
        @PathVariable UUID id,
        @RequestHeader(value = "X-Correlation-ID", required = false) String correlationId,
        // --- SECURITY ALARM NEW --- Demand the secret key!
        @RequestHeader(value = "X-Internal-Service-Key", required = false) String serviceKey) {
        
        // 1. Check the VIP Pass
        String EXPECTED_SECRET = "super-secret-delivery-key-2026";
        
        if (serviceKey == null || !serviceKey.equals(EXPECTED_SECRET)) {
            System.out.println("[SECURITY ALARM] Unauthorized access attempt blocked!");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 401 Error
        }

        System.out.println("[Identity Service] Processing request for User ID: " + id + 
                           " | Correlation ID: " + correlationId);

        return userRepository.findById(id)
                .map(foundUser -> ResponseEntity.ok().body(foundUser))
                .orElse(ResponseEntity.notFound().build()); 
    }
}