package com.rey.courier.api;

import com.rey.courier.domain.User;
import com.rey.courier.domain.repository.UserRepository;
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

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(
        @PathVariable UUID id,
        // --- CORRELATION ID START ---
        @RequestHeader(value = "X-Correlation-ID", required = false) String correlationId) {
        
        // Print the ID to the console to prove we received it
        System.out.println("[Identity Service] Processing request for User ID: " + id + 
                           " | Correlation ID: " + correlationId);
        // --- CORRELATION ID END ---

        return userRepository.findById(id)
                .map(user -> ResponseEntity.ok().body(user))
                .orElse(ResponseEntity.notFound().build()); 
    }
}