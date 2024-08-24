package com.ekalavya.org.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.ekalavya.org.entity.Role;
import com.ekalavya.org.entity.RoleRequest;
import com.ekalavya.org.entity.User;
import com.ekalavya.org.service.RoleRequestService;
import com.ekalavya.org.service.RoleService;
import com.ekalavya.org.service.UserService;

@RestController
@RequestMapping("/self-service")
public class RoleRequestController {

    @Autowired
    private RoleRequestService roleRequestService;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @CrossOrigin(origins = "http://localhost:3000") // Allow requests from React frontend
    @PostMapping("/submitRoleRequest")
    public ResponseEntity<Map<String, String>> submitRoleRequest(@RequestParam("username") String username,
                                                                 @RequestParam("password") String password,
                                                                 @RequestParam("requestedRole") String requestedRole) {
        Map<String, String> response = new HashMap<>();
        Role role = roleService.getRoleByRolename(requestedRole);
        if (role != null) {
            User user = new User();
            user.setUsername(username);
            user.setPassword(password);
            user.setRole(role);
            userService.save(user);
            RoleRequest roleRequest = new RoleRequest();
            roleRequest.setUser(user);
            roleRequest.setRequestedRole(requestedRole);
            roleRequestService.submitRoleRequest(roleRequest);
            response.put("status", "success");
            response.put("message", "Registration successful!");
            return ResponseEntity.ok(response);
        } else {
            response.put("status", "error");
            response.put("message", "Invalid role requested.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
}