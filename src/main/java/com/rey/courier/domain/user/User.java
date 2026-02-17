package com.rey.courier.domain.user;
import java.util.UUID;

public class User {
    private final UUID id;
    private final String email;
    private final String passwordHash;
    private final String role;

    public User(UUID id, String email, String passwordHash, String role) {
        this.id = id;
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;
    }

    public UUID getId() { return id; }
    public String getEmail() { return email; }
    public String getPasswordHash() { return passwordHash; }
    public String getRole() { return role; }
}