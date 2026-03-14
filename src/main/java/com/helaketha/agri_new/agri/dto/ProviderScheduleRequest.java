package com.helaketha.agri_new.agri.dto;

import com.helaketha.agri_new.agri.entity.ProviderSchedule;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;

public class ProviderScheduleRequest {

    @NotBlank(message = "Provider type is required")
    @Pattern(regexp = "TRACTOR|HARVESTER|FERTILIZER", 
             message = "Provider type must be TRACTOR, HARVESTER, or FERTILIZER")
    private String providerType;

    @NotNull(message = "Provider ID is required")
    private Integer providerId;

    @NotNull(message = "Available date is required")
    private LocalDate availableDate;

    private Boolean isBooked = false;  // Default to false (available)

    public String getProviderType() {
        return providerType;
    }

    public void setProviderType(String providerType) {
        this.providerType = providerType;
    }

    public Integer getProviderId() {
        return providerId;
    }

    public void setProviderId(Integer providerId) {
        this.providerId = providerId;
    }

    public LocalDate getAvailableDate() {
        return availableDate;
    }

    public void setAvailableDate(LocalDate availableDate) {
        this.availableDate = availableDate;
    }

    public Boolean getIsBooked() {
        return isBooked;
    }

    public void setIsBooked(Boolean isBooked) {
        this.isBooked = isBooked;
    }

    public ProviderSchedule toEntity() {
        ProviderSchedule schedule = new ProviderSchedule();
        schedule.setProviderType(providerType);
        schedule.setProviderId(providerId);
        schedule.setAvailableDate(availableDate);
        schedule.setIsBooked(isBooked != null ? isBooked : false);
        return schedule;
    }
}

