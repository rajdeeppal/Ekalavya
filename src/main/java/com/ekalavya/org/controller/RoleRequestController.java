package com.ekalavya.org.controller;

import java.util.List;

import com.ekalavya.org.service.EmployeeIdGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ekalavya.org.entity.Role;
import com.ekalavya.org.entity.RoleRequest;
import com.ekalavya.org.entity.User;
import com.ekalavya.org.service.RoleRequestService;
import com.ekalavya.org.service.RoleService;
import com.ekalavya.org.service.UserService;

@Controller
@RequestMapping("/self-service")
public class RoleRequestController {
    @Autowired
    private RoleRequestService roleRequestService;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private EmployeeIdGeneratorService employeeIdGeneratorService;

    @GetMapping("/requestRole")
    public String requestRoleForm(Model model) {
        return "self-service/thankyou";
    }

    @PostMapping("/submitRoleRequest")
    public String submitRoleRequest(@RequestParam("username") String username, @RequestParam("password") String password,
                                    @RequestParam("requestedRole") String requestedRole,
                                    @RequestParam(value = "domain", required = false) String domain,
                                    @RequestParam("emailid") String emailid,
                                    Model model) {
        User user = new User();
        if ("DOMAIN EXPERT".equals(requestedRole)) {
            if (domain == null || domain.isEmpty()) {
                model.addAttribute("error", "Domain is required for DOMAIN EXPERT role.");
                return "registrationForm";
            } else
                user.setDomain(domain);
        }
        Role role = roleService.getRoleByRolename(requestedRole);
        if (role != null) {
            user.setUsername(username);
            user.setPassword(password);
            user.setRole(role);
            if (user.getEmpId() == null) {
                user.setEmpId(employeeIdGeneratorService.generateNextEmpId());
            }
            user.setEmailid(emailid);
            userService.save(user);
            RoleRequest roleRequest = new RoleRequest();
            roleRequest.setUser(user);
            roleRequest.setRequestedRole(requestedRole);
            roleRequestService.submitRoleRequest(roleRequest);
        }
        return "redirect:/self-service/requestRole";
    }

}
