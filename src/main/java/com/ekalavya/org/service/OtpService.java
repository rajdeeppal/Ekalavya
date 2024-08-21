package com.ekalavya.org.service;
import com.ekalavya.org.entity.Admin;
import com.ekalavya.org.repository.AdminRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Random;

@Service
public class OtpService {
    private final AdminRepository adminRepository;
    private final EmailService emailService;

    public OtpService(AdminRepository adminRepository, EmailService emailService) {
        this.adminRepository = adminRepository;
        this.emailService = emailService;
    }

    public void generateAndSendOtp(String username) {
        String otp = String.valueOf(new Random().nextInt(999999));
        Admin admin = adminRepository.findByUsername(username);
        admin.setOtp(otp);
        admin.setOtpTimestamp(LocalDateTime.now());
        adminRepository.save(admin);

        emailService.sendOtpEmail(admin.getEmail(), otp);
    }

    public boolean validateOtp(String username, String otp) {
        Admin admin = adminRepository.findByUsername(username);
        LocalDateTime otpTimestamp = admin.getOtpTimestamp();

        return admin.getOtp().equals(otp) && otpTimestamp.isAfter(LocalDateTime.now().minusMinutes(10));
    }
}
