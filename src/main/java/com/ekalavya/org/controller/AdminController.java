package com.ekalavya.org.controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import com.ekalavya.org.DTO.AdminLoginRequest;
import com.ekalavya.org.DTO.ProjectConfigurationDTO;
import com.ekalavya.org.DTO.TaskDTO;
import com.ekalavya.org.entity.*;
import com.ekalavya.org.service.*;
import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import static com.ekalavya.org.service.OtpService.getValidMaskedEmail;

@Controller
@Slf4j
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/admin")
public class AdminController {

    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private OtpService otpService;

    @Autowired
    private RoleRequestService roleRequestService;

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

    @GetMapping("/login")
    public String showAdminLoginPage(@ModelAttribute AdminLoginRequest adminLoginRequest, Model model) {
        return "admin/login";
    }

    @PostMapping("/sendOtp")
    public String sendOtp(@ModelAttribute AdminLoginRequest adminLoginRequest, Model model,
                          HttpServletRequest request, HttpServletResponse response) {
        log.info("inside sendotp()");
        boolean isUserAdmin = otpService.validateAdmin(adminLoginRequest.getUsername());
        if (isUserAdmin) {
            String otpEmail = otpService.generateAndSendOtp(adminLoginRequest.getUsername());
            if (otpEmail != null) {
                String maskEmail = getValidMaskedEmail(otpEmail);
                model.addAttribute("username", adminLoginRequest.getUsername());
                model.addAttribute("otpSent", true);
                model.addAttribute("message", "OTP send to: " + maskEmail);
                return "admin/login";
            } else {
                model.addAttribute("error", "Failed to send OTP. Please try again.");
                return "admin/login";
            }
        } else {
            model.addAttribute("error", "Invalid Admin Credential. Please try again.");
            return "admin/login";
        }
    }

    @PostMapping("/validateOtp")
    public String validateOtp(@ModelAttribute("username") String username, @ModelAttribute("otp") String otp, Model model) {
        if (otpService.validateOtp(username, otp)) {
            //return "redirect:/admin/manageRoles";
            return "redirect:/admins/home";
        } else {
            model.addAttribute("username", username);
            model.addAttribute("error", "Invalid OTP or OTP expired");
            model.addAttribute("otpSent", true);
            return "redirect:/admins/home";
        }
    }

    @GetMapping("/logout")
    public String showAdminLogoutPage(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/admin/login?logout"; // Redirects to the admin login page after logout
    }


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
        if (role != null) {
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

    @GetMapping("/tasks")
    public ResponseEntity<?> getTasks(@RequestParam String activity) {
        try {
            Activity activityEntity = activityService.findByName(activity);
            if (activityEntity == null) {
                return ResponseEntity.badRequest().body("Activity not found");
            }
            List<Task> tasks = taskService.findByActivity(activityEntity);
            return ResponseEntity.ok(tasks);
        } catch (Exception e) {
            logger.error("Error fetching tasks", e);
            return ResponseEntity.status(500).body("Error fetching tasks");
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

    @PutMapping("/tasks/{taskId}")
    public ResponseEntity<String> updateTask(@PathVariable Long taskId, @RequestBody TaskDTO taskDTO) {
        taskService.updateTask(taskId, taskDTO);
        return ResponseEntity.ok("Configuration update successfully.");
    }

    @GetMapping("/tasks/{taskId}")
    public ResponseEntity<?> getTaskById(@PathVariable Long taskId) {
        try {
            Optional<Task> task = taskService.getTaskById(taskId);
            if (task.isPresent()) {
                return ResponseEntity.ok(task.get());
            } else {
                return ResponseEntity.badRequest().body("Task not found with id: " + taskId);
            }
        } catch (Exception e) {
            logger.error("Error fetching activities", e);
            return ResponseEntity.status(500).body("Error fetching activities");
        }
    }
}
