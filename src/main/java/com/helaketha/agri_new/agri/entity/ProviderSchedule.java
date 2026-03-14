package com.helaketha.agri_new.agri.entity;

import java.time.LocalDate;

public class ProviderSchedule {

    private Integer scheduleId;
    private String providerType;  // 'TRACTOR', 'HARVESTER', or 'FERTILIZER'
    private Integer providerId;
    private LocalDate availableDate;
    private Boolean isBooked;  // FALSE = Available, TRUE = Booked

    public ProviderSchedule() {
    }

    public ProviderSchedule(Integer scheduleId, String providerType, Integer providerId, 
                           LocalDate availableDate, Boolean isBooked) {
        this.scheduleId = scheduleId;
        this.providerType = providerType;
        this.providerId = providerId;
        this.availableDate = availableDate;
        this.isBooked = isBooked;
    }

    public Integer getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(Integer scheduleId) {
        this.scheduleId = scheduleId;
    }

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
}

