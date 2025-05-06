package com.sky.dto;

import lombok.Data;

@Data
public class PinglunDTO {
    private Integer shequid;
    private String details;

    // Getters and Setters
    public Integer getShequid() {
        return shequid;
    }

    public void setShequid(Integer shequid) {
        this.shequid = shequid;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}

