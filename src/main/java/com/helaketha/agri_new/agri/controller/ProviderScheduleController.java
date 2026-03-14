package com.helaketha.agri_new.agri.controller;

import com.helaketha.agri_new.agri.dto.ProviderScheduleRequest;
import com.helaketha.agri_new.agri.entity.ProviderSchedule;
import com.helaketha.agri_new.agri.service.ProviderScheduleService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/provider-schedules")
@CrossOrigin
@Validated
public class ProviderScheduleController {

    private final ProviderScheduleService service;

    public ProviderScheduleController(ProviderScheduleService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ProviderSchedule> createSchedule(@Valid @RequestBody ProviderScheduleRequest request) {
        ProviderSchedule created = service.create(request.toEntity());
        return ResponseEntity.status(201).body(created);
    }

    @GetMapping
    public List<ProviderSchedule> getAllSchedules() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProviderSchedule> getScheduleById(@PathVariable int id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/provider/{providerType}/{providerId}")
    public List<ProviderSchedule> getSchedulesByProvider(
            @PathVariable String providerType,
            @PathVariable Integer providerId) {
        return service.findByProviderTypeAndProviderId(providerType, providerId);
    }

    @GetMapping("/available/{providerType}")
    public List<ProviderSchedule> getAvailableSchedules(
            @PathVariable String providerType,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return service.findAvailableSchedules(providerType, date);
    }

    @GetMapping("/date/{providerType}")
    public List<ProviderSchedule> getSchedulesByDate(
            @PathVariable String providerType,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return service.findByProviderTypeAndDate(providerType, date);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProviderSchedule> updateSchedule(
            @PathVariable int id,
            @Valid @RequestBody ProviderScheduleRequest request) {
        ProviderSchedule updated = service.update(id, request.toEntity());
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/{id}/book")
    public ResponseEntity<ProviderSchedule> bookSchedule(@PathVariable int id) {
        ProviderSchedule booked = service.bookSchedule(id);
        return ResponseEntity.ok(booked);
    }

    @PatchMapping("/{id}/release")
    public ResponseEntity<ProviderSchedule> releaseSchedule(@PathVariable int id) {
        ProviderSchedule released = service.releaseSchedule(id);
        return ResponseEntity.ok(released);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable int id) {
        boolean deleted = service.delete(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}

