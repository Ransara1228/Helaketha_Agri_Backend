package com.helaketha.agri_new.agri.dto;

import com.helaketha.agri_new.agri.entity.Farmer;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class FarmerPatchRequest {

    @Size(max = 100)
    private String fullName;

    @Pattern(regexp = "\\+?\\d{10,15}")
    private String phone;

    @Email
    private String email;

    @Size(max = 255)
    private String address;

    @Pattern(regexp = "^(?:\\d{9}[vVxX]|\\d{12})$")
    private String nic;

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


    public void applyTo(Farmer farmer) {
        if (fullName != null) {
            farmer.setFullName(fullName);
        }
        if (phone != null) {
            farmer.setPhone(phone);
        }
        if (email != null) {
            farmer.setEmail(email);
        }
        if (address != null) {
            farmer.setAddress(address);
        }
        if (nic != null) {
            farmer.setNic(nic);
        }
        if (username != null) {
            farmer.setUsername(username);

        }
    }
}

