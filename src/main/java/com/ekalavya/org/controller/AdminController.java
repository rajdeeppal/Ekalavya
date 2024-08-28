package com.ekalavya.org.controller;

import java.io.IOException;
import java.util.List;

import com.ekalavya.org.DTO.ProjectConfigurationDTO;
import com.ekalavya.org.entity.*;
import com.ekalavya.org.service.*;
import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/admin")
public class AdminController {

    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

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

    @Autowired
    private VerticalService verticalService;

    @Autowired
    private ComponentService componentService;

    @Autowired
    private ActivityService activityService;

    @Autowired
    private TaskService taskService;

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

    @GetMapping("/verticals")
    public ResponseEntity<?> getVerticals() {
        try {
            List<Vertical> verticals = verticalService.findAll();
            return ResponseEntity.ok(verticals);
        } catch (Exception e) {
            logger.error("Error fetching verticals", e);
            return ResponseEntity.status(500).body("Error fetching verticals");
        }
    }

    @GetMapping("/components")
    public ResponseEntity<?> getComponents(@RequestParam String vertical) {
        try {
            Vertical verticalEntity = verticalService.findByName(vertical);
            if (verticalEntity == null) {
                return ResponseEntity.badRequest().body("Vertical not found");
            }
            List<Component> components = componentService.findByVertical(verticalEntity);
            return ResponseEntity.ok(components);
        } catch (Exception e) {
            logger.error("Error fetching components", e);
            return ResponseEntity.status(500).body("Error fetching components");
        }
    }

    @GetMapping("/activities")
    public ResponseEntity<?> getActivities(@RequestParam String component) {
        try {
            Component componentEntity = componentService.findByName(component);
            if (componentEntity == null) {
                return ResponseEntity.badRequest().body("Component not found");
            }
            List<Activity> activities = activityService.findByComponent(componentEntity);
            return ResponseEntity.ok(activities);
        } catch (Exception e) {
            logger.error("Error fetching activities", e);
            return ResponseEntity.status(500).body("Error fetching activities");
        }
    }

    @PostMapping("/configuration/save")
    public ResponseEntity<String> saveConfiguration(@RequestBody ProjectConfigurationDTO projectConfig) {
        try {
            // Process Vertical
            Vertical vertical = verticalService.findByName(projectConfig.getVerticalName());
            if (vertical == null) {
                vertical = new Vertical();
                vertical.setVerticalName(projectConfig.getVerticalName());
                vertical = verticalService.save(vertical);
            }

            // Process Component
            Component component = componentService.findByComponentNameAndVertical(projectConfig.getComponentName(), vertical);
            if (component == null) {
                component = new Component();
                component.setComponentName(projectConfig.getComponentName());
                component.setVertical(vertical);
                component = componentService.save(component);
            }

            // Process Activity
            Activity activity = activityService.findByActivityNameAndComponent(projectConfig.getActivityName(), component);
            if (activity == null) {
                activity = new Activity();
                activity.setActivityName(projectConfig.getActivityName());
                activity.setComponent(component);
                activity = activityService.save(activity);
            }

            // Process Task
            Task task = new Task();
            task.setTaskName(projectConfig.getTaskName());
            task.setUnits(projectConfig.getUnits());
            task.setRatePerUnit(projectConfig.getRatePerUnit());
            task.setActivity(activity);
            taskService.save(task);

            return ResponseEntity.ok("Configuration saved successfully.");
        } catch (Exception e) {
            logger.error("Error saving configuration", e);
            return ResponseEntity.status(500).body("Error saving configuration");
        }
    }
}
