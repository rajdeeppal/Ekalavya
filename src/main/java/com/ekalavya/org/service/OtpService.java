package com.ekalavya.org.service;

import com.ekalavya.org.entity.Admin;
import com.ekalavya.org.repository.AdminRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class OtpService {

    @Autowired
    private final AdminRepository adminRepository;

    @Autowired
    private final EmailService emailService;

    private static final Logger logger = LoggerFactory.getLogger(OtpService.class);

    public OtpService(AdminRepository adminRepository, EmailService emailService) {
        this.adminRepository = adminRepository;
        this.emailService = emailService;
    }

    public String generateAndSendOtp(String username) {
        String otp = String.valueOf(new Random().nextInt(999999));
        Admin admin = adminRepository.findByUsername(username);
        if (admin == null) {
            return null;
        }
        admin.setOtp(otp);
        admin.setOtpTimestamp(LocalDateTime.now());
        admin.setAlreadyValidated(false);
        try {
            adminRepository.save(admin);
            emailService.sendOtpEmail(admin.getEmail(), otp);
        } catch (Exception e) {
            logger.info("Unable to send OTP!!");
            return null;
        }
        return admin.getEmail();
    }

    public boolean validateOtp(String username, String otp) {
        Admin admin = adminRepository.findByUsername(username);
        LocalDateTime otpTimestamp = admin.getOtpTimestamp();
        if (admin.getOtp().equals(otp) && otpTimestamp.isAfter(LocalDateTime.now().minusMinutes(10)) && !admin.isAlreadyValidated()) {
            admin.setAlreadyValidated(true);
            return true;
        }
        return false;
    }

    public boolean validateAdmin(String username) {
        Admin admin = adminRepository.findByUsername(username);
        if (admin != null) {
            return true;
        }
        return false;
    }

    public static String getValidMaskedEmail(String email) {
        int atIndex = email.indexOf('@');
        if (atIndex == -1) {
            return email;
        }
        String localPart = email.substring(0, atIndex);
        String domain = email.substring(atIndex);
        StringBuilder maskedLocalPart = new StringBuilder();
        if (localPart.length() > 2) {
            maskedLocalPart.append(localPart.charAt(0));
            for (int i = 1; i < localPart.length() - 1; i++) {
                maskedLocalPart.append('*');
            }
            maskedLocalPart.append(localPart.charAt(localPart.length() - 1));
        } else {
            maskedLocalPart.append(localPart);
        }
        return maskedLocalPart + domain;
    }
}
