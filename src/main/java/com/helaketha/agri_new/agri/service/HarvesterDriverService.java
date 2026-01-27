package com.helaketha.agri_new.agri.service;

import com.helaketha.agri_new.agri.entity.HarvesterDriver;
import com.helaketha.agri_new.agri.repository.HarvesterDriverRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class HarvesterDriverService {

    private final HarvesterDriverRepository dao;
    private final KeycloakAdminService keycloakAdminService;

    public HarvesterDriverService(HarvesterDriverRepository dao, KeycloakAdminService keycloakAdminService) {
        this.dao = dao;
        this.keycloakAdminService = keycloakAdminService;
    }

    public HarvesterDriver create(HarvesterDriver d) {
        String keycloakUserId = null;
        try {
            // 1. Create User in Keycloak
            // This assigns "HARVESTER_DRIVER" role and generates a temp password
            keycloakUserId = keycloakAdminService.createUser(
                    d.getUsername(),
                    d.getEmail(),
                    extractFirstName(d.getName()),
                    extractLastName(d.getName()),
                    "HARVESTER_DRIVER"
            );

            // 2. Set the returned Keycloak ID on the entity
            d.setKeycloakUserId(keycloakUserId);

            // 3. Save to the database
            int id = dao.insert(d);
            d.setHarvesterDriverId(id);
            return d;

        } catch (Exception e) {
            // Rollback: If DB save fails, remove the user from Keycloak
            if (keycloakUserId != null) {
                try {
                    keycloakAdminService.deleteUser(keycloakUserId);
                } catch (Exception ex) {
                    System.err.println("Rollback failed: " + ex.getMessage());
                }
            }
            throw new RuntimeException("Failed to create driver: " + e.getMessage(), e);
        }
    }

    public List<HarvesterDriver> findAll() {
        return dao.findAll();
    }

    public Optional<HarvesterDriver> findById(int id) {
        return dao.findById(id);
    }

    public HarvesterDriver update(int id, HarvesterDriver d) {
        d.setHarvesterDriverId(id);
        dao.update(d);
        return d;
    }

    public boolean delete(int id) {
        Optional<HarvesterDriver> opt = dao.findById(id);
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