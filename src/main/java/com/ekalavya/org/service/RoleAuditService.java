package com.ekalavya.org.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ekalavya.org.entity.Role;
import com.ekalavya.org.entity.RoleAudit;
import com.ekalavya.org.entity.User;
import com.ekalavya.org.repository.RoleAuditRepository;

@Service
public class RoleAuditService {

	 @Autowired
	 private RoleAuditRepository roleAuditRepository;

	    public void logRoleChange(String action, User user, Role role, String performedBy) {
	        RoleAudit audit = new RoleAudit();
	        audit.setUser(user);
	        audit.setRole(role);
	        audit.setAction(action);
	        audit.setPerformedBy(performedBy);
	        roleAuditRepository.save(audit);
	    }
}
