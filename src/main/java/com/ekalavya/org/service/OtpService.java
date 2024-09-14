package com.ekalavya.org.service;

import com.ekalavya.org.entity.OtpDetails;
import com.ekalavya.org.entity.Role;
import com.ekalavya.org.entity.User;
import com.ekalavya.org.repository.OtpRepository;
import com.ekalavya.org.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class OtpService {

    @Autowired
    private OtpRepository otpRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(OtpService.class);

    public String generateAndSendOtp(String username) {
        String otp = String.valueOf(new Random().nextInt(999999));

        Optional<User> user = userRepository.findByUsername(username);

        OtpDetails otpDbObject = new OtpDetails();
        otpDbObject.setUsername(username);
        otpDbObject.setEmail(user.get().getEmailid());
        otpDbObject.setOtp(otp);
        otpDbObject.setOtpTimestamp(LocalDateTime.now());
        otpDbObject.setAlreadyValidated(false);
        try {
            otpRepository.save(otpDbObject);
            emailService.sendOtpEmail(otpDbObject.getEmail(), otp);
        } catch (Exception e) {
            logger.info("Unable to send OTP!!");
            return null;
        }
        return otpDbObject.getEmail();
    }

    public boolean validateOtp(String username, String otp) {
        OtpDetails otpDetails = otpRepository.findByUsername(username);
        LocalDateTime otpTimestamp = otpDetails.getOtpTimestamp();
        if (otpDetails.getOtp().equals(otp) && otpTimestamp.isAfter(LocalDateTime.now().minusMinutes(10)) && !otpDetails.isAlreadyValidated()) {
            otpDetails.setAlreadyValidated(true);
            return true;
        }
        return false;
    }

    public boolean validateAdmin(String username) {
        Optional<User> userObject = userRepository.findByUsername(username);
        if (userObject.isPresent()) {
            Role userRole  = userObject.get().getRole();
            return "SPWAdmin".equals(userRole.getName());
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
