package com.helaketha.agri_new.agri.service;

import com.helaketha.agri_new.agri.repository.FarmerRepository;
import com.helaketha.agri_new.agri.entity.Farmer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class FarmerService {

    private final FarmerRepository dao;
    private final KeycloakAdminService keycloakAdminService;

    public FarmerService(FarmerRepository dao, KeycloakAdminService keycloakAdminService) {
        this.dao = dao;
        this.keycloakAdminService = keycloakAdminService;
    }

    /**
     * Create farmer: First create user in Keycloak (with auto-generated temporary password),
     * then save to database with Keycloak user ID
     */
    public Farmer create(Farmer f) {
        try {
            // Step 1: Create user in Keycloak (password is auto-generated and temporary)
            String keycloakUserId = keycloakAdminService.createUser(
                    f.getUsername(),
                    f.getEmail(),
                    extractFirstName(f.getFullName()),
                    extractLastName(f.getFullName()),
                    "FARMER" // Assign FARMER role
            );

            // Step 2: Set Keycloak user ID in farmer entity
            f.setKeycloakUserId(keycloakUserId);

            // Step 3: Save farmer to database with Keycloak user ID
            int id = dao.insert(f);
            f.setFarmerId(id);
            return f;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create farmer: " + e.getMessage(), e);
        }
    }

    /**
     * Helper method to extract first name from full name
     */
    private String extractFirstName(String fullName) {
        if (fullName == null || fullName.trim().isEmpty()) {
            return "";
        }
        String[] parts = fullName.trim().split("\\s+");
        return parts.length > 0 ? parts[0] : "";
    }

    /**
     * Helper method to extract last name from full name
     */
    private String extractLastName(String fullName) {
        if (fullName == null || fullName.trim().isEmpty()) {
            return "";
        }
        String[] parts = fullName.trim().split("\\s+");
        if (parts.length > 1) {
            return parts[parts.length - 1];
        }
        return "";
    }

    public List<Farmer> findAll() {
        return dao.findAll();
    }

    public Optional<Farmer> findById(int id) {
        return dao.findById(id);
    }

    public Farmer update(int id, Farmer f) {
        f.setFarmerId(id);
        dao.update(f);
        return f;
    }

    public boolean delete(int id) {
        // Get farmer to retrieve Keycloak user ID
        Optional<Farmer> farmerOpt = dao.findById(id);
        if (farmerOpt.isPresent()) {
            Farmer farmer = farmerOpt.get();
            // Delete from Keycloak first
            if (farmer.getKeycloakUserId() != null && !farmer.getKeycloakUserId().isEmpty()) {
                try {
                    keycloakAdminService.deleteUser(farmer.getKeycloakUserId());
                } catch (Exception e) {
                    // Log error but continue with database deletion
                    System.err.println("Warning: Failed to delete user from Keycloak: " + e.getMessage());
                }
            }
        }
        // Delete from database
        return dao.delete(id) > 0;
    }
}
