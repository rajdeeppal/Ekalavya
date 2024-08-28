package com.ekalavya.org.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Admin {
    @Id
    private String username;

    private String email;
    private String otp;
    private LocalDateTime otpTimestamp;
    private boolean isAlreadyValidated;
}
