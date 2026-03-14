package com.helaketha.agri_new.agri.entity;

import java.math.BigDecimal;

public class FertilizerSupplier {

    private Integer supplierId;
    private String name;
    private String phone;
    private String fertilizerType;
    private Integer stockQuantityLiters;
    private BigDecimal pricePerLiter;
    private String username;
    private String password;

    public Integer getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Integer supplierId) {
        this.supplierId = supplierId;
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

    public String getFertilizerType() {
        return fertilizerType;
    }

    public void setFertilizerType(String fertilizerType) {
        this.fertilizerType = fertilizerType;
    }

    public Integer getStockQuantityLiters() {
        return stockQuantityLiters;
    }

    public void setStockQuantityLiters(Integer stockQuantityLiters) {
        this.stockQuantityLiters = stockQuantityLiters;
    }

    public BigDecimal getPricePerLiter() {
        return pricePerLiter;
    }

    public void setPricePerLiter(BigDecimal pricePerLiter) {
        this.pricePerLiter = pricePerLiter;
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
