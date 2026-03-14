package com.helaketha.agri_new.agri.service;

import com.helaketha.agri_new.agri.entity.FertilizerSupplier;
import com.helaketha.agri_new.agri.repository.FertilizerSupplierRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class FertilizerSupplierService {

    private final FertilizerSupplierRepository dao;
    private final KeycloakAdminService keycloakAdminService; // Inject Keycloak Service

    public FertilizerSupplierService(FertilizerSupplierRepository dao, KeycloakAdminService keycloakAdminService) {
        this.dao = dao;
        this.keycloakAdminService = keycloakAdminService;
    }

    public FertilizerSupplier create(FertilizerSupplier s) {
        String keycloakUserId = null;
        try {
            // 1. Create User in Keycloak
            // This generates a temporary password automatically
            keycloakUserId = keycloakAdminService.createUser(
                    s.getUsername(),
                    s.getEmail(),
                    extractFirstName(s.getName()),
                    extractLastName(s.getName()),
                    "FERTILIZER_SUPPLIER" // Assign Role
            );

            // 2. Link Keycloak ID to the local entity
            s.setKeycloakUserId(keycloakUserId);

            // 3. Save to Local MySQL Database
            int id = dao.insert(s);
            s.setSupplierId(id);
            return s;

        } catch (Exception e) {
            // Rollback: If DB save fails, delete the user from Keycloak to prevent orphans
            if (keycloakUserId != null) {
                try {
                    keycloakAdminService.deleteUser(keycloakUserId);
                } catch (Exception ex) {
                    System.err.println("Error rolling back Keycloak user: " + ex.getMessage());
                }
            }
            throw new RuntimeException("Failed to create supplier: " + e.getMessage(), e);
        }
    }

    public List<FertilizerSupplier> findAll() {
        return dao.findAll();
    }

    public Optional<FertilizerSupplier> findById(int id) {
        return dao.findById(id);
    }

    public FertilizerSupplier update(int id, FertilizerSupplier s) {
        s.setSupplierId(id);
        dao.update(s);
        return s;
    }

    public boolean delete(int id) {
        // Also delete from Keycloak when deleting from DB
        Optional<FertilizerSupplier> existing = dao.findById(id);
        if (existing.isPresent() && existing.get().getKeycloakUserId() != null) {
            try {
                keycloakAdminService.deleteUser(existing.get().getKeycloakUserId());
            } catch (Exception e) {
                System.err.println("Warning: Could not delete user from Keycloak: " + e.getMessage());
            }
        }
        return dao.delete(id) > 0;
    }

    // Helpers to split full name
    private String extractFirstName(String fullName) {
        return (fullName != null && !fullName.trim().isEmpty()) ? fullName.trim().split("\\s+")[0] : "Supplier";
    }

    private String extractLastName(String fullName) {
        if (fullName == null || fullName.trim().isEmpty()) return "";
        String[] parts = fullName.trim().split("\\s+");
        return parts.length > 1 ? parts[parts.length - 1] : "";
    }
}