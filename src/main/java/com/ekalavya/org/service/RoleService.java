package com.ekalavya.org.service;

import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ekalavya.org.entity.Role;
import com.ekalavya.org.entity.User;
import com.ekalavya.org.repository.RoleRepository;

@Service
public class RoleService {

	@Autowired
	RoleRepository roleRepository;
	
	public Role getRoleByRolename(String rolename) {
		Role role = roleRepository.findByName(rolename);
		if(role != null) {
			return role;
		}
		return null;
	}
}
