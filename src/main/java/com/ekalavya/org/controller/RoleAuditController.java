package com.ekalavya.org.controller;

import java.util.List;

import com.ekalavya.org.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.ekalavya.org.entity.Role;
import com.ekalavya.org.entity.RoleAudit;
import com.ekalavya.org.entity.User;
import com.ekalavya.org.repository.RoleAuditRepository;
import com.ekalavya.org.repository.RoleRepository;
import com.ekalavya.org.repository.UserRepository;

@RestController
@RequestMapping("/roleAudit")
@Slf4j
public class RoleAuditController {
	    
	    @Autowired
	    private RoleRepository roleRepository;
	    
	    @Autowired
	    private RoleAuditRepository roleAuditRepository;
	    
	    @Autowired
	    private UserRepository userRepository;

		@Autowired
		private RoleService roleService;

		@GetMapping
		public ResponseEntity<?> getRoles(){
			return ResponseEntity.status(HttpStatus.OK).body(roleService.getAllRoles());
		}

	    @GetMapping("/searchRole/{roleName}")
	    public ResponseEntity<?> auditRole(@PathVariable String roleName) {
			List<User> users = null;
	    	if(!"UNASSIGN".equals(roleName)) {
				Role role = roleRepository.findByName(roleName);
				users = userRepository.findApprovedUsersWithSpecificRole(role, "APPROVED");
				List<Role> allRoles = roleService.getAllRoles();
	    	}
	    	else {
	    		Role role = new Role();
	    		role.setName("UNASSIGN");
	    		users = userRepository.findByRoleIsNull();
	    		List<Role> allRoles = roleRepository.findAll();
	    	}
			return ResponseEntity.status(HttpStatus.OK).body(users);
	    }

	    @PostMapping("/revokeRole")
	    public ResponseEntity<?> revokeRole(@RequestParam("userId") Long userId, @RequestParam("roleId") Long roleId) {
			/// for users who are already assigned a role
	        try{
				// Find the user and role
				User user = userRepository.findById(userId).orElse(null);
				Role role = roleRepository.findById(roleId).orElse(null);


				if (user != null && role != null) {
					RoleAudit roleAudit = roleAuditRepository.findByUser(user);
					user.setRole(null);
					userRepository.save(user);
					if(roleAudit != null ) {
						roleAudit.setAction("REVOKED");
						roleAudit.setPerformedBy("Admin");
						roleAuditRepository.save(roleAudit);
					}
					return ResponseEntity.ok().body(String.format("Role Revoked from user %s successfully!", user.getUsername()));
				}
			}catch(Exception e){
				log.info("Exception occurred : {}", e.getMessage());
				return ResponseEntity.ok().body(e.getMessage());
			}
			return null;
	    }


	    @PostMapping("/changeRole")
	    public ResponseEntity<?> changeRole(@RequestParam("userId") Long userId, @RequestParam("newRoleId") Long newRoleId, Model model) {
	        try{
				User user = userRepository.findById(userId).orElse(null);
				Role newRole = roleRepository.findById(newRoleId).orElse(null);

				if (user != null && newRole != null) {
					RoleAudit roleAudit = roleAuditRepository.findByUser(user);
					user.setRole(newRole);
					userRepository.save(user);

					if(roleAudit != null ) {
						roleAudit.setRole(newRole);
						roleAudit.setAction("ASSIGNED");
						roleAuditRepository.save(roleAudit);
					}
				}
				return ResponseEntity.ok().body("Role Successfully Reassigned");
			}catch (Exception e) {
				log.info("Exception occurred : {}", e.getMessage());
				return ResponseEntity.ok().body(e.getMessage());
			}
	    }
}
