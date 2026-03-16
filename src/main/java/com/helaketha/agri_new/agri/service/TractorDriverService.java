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
        KeycloakAdminService.UserProvisioningResult provisioningResult = null;

        try {
            provisioningResult = keycloakAdminService.createOrLinkUserWithRole(
                    d.getUsername(),
                    d.getEmail(),
                    extractFirstName(d.getName()),
                    extractLastName(d.getName()),
                    "TRACTOR_DRIVER"
            );

            d.setKeycloakUserId(provisioningResult.userId());
            int id = dao.insert(d);
            d.setTractorDriverId(id);
            return d;

        } catch (Exception e) {
            if (provisioningResult != null && provisioningResult.created()) {
                try {
                    keycloakAdminService.deleteUser(provisioningResult.userId());
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