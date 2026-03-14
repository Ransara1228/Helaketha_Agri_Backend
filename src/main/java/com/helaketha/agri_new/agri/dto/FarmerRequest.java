package com.helaketha.agri_new.agri.dto;

import com.helaketha.agri_new.agri.entity.Farmer;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class FarmerRequest {

    @NotBlank
    @Size(max = 100)
    private String fullName;

    @NotBlank
    @Pattern(regexp = "\\+?\\d{10,15}")
    private String phone;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(max = 255)
    private String address;

    @NotBlank
    @Pattern(regexp = "^(?:\\d{9}[vVxX]|\\d{12})$")
    private String nic;

    @NotBlank
    @Size(max = 50)
    private String username;

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

    public Farmer toEntity() {
        Farmer farmer = new Farmer();
        farmer.setFullName(fullName);
        farmer.setPhone(phone);
        farmer.setEmail(email);
        farmer.setAddress(address);
        farmer.setNic(nic);
        farmer.setUsername(username);
        return farmer;
    }
}

