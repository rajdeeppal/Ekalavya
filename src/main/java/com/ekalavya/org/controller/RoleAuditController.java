package com.ekalavya.org.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ekalavya.org.entity.Role;
import com.ekalavya.org.entity.RoleAudit;
import com.ekalavya.org.entity.User;
import com.ekalavya.org.repository.RoleAuditRepository;
import com.ekalavya.org.repository.RoleRepository;
import com.ekalavya.org.repository.UserRepository;

@Controller
@RequestMapping("/admin")
public class RoleAuditController {
	    
	    @Autowired
	    private RoleRepository roleRepository;
	    
	    @Autowired
	    private RoleAuditRepository roleAuditRepository;
	    
	    @Autowired
	    private UserRepository userRepository;

	    @GetMapping("/roleAudit")
	    public String auditRole(@RequestParam("searchRole") String roleName, Model model) {
	    	if(!"UNASSIGN".equals(roleName)) {
	        Role role = roleRepository.findByName(roleName);
	        List<User> users = userRepository.findApprovedUsersWithSpecificRole(role, "APPROVED");
	        List<Role> allRoles = roleRepository.findAll();
	        model.addAttribute("users", users);
	        model.addAttribute("role", role);
	        model.addAttribute("allRoles", allRoles);
	    	}
	    	else {
	    		Role role = new Role();
	    		role.setName("UNASSIGN");
	    		List<User> users = userRepository.findByRoleIsNull();
	    		List<Role> allRoles = roleRepository.findAll();
		        model.addAttribute("users", users);
		        model.addAttribute("role", role);
		        model.addAttribute("allRoles", allRoles);
	    	}
	        return "audit/roleAudit"; 
	    }

	    @PostMapping("/revokeRole")
	    public String revokeRole(@RequestParam("userId") Long userId, @RequestParam("roleId") Long roleId, Model model) {
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
	        }
	        return "redirect:/admin/roleAudit?searchRole=" + role.getName();
	    }
	    

	    @PostMapping("/changeRole")
	    public String changeRole(@RequestParam("userId") Long userId, @RequestParam("newRoleId") Long newRoleId, Model model) {
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
	        return "redirect:/admin/roleAudit?searchRole=" + newRole.getName();
	    }
}
