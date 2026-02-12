package com.helaketha.agri_new.agri.controller;

import com.helaketha.agri_new.agri.entity.FertilizerSupplier;
import com.helaketha.agri_new.agri.service.FertilizerSupplierService;
import com.helaketha.agri_new.agri.security.UserPrincipal; // Added Import
import jakarta.validation.Valid;
import org.slf4j.Logger; // Added Import
import org.slf4j.LoggerFactory; // Added Import
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal; // Added Import
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fertilizer-suppliers")
@CrossOrigin
@Validated
public class FertilizerSupplierController {

    private final FertilizerSupplierService service;
    private static final Logger log = LoggerFactory.getLogger(FertilizerSupplierController.class); // Added Logger

    public FertilizerSupplierController(FertilizerSupplierService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<FertilizerSupplier> create(@Valid @RequestBody FertilizerSupplier supplier) {
        FertilizerSupplier created = service.create(supplier);
        return ResponseEntity.status(201).body(created);
    }

    @GetMapping
    public List<FertilizerSupplier> getAll(@AuthenticationPrincipal UserPrincipal principal) {
        // This captures the 'sub' (User ID) from Keycloak which matches your database keycloak_user_id
        String userId = principal != null ? principal.getUserId() : null;
        log.info("UserID from principal : {}", userId);
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<FertilizerSupplier> getById(@PathVariable int id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<FertilizerSupplier> update(@PathVariable int id, @Valid @RequestBody FertilizerSupplier supplier) {
        FertilizerSupplier updated = service.update(id, supplier);
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<FertilizerSupplier> patch(@PathVariable int id, @Valid @RequestBody FertilizerSupplier supplier) {
        return service.findById(id).map(existing -> {
            if (supplier.getName() != null) {
                existing.setName(supplier.getName());
            }
            if (supplier.getPhone() != null) {
                existing.setPhone(supplier.getPhone());
            }
            if (supplier.getFertilizerType() != null) {
                existing.setFertilizerType(supplier.getFertilizerType());
            }
            if (supplier.getStockQuantityLiters() != null) {
                existing.setStockQuantityLiters(supplier.getStockQuantityLiters());
            }
            if (supplier.getPricePerLiter() != null) {
                existing.setPricePerLiter(supplier.getPricePerLiter());
            }
            if (supplier.getUsername() != null) {
                existing.setUsername(supplier.getUsername());
            }
            // Password is managed by Keycloak, not stored in database
            FertilizerSupplier updated = service.update(id, existing);
            return ResponseEntity.ok(updated);
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        boolean deleted = service.delete(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}