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

    public LocalDateTime getOtpTimestamp() {
        return otpTimestamp;
    }

    public void setOtpTimestamp(LocalDateTime otpTimestamp) {
        this.otpTimestamp = otpTimestamp;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public boolean isAlreadyValidated() {
        return isAlreadyValidated;
    }

    public void setAlreadyValidated(boolean alreadyValidated) {
        isAlreadyValidated = alreadyValidated;
    }

    @Id
    private String username;

    private String email;
    private String otp;
    private LocalDateTime otpTimestamp;
    private boolean isAlreadyValidated;
}
