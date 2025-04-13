package com.sky.dto;

import lombok.Data;

@Data
public class PasswordEditDTO {
    private String empId;
    private String oldPassword;
    private String newPassword;
}