package com.helaketha.agri_new.agri.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

public class ServiceBooking {

    private Integer bookingId;
    private Integer farmerId;
    private String serviceType;     // e.g. "Tractor Service"
    private Integer providerId;     // The ID of the driver or supplier chosen
    private LocalDate bookingDate;
    private LocalTime bookingTime;
    private BigDecimal totalCost;
    private String status;          // Pending, Accepted, Completed

    public ServiceBooking() { }

    public ServiceBooking(Integer bookingId, Integer farmerId, String serviceType, Integer providerId,
                          LocalDate bookingDate, LocalTime bookingTime, BigDecimal totalCost, String status) {
        this.bookingId = bookingId;
        this.farmerId = farmerId;
        this.serviceType = serviceType;
        this.providerId = providerId;
        this.bookingDate = bookingDate;
        this.bookingTime = bookingTime;
        this.totalCost = totalCost;
        this.status = status;
    }

    public Integer getBookingId() { return bookingId; }
    public void setBookingId(Integer bookingId) { this.bookingId = bookingId; }

    public Integer getFarmerId() { return farmerId; }
    public void setFarmerId(Integer farmerId) { this.farmerId = farmerId; }

    public String getServiceType() { return serviceType; }
    public void setServiceType(String serviceType) { this.serviceType = serviceType; }

    public Integer getProviderId() { return providerId; }
    public void setProviderId(Integer providerId) { this.providerId = providerId; }

    public LocalDate getBookingDate() { return bookingDate; }
    public void setBookingDate(LocalDate bookingDate) { this.bookingDate = bookingDate; }

    public LocalTime getBookingTime() { return bookingTime; }
    public void setBookingTime(LocalTime bookingTime) { this.bookingTime = bookingTime; }

    public BigDecimal getTotalCost() { return totalCost; }
    public void setTotalCost(BigDecimal totalCost) { this.totalCost = totalCost; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
