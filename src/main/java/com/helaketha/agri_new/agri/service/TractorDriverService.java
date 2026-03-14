package com.helaketha.agri_new.agri.service;

import com.helaketha.agri_new.agri.repository.TractorDriverRepository;
import com.helaketha.agri_new.agri.entity.TractorDriver;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TractorDriverService {

    private final TractorDriverRepository dao;
    private final KeycloakAdminService keycloakAdminService;

    public TractorDriverService(TractorDriverRepository dao, KeycloakAdminService keycloakAdminService) {
        this.dao = dao;
        this.keycloakAdminService = keycloakAdminService;
    }

    public TractorDriver create(TractorDriver d) {
        String keycloakUserId = null;
        boolean isNewUser = false; // Track if we created a new user or found an existing one

        try {
            // -----------------------------------------------------------
            // STEP 1: Keycloak User Management
            // -----------------------------------------------------------
            try {
                // Try to create a NEW user in Keycloak
                keycloakUserId = keycloakAdminService.createUser(
                        d.getUsername(),
                        d.getEmail(),
                        extractFirstName(d.getName()),
                        extractLastName(d.getName()),
                        "TRACTOR_DRIVER"
                );
                isNewUser = true; // We successfully created a fresh user

            } catch (RuntimeException e) {
                // Handle "User already exists" scenario gracefully
                if (e.getMessage() != null && e.getMessage().contains("already exists")) {
                    System.out.println("User " + d.getUsername() + " exists in Keycloak. Linking to existing account.");

                    // 1. Get existing ID from Keycloak
                    keycloakUserId = keycloakAdminService.getUserIdByUsername(d.getUsername());

                    // 2. Reset Password (so the driver can log in with the new password)
                    String newTempPass = keycloakAdminService.generateTemporaryPassword();
                    keycloakAdminService.updateUserPassword(keycloakUserId, newTempPass);
                    System.out.println("Password reset for existing user: " + d.getUsername());

                    // 3. Ensure they have the correct Role
                    keycloakAdminService.assignRole(keycloakUserId, "TRACTOR_DRIVER");

                    // Note: isNewUser remains false
                } else {
                    // If it's a different error (e.g., connection failed), rethrow it
                    throw e;
                }
            }

            // -----------------------------------------------------------
            // STEP 2: Local Database Save
            // -----------------------------------------------------------
            d.setKeycloakUserId(keycloakUserId);
            int id = dao.insert(d);
            d.setTractorDriverId(id);
            return d;

        } catch (Exception e) {
            // -----------------------------------------------------------
            // ROLLBACK LOGIC
            // -----------------------------------------------------------
            // Only delete from Keycloak if WE created the user in this transaction.
            // If we linked to an existing user, DO NOT delete them if the local DB save fails.
            if (isNewUser && keycloakUserId != null) {
                try {
                    System.err.println("Rolling back created Keycloak user: " + keycloakUserId);
                    keycloakAdminService.deleteUser(keycloakUserId);
                } catch (Exception ex) {
                    System.err.println("Rollback failed: " + ex.getMessage());
                }
            }
            throw new RuntimeException("Failed to register driver: " + e.getMessage(), e);
        }
    }

    public List<TractorDriver> findAll() {
        return dao.findAll();
    }

    public Optional<TractorDriver> findById(int id) {
        return dao.findById(id);
    }

    public TractorDriver update(int id, TractorDriver d) {
        d.setTractorDriverId(id);
        dao.update(d);
        return d;
    }

    public boolean delete(int id) {
        Optional<TractorDriver> opt = dao.findById(id);
        if (opt.isPresent() && opt.get().getKeycloakUserId() != null) {
            try {
                keycloakAdminService.deleteUser(opt.get().getKeycloakUserId());
            } catch (Exception e) {
                System.err.println("Warning: Keycloak delete failed: " + e.getMessage());
            }
        }
        return dao.delete(id) > 0;
    }

    private String extractFirstName(String fullName) {
        return (fullName != null && !fullName.isBlank()) ? fullName.trim().split("\\s+")[0] : "";
    }

    private String extractLastName(String fullName) {
        if (fullName == null || fullName.isBlank()) return "";
        String[] parts = fullName.trim().split("\\s+");
        return parts.length > 1 ? parts[parts.length - 1] : "";
    }
}