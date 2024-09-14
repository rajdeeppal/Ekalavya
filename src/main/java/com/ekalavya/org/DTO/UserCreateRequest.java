package com.ekalavya.org.DTO;

import lombok.Data;

@Data
public class UserCreateRequest {
    private String username;
    private String password;
    private String domain;
    private String emailId;
    private String requestedRole;
}
