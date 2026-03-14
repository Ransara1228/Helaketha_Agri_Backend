package com.helaketha.agri_new.agri.controller;

import com.helaketha.agri_new.agri.entity.HarvesterDriver;
import com.helaketha.agri_new.agri.service.HarvesterDriverService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/harvester-drivers")
@CrossOrigin
@Validated
public class HarvesterDriverController {

    private final HarvesterDriverService service;

    public HarvesterDriverController(HarvesterDriverService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<HarvesterDriver> create(@Valid @RequestBody HarvesterDriver driver) {
        HarvesterDriver created = service.create(driver);
        return ResponseEntity.status(201).body(created);
    }

    @GetMapping
    public List<HarvesterDriver> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<HarvesterDriver> getById(@PathVariable int id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<HarvesterDriver> update(@PathVariable int id, @Valid @RequestBody HarvesterDriver driver) {
        HarvesterDriver updated = service.update(id, driver);
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<HarvesterDriver> patch(@PathVariable int id, @Valid @RequestBody HarvesterDriver driver) {
        return service.findById(id).map(existing -> {
            if (driver.getName() != null) {
                existing.setName(driver.getName());
            }
            if (driver.getPhone() != null) {
                existing.setPhone(driver.getPhone());
            }
            if (driver.getAvailableMachines() != null) {
                existing.setAvailableMachines(driver.getAvailableMachines());
            }
            if (driver.getPricePerAcre() != null) {
                existing.setPricePerAcre(driver.getPricePerAcre());
            }
            if (driver.getUsername() != null) {
                existing.setUsername(driver.getUsername());
            }
            // Password is managed by Keycloak, not stored in database
            HarvesterDriver updated = service.update(id, existing);
            return ResponseEntity.ok(updated);
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        boolean deleted = service.delete(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}