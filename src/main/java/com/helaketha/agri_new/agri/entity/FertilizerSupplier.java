package com.helaketha.agri_new.agri.entity;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;

public class FertilizerSupplier {

    private Integer supplierId;

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Phone is required")
    private String phone;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email; // Required for Keycloak

    private String fertilizerType;
    private Integer stockQuantityLiters;
    private BigDecimal pricePerLiter;

    @NotBlank(message = "Username is required")
    private String username;

    private String keycloakUserId; // Stores the link to Keycloak

    // --- Getters and Setters ---
    public Integer getSupplierId() { return supplierId; }
    public void setSupplierId(Integer supplierId) { this.supplierId = supplierId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getFertilizerType() { return fertilizerType; }
    public void setFertilizerType(String fertilizerType) { this.fertilizerType = fertilizerType; }

    public Integer getStockQuantityLiters() { return stockQuantityLiters; }
    public void setStockQuantityLiters(Integer stockQuantityLiters) { this.stockQuantityLiters = stockQuantityLiters; }

    public BigDecimal getPricePerLiter() { return pricePerLiter; }
    public void setPricePerLiter(BigDecimal pricePerLiter) { this.pricePerLiter = pricePerLiter; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getKeycloakUserId() { return keycloakUserId; }
    public void setKeycloakUserId(String keycloakUserId) { this.keycloakUserId = keycloakUserId; }
}