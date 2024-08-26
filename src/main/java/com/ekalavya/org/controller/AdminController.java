package com.ekalavya.org.controller;

import java.io.IOException;
import java.util.List;

import com.ekalavya.org.service.*;
import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
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

@Controller
@RequestMapping("/admin")
public class AdminController {

	@Autowired
    private RoleRequestService roleRequestService;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleAuditService roleAuditService;
    
    @Autowired
    private RoleService roleService;

    @Autowired
    private EmailService emailService;

    @GetMapping("/manageRoles")
    public String manageRoles(Model model) {
        List<RoleRequest> pendingRequests = roleRequestService.getPendingRequests();
        model.addAttribute("pendingRequests", pendingRequests);
        return "admin/manageRoles";
    }

    @PostMapping("/approveRoleRequest")
    public String approveRoleRequest(@RequestParam("requestId") Long requestId, @RequestParam("approverComments") String approverComments) throws MessagingException, TemplateException, IOException {
        RoleRequest request = roleRequestService.approveRequest(requestId, approverComments);
        User user = request.getUser();
        Role role = roleService.getRoleByRolename(request.getRequestedRole());
        if(role != null ) {
            emailService.sendApprovalEmail(user);
            userService.assignRole(user, request.getRequestedRole());
            roleAuditService.logRoleChange("ASSIGNED", user, role, "Admin");
        }
        return "redirect:/admin/manageRoles";
    }

    @PostMapping("/rejectRoleRequest")
    public String rejectRoleRequest(@RequestParam("requestId") Long requestId, @RequestParam("rejectionComments") String rejectionComments) {
        roleRequestService.rejectRequest(requestId, rejectionComments);
        return "redirect:/admin/manageRoles";
    }
}
