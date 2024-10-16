package com.ekalavya.org.DTO;

import lombok.Data;

@Data
public class UserCreateRequest {
    private String username;
    private String password;
    private String vertical;
    private String component;
    private String emailId;
    private String requestedRole;
}
