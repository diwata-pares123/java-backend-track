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

    // --- NEW: The POST mapping to create a new user! ---
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User savedUser = userRepository.save(user);
        System.out.println("[Identity Service] Successfully created new user with ID: " + savedUser.getId());
        // Return a 201 Created status along with the saved user data (including the generated UUID)
        return ResponseEntity.status(201).body(savedUser);
    }

    // --- EXISTING: The GET mapping to find a user ---
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
                .map(foundUser -> ResponseEntity.ok().body(foundUser))
                .orElse(ResponseEntity.notFound().build()); 
    }
}