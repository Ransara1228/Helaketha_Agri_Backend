package com.helaketha.agri_new.agri.controller;

import com.helaketha.agri_new.agri.dto.ServiceBookingRequest;
import com.helaketha.agri_new.agri.dto.ServiceStatusUpdateRequest;
import com.helaketha.agri_new.agri.entity.ServiceBooking;
import com.helaketha.agri_new.agri.service.ServiceBookingService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/services")
@CrossOrigin
@Validated
public class ServiceController {

    private final ServiceBookingService service;

    public ServiceController(ServiceBookingService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ServiceBooking> create(@Valid @RequestBody ServiceBookingRequest request) {
        ServiceBooking created = service.save(request.toEntity());
        return ResponseEntity.status(201).body(created);
    }

    @GetMapping
    public List<ServiceBooking> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceBooking> getById(@PathVariable Integer id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/status/{status}")
    public List<ServiceBooking> getByStatus(@PathVariable String status) {
        return service.findByStatus(status);
    }

    @GetMapping("/farmer/{farmerId}")
    public List<ServiceBooking> getByFarmer(@PathVariable Integer farmerId) {
        return service.findByFarmer(farmerId);
    }

    @GetMapping("/date")
    public List<ServiceBooking> getByDate(@RequestParam String date) {
        return service.findByDate(LocalDate.parse(date));
    }

    @GetMapping("/range")
    public List<ServiceBooking> getRange(@RequestParam String start, @RequestParam String end) {
        return service.findByDateRange(LocalDate.parse(start), LocalDate.parse(end));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServiceBooking> update(@PathVariable Integer id,
                                                 @Valid @RequestBody ServiceBookingRequest request) {
        ServiceBooking updated = service.update(id, request.asEntityWithId(id));
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ServiceBooking> patch(@PathVariable Integer id,
                                                @Valid @RequestBody ServiceBookingRequest request) {
        return service.findById(id).map(existing -> {
            if (request.getFarmerId() != null) {
                existing.setFarmerId(request.getFarmerId());
            }
            if (request.getServiceType() != null) {
                existing.setServiceType(request.getServiceType());
            }
            if (request.getProviderId() != null) {
                existing.setProviderId(request.getProviderId());
            }
            if (request.getBookingDate() != null) {
                existing.setBookingDate(request.getBookingDate());
            }
            if (request.getBookingTime() != null) {
                existing.setBookingTime(request.getBookingTime());
            }
            if (request.getTotalCost() != null) {
                existing.setTotalCost(request.getTotalCost());
            }
            if (request.getStatus() != null) {
                existing.setStatus(request.getStatus());
            }
            ServiceBooking updated = service.update(id, existing);
            return ResponseEntity.ok(updated);
        }).orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ServiceBooking> patchStatus(@PathVariable Integer id,
                                                      @Valid @RequestBody ServiceStatusUpdateRequest request) {
        ServiceBooking updated = service.updateStatus(id, request.getStatus());
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        boolean deleted = service.delete(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
