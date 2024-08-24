package com.ekalavya.org.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Admin {

    @Id
    private String username;

    private String email;
    private String otp;
    private LocalDateTime otpTimestamp;
    private boolean isAlreadyValidated;
}
