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
        int atIndex = email.indexOf("@");
        String localPart = email.substring(0, atIndex);
        String domainPart = email.substring(atIndex);

        int visibleChars = getVisibleChars(localPart.length());

        if (visibleChars == 0) {
            return "*".repeat(localPart.length()) + domainPart;
        }

        Random random = new Random();
        char[] masked = new char[localPart.length()];
        int range = localPart.length();
        boolean valid = false;

        while (!valid) {
            // Mask all characters initially
            for (int i = 0; i < masked.length; i++) {
                masked[i] = '*';
            }

            // Randomly select positions for visible characters
            int[] visiblePositions = new int[visibleChars];
            int count = 0;

            while (count < visibleChars) {
                int index = random.nextInt(range);
                if (isValidIndex(visiblePositions, index, count)) {
                    visiblePositions[count] = index;
                    masked[index] = localPart.charAt(index);
                    count++;
                }
            }

            valid = isValidMaskedEmail(masked, visibleChars);
        }

        return new String(masked) + domainPart;
    }

    private static int getVisibleChars(int length) {
        if (length < 4) {
            return 0;
        } else if (length <= 5) {
            return 1;
        } else if (length <= 8) {
            return 2;
        } else if (length <= 12) {
            return 3;
        } else {
            return 4;
        }
    }

    private static boolean isValidIndex(int[] positions, int index, int count) {
        // Ensure there are at least 3 positions between visible characters
        for (int i = 0; i < count; i++) {
            if (Math.abs(positions[i] - index) < 3) {
                return false;
            }
        }
        return true;
    }

    private static boolean isValidMaskedEmail(char[] masked, int visibleChars) {
        int visibleCount = 0;
        for (char c : masked) {
            if (c != '*') {
                visibleCount++;
            }
        }
        return visibleCount == visibleChars;
    }
}
