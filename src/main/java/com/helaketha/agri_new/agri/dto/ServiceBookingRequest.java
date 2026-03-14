package com.helaketha.agri_new.agri.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.helaketha.agri_new.agri.entity.ServiceBooking;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

public class ServiceBookingRequest {

    @NotNull
    @Positive
    private Integer farmerId;

    @NotBlank
    private String serviceType;

    @NotNull
    @Positive
    private Integer providerId;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate bookingDate;

    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime bookingTime;

    private BigDecimal totalCost;

    @Pattern(regexp = "Pending|Accepted|Completed")
    private String status;

    public Integer getFarmerId() {
        return farmerId;
    }

    public void setFarmerId(Integer farmerId) {
        this.farmerId = farmerId;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public LocalDate getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(LocalDate bookingDate) {
        this.bookingDate = bookingDate;
    }

    public Integer getProviderId() {
        return providerId;
    }

    public void setProviderId(Integer providerId) {
        this.providerId = providerId;
    }

    public LocalTime getBookingTime() {
        return bookingTime;
    }

    public void setBookingTime(LocalTime bookingTime) {
        this.bookingTime = bookingTime;
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ServiceBooking toEntity() {
        return new ServiceBooking(
                null,
                farmerId,
                serviceType,
                providerId,
                bookingDate,
                bookingTime,
                totalCost,
                status != null ? status : "Pending"
        );
    }

    public ServiceBooking asEntityWithId(Integer id) {
        ServiceBooking booking = toEntity();
        booking.setBookingId(id);
        return booking;
    }
}

