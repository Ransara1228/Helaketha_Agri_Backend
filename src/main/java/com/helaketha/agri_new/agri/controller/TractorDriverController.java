package com.helaketha.agri_new.agri.controller;

import com.helaketha.agri_new.agri.entity.TractorDriver;
import com.helaketha.agri_new.agri.service.TractorDriverService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tractor-drivers")
@CrossOrigin
@Validated
public class TractorDriverController {

    private final TractorDriverService service;

    public TractorDriverController(TractorDriverService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<TractorDriver> create(@Valid @RequestBody TractorDriver driver) {
        TractorDriver created = service.create(driver);
        return ResponseEntity.status(201).body(created);
    }

    @GetMapping
    public List<TractorDriver> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TractorDriver> getById(@PathVariable int id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<TractorDriver> update(@PathVariable int id, @Valid @RequestBody TractorDriver driver) {
        TractorDriver updated = service.update(id, driver);
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<TractorDriver> patch(@PathVariable int id, @Valid @RequestBody TractorDriver driver) {
        return service.findById(id).map(existing -> {
            if (driver.getName() != null) {
                existing.setName(driver.getName());
            }
            if (driver.getPhone() != null) {
                existing.setPhone(driver.getPhone());
            }
            if (driver.getMachineQuantity() != null) {
                existing.setMachineQuantity(driver.getMachineQuantity());
            }
            if (driver.getPricePerAcre() != null) {
                existing.setPricePerAcre(driver.getPricePerAcre());
            }
            if (driver.getUsername() != null) {
                existing.setUsername(driver.getUsername());
            }
            // Password is managed by Keycloak, not stored in database
            TractorDriver updated = service.update(id, existing);
            return ResponseEntity.ok(updated);
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        boolean deleted = service.delete(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}