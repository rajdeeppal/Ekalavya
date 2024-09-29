package com.ekalavya.org.controller;

import com.ekalavya.org.DTO.ProjectDTO;
import com.ekalavya.org.entity.Component;
import com.ekalavya.org.entity.Project;
import com.ekalavya.org.service.ComponentService;
import com.ekalavya.org.service.ProjectService;
import com.ekalavya.org.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UsersController {

    private static final Logger logger = LoggerFactory.getLogger(UsersController.class);

    @Autowired
    private ProjectService projectService;

    @Autowired
    private ComponentService componentService;

    @Autowired
    private UserService userService;


    @GetMapping("/pm/dashboard")
    public String showUserHomePage() {
        return "user/home";  // Renders the user home page (user/home.html)
    }

    @PostMapping("/pm/project/save/{userId}")
    public ResponseEntity<?> addProject(@PathVariable String userId, @RequestBody ProjectDTO projectDTO){
        try {
            Project project = projectService.addProject(userId, projectDTO);
            return ResponseEntity.ok(project);
        } catch (Exception e){
            logger.error("Error creating project", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/pm/projects/{userId}")
    public ResponseEntity<?> getProjects(@PathVariable String userId) {
        try {
            List<Project> projects = projectService.getInprogressProjectsByUserId(userId);
            return ResponseEntity.ok(projects);
        } catch (Exception e) {
            logger.error("Error fetching projects", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error fetching projects");
        }
    }

    @GetMapping("/pm/components")
    public ResponseEntity<?> getComponentsViaProject(@RequestParam String project) {
        try {
            Project projectEntity = projectService.findByName(project);
            if (projectEntity == null) {
                return ResponseEntity.badRequest().body("Vertical not found");
            }
            List<Component> components = componentService.findByVertical(projectEntity.getVertical());
            return ResponseEntity.ok(components);
        } catch (Exception e) {
            logger.error("Error fetching components", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error fetching components");
        }
    }
}
