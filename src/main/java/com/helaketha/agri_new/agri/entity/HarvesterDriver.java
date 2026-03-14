package com.helaketha.agri_new.agri.entity;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;

public class HarvesterDriver {

    private Integer harvesterDriverId;

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Phone is required")
    private String phone;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email; // Required for Keycloak

    private Integer availableMachines;
    private BigDecimal pricePerAcre;

    @NotBlank(message = "Username is required")
    private String username;

    private String keycloakUserId; // Link to Keycloak

    // --- Getters and Setters ---
    public Integer getHarvesterDriverId() { return harvesterDriverId; }
    public void setHarvesterDriverId(Integer harvesterDriverId) { this.harvesterDriverId = harvesterDriverId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Integer getAvailableMachines() { return availableMachines; }
    public void setAvailableMachines(Integer availableMachines) { this.availableMachines = availableMachines; }

    public BigDecimal getPricePerAcre() { return pricePerAcre; }
    public void setPricePerAcre(BigDecimal pricePerAcre) { this.pricePerAcre = pricePerAcre; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getKeycloakUserId() { return keycloakUserId; }
    public void setKeycloakUserId(String keycloakUserId) { this.keycloakUserId = keycloakUserId; }
}