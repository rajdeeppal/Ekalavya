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
        otpService.generateAndSendOtp(username);
        model.addAttribute("username", username);
        return "adminOtpInput"; // Redirect to OTP input page
    }

    @PostMapping("/admin/validateOtp")
    public String validateOtp(@RequestParam("username") String username,
                              @RequestParam("otp") String otp, Model model) {
        if (otpService.validateOtp(username, otp)) {
            // Redirect to the dashboard after successful OTP validation
            return "redirect:/admin/dashboard";
        } else {
            model.addAttribute("error", "Invalid OTP or OTP expired");
            return "adminOtpInput";
        }
    }
}
