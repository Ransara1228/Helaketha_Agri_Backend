package com.helaketha.agri_new.agri.entity;

import java.math.BigDecimal;

public class HarvesterDriver {

    private Integer harvesterDriverId;
    private String name;
    private String phone;
    private Integer availableMachines;
    private BigDecimal pricePerAcre;
    private String username;
    private String password;

    public Integer getHarvesterDriverId() {
        return harvesterDriverId;
    }

    public void setHarvesterDriverId(Integer harvesterDriverId) {
        this.harvesterDriverId = harvesterDriverId;
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

    public Integer getAvailableMachines() {
        return availableMachines;
    }

    public void setAvailableMachines(Integer availableMachines) {
        this.availableMachines = availableMachines;
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
