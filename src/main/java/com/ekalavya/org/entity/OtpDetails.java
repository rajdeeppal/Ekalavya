package com.ekalavya.org.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "otp_details")
public class OtpDetails {
    @Id
    private String username;

    private String email;
    private String otp;
    private LocalDateTime otpTimestamp;
    private boolean isAlreadyValidated;
}
