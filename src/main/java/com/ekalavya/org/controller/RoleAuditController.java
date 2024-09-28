package com.ekalavya.org.controller;

import java.util.List;

import com.ekalavya.org.service.RoleService;
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
	    		users = userRepository.findByIsActiveN();
	    		List<Role> allRoles = roleRepository.findAll();
	    	}
			return ResponseEntity.status(HttpStatus.OK).body(users);
	    }
//
//	    @PostMapping("/revokeRole")
//	    public String revokeRole(@RequestParam("userId") Long userId, @RequestParam("roleId") Long roleId, Model model) {
//	        // Find the user and role
//	        User user = userRepository.findById(userId).orElse(null);
//	        Role role = roleRepository.findById(roleId).orElse(null);
//
//
//	        if (user != null && role != null) {
//	        	RoleAudit roleAudit = roleAuditRepository.findByUser(user);
//	            user.setRole(null);
//	            userRepository.save(user);
//	            if(roleAudit != null ) {
//	            	roleAudit.setAction("REVOKED");
//	            	roleAudit.setPerformedBy("Admin");
//		            roleAuditRepository.save(roleAudit);
//	            }
//	        }
//	        return "redirect:/admin/roleAudit?searchRole=" + role.getName();
//	    }
//
//
//	    @PostMapping("/changeRole")
//	    public String changeRole(@RequestParam("userId") Long userId, @RequestParam("newRoleId") Long newRoleId, Model model) {
//	        User user = userRepository.findById(userId).orElse(null);
//	        Role newRole = roleRepository.findById(newRoleId).orElse(null);
//
//	        if (user != null && newRole != null) {
//	        	RoleAudit roleAudit = roleAuditRepository.findByUser(user);
//	            user.setRole(newRole);
//	            userRepository.save(user);
//
//	           if(roleAudit != null ) {
//	        	   roleAudit.setRole(newRole);
//	        	   roleAudit.setAction("ASSIGNED");
//	        	   roleAuditRepository.save(roleAudit);
//	            }
//	        }
//	        return "redirect:/admin/roleAudit?searchRole=" + newRole.getName();
//	    }
}
