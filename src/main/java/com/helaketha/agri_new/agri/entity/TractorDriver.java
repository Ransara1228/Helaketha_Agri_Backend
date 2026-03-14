package com.helaketha.agri_new.agri.entity;

import java.math.BigDecimal;

public class TractorDriver {

    private Integer tractorDriverId;
    private String name;
    private String phone;
    private Integer machineQuantity;
    private BigDecimal pricePerAcre;
    private String username;
    private String password; // Added this field

    // Default Constructor
    public TractorDriver() {}

    // Getters and Setters
    public Integer getTractorDriverId() {
        return tractorDriverId;
    }

    public void setTractorDriverId(Integer tractorDriverId) {
        this.tractorDriverId = tractorDriverId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getMachineQuantity() {
        return machineQuantity;
    }

    public void setMachineQuantity(Integer machineQuantity) {
        this.machineQuantity = machineQuantity;
    }

    public BigDecimal getPricePerAcre() {
        return pricePerAcre;
    }

    public void setPricePerAcre(BigDecimal pricePerAcre) {
        this.pricePerAcre = pricePerAcre;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}