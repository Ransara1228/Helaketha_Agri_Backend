package com.helaketha.agri_new.agri.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class ServiceStatusUpdateRequest {

    @NotBlank
    @Pattern(regexp = "Pending|Accepted|Completed")
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

