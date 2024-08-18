package com.ekalavya.org.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ekalavya.org.entity.User;
import com.ekalavya.org.service.UserService;

@Controller
public class AuthController {
	@Autowired
    private UserService userService;

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
}
