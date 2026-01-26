package com.helaketha.agri_new.agri.controller;

import com.helaketha.agri_new.agri.dto.FarmerPatchRequest;
import com.helaketha.agri_new.agri.dto.FarmerRequest;
import com.helaketha.agri_new.agri.entity.Farmer;
import com.helaketha.agri_new.agri.service.FarmerService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/farmers")
@CrossOrigin
@Validated
public class FarmerController {

    private final FarmerService service;

    public FarmerController(FarmerService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Farmer> addFarmer(@Valid @RequestBody FarmerRequest request) {
        Farmer farmer = request.toEntity();
        // Create farmer: Keycloak user is created first (with auto-generated temporary password), then saved to database
        Farmer created = service.create(farmer);
        return ResponseEntity.status(201).body(created);
    }

    @GetMapping
    public List<Farmer> getFarmers() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Farmer> getFarmerById(@PathVariable int id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Farmer> updateFarmer(@PathVariable int id, @Valid @RequestBody FarmerRequest request) {
        Farmer updated = service.update(id, request.toEntity());
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFarmer(@PathVariable int id) {
        boolean deleted = service.delete(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Farmer> patchFarmer(@PathVariable int id, @Valid @RequestBody FarmerPatchRequest patch) {
        return service.findById(id).map(existing -> {
            patch.applyTo(existing);
            service.update(id, existing);
            return ResponseEntity.ok(existing);
        }).orElse(ResponseEntity.notFound().build());
    }
}
