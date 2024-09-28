package com.ekalavya.org.DTO;

import lombok.Data;

@Data
public class ValidateOtpRequest {

    private String username;
    private String otp;
}
