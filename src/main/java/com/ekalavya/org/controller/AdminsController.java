package com.ekalavya.org.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
@RequestMapping("/admins")
public class AdminsController {

    @GetMapping("/home")
    public String showAdminHomePage() {
        log.info("inside this method !! ");
        return "admin/home";  // Renders the admin home page (admin/home.html)
    }
}
