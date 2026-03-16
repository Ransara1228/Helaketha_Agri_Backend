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
    private final KeycloakAdminService keycloakAdminService;

    public FertilizerSupplierService(FertilizerSupplierRepository dao, KeycloakAdminService keycloakAdminService) {
        this.dao = dao;
        this.keycloakAdminService = keycloakAdminService;
    }

    public FertilizerSupplier create(FertilizerSupplier s) {
        KeycloakAdminService.UserProvisioningResult provisioningResult = null;

        try {
            provisioningResult = keycloakAdminService.createOrLinkUserWithRole(
                    s.getUsername(),
                    s.getEmail(),
                    extractFirstName(s.getName()),
                    extractLastName(s.getName()),
                    "FERTILIZER_SUPPLIER"
            );

            s.setKeycloakUserId(provisioningResult.userId());
            int id = dao.insert(s);
            s.setSupplierId(id);
            return s;

        } catch (Exception e) {
            if (provisioningResult != null && provisioningResult.created()) {
                try {
                    keycloakAdminService.deleteUser(provisioningResult.userId());
                } catch (Exception ex) {
                    System.err.println("Rollback failed: " + ex.getMessage());
                }
            }
            throw new RuntimeException("Failed to create fertilizer supplier: " + e.getMessage(), e);
        }
    }

    public List<FertilizerSupplier> findAll() { return dao.findAll(); }
    public Optional<FertilizerSupplier> findById(int id) { return dao.findById(id); }
    public FertilizerSupplier update(int id, FertilizerSupplier s) { s.setSupplierId(id); dao.update(s); return s; }
    public boolean delete(int id) {
        Optional<FertilizerSupplier> opt = dao.findById(id);
        if (opt.isPresent() && opt.get().getKeycloakUserId() != null) {
            try { keycloakAdminService.deleteUser(opt.get().getKeycloakUserId()); } catch (Exception e) {}
        }
        return dao.delete(id) > 0;
    }

    private String extractFirstName(String name) { return (name != null && !name.isBlank()) ? name.trim().split("\\s+")[0] : ""; }
    private String extractLastName(String name) { if (name == null || name.isBlank()) return ""; String[] p = name.trim().split("\\s+"); return p.length > 1 ? p[p.length - 1] : ""; }
}