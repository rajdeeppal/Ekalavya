package com.ekalavya.org.DTO;

import lombok.Data;

@Data
public class UserCreateRequest {
    private String username;
    private String empId;
    private String domain;
    private String emailId;
    private String role;
}