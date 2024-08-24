package com.ekalavya.org.controller;

import com.ekalavya.org.service.OtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ekalavya.org.entity.User;
import com.ekalavya.org.service.UserService;
import org.springframework.web.bind.annotation.RequestParam;

import static com.ekalavya.org.service.OtpService.getValidMaskedEmail;

@Controller
public class AuthController {
    @Autowired
    private UserService userService;

    @Autowired
    private OtpService otpService;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(User user, String requestedRole) {
        userService.save(user);
        return "redirect:/login?registered";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/adminLogin")
    public String showAdminLoginPage() {
        return "adminLogin";
    }

    @PostMapping("/admin/sendOtp")
    public String sendOtp(@RequestParam("username") String username, Model model) {
        boolean isUserAdmin = otpService.validateAdmin(username);
        if (isUserAdmin) {
            String otpEmail = otpService.generateAndSendOtp(username);
            if (otpEmail != null) {
                String maskEmail = getValidMaskedEmail(otpEmail);
                model.addAttribute("username", username);
                model.addAttribute("otpSent", true);
                model.addAttribute("message", "OTS send to: " + maskEmail);
                return "adminLogin";
            } else {
                model.addAttribute("error", "Failed to send OTP. Please try again.");
                return "adminLogin";
            }
        } else {
            model.addAttribute("error", "Invalid Admin Credential. Please try again.");
            return "adminLogin";
        }
    }


    @PostMapping("/admin/validateOtp")
    public String validateOtp(@RequestParam("username") String username,
                              @RequestParam("otp") String otp, Model model) {
        if (otpService.validateOtp(username, otp)) {
            return "redirect:/admin/manageRoles";
        } else {
            model.addAttribute("username", username);
            model.addAttribute("error", "Invalid OTP or OTP expired");
            model.addAttribute("otpSent", true);
            return "adminLogin";
        }
    }
}
