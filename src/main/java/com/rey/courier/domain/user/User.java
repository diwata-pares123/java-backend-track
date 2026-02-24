package com.rey.courier.domain; // Moved to flat domain

import jakarta.persistence.*; // Required for annotations
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String passwordHash;

    private String role;

    // For now, we'll comment out Address until we map it as an @Embeddable
    // private Address address; 

    // ✅ M4: This is the "One" side of the relationship
    // mappedBy = "sender" tells JPA that the 'sender' field in DeliveryPackage 
    // is the one actually holding the Foreign Key.
    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL)
    private List<DeliveryPackage> packages;

    // ✅ JPA Requirement: Protected no-args constructor
    protected User() {}

    public User(String email, String passwordHash, String role) {
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;
    }

    // Getters
    public UUID getId() { return id; }
    public String getEmail() { return email; }
    public String getRole() { return role; }
    public List<DeliveryPackage> getPackages() { return packages; }
}