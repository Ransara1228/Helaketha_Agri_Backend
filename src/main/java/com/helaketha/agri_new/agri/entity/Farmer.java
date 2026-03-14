package com.helaketha.agri_new.agri.entity;

public class Farmer {

    private Integer farmerId;
    private String fullName;
    private String phone;
    private String email;
    private String address;
    private String nic;
    private String username;
    private String keycloakUserId; // Keycloak user ID


    public Farmer() { }

    public Farmer(Integer farmerId, String fullName, String phone, String address, String nic) {
        this.farmerId = farmerId;
        this.fullName = fullName;
        this.phone = phone;
        this.address = address;
        this.nic = nic;
    }

    public Integer getFarmerId() {
        return farmerId;
    }

    public void setFarmerId(Integer farmerId) {
        this.farmerId = farmerId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNic() {
        return nic;
    }

    public void setNic(String nic) {
        this.nic = nic;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getKeycloakUserId() {
        return keycloakUserId;
    }

    public void setKeycloakUserId(String keycloakUserId) {
        this.keycloakUserId = keycloakUserId;
    }
}