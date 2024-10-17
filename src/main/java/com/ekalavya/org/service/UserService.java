package com.ekalavya.org.service;

import java.util.List;
import java.util.Optional;

import com.ekalavya.org.repository.RoleRequestRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ekalavya.org.entity.User;
import com.ekalavya.org.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private RoleService roleService;

    @Autowired
    private UserDetailsService userDetailsService;

	@Autowired
	private RoleRequestRepository roleRequestRepository;

    private static final Logger logger = LoggerFactory.getLogger(RoleAuditService.class);

	public User save(User user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		return userRepository.save(user);
	}

	public User updateUser(User user){
		return userRepository.save(user);
	}

	public User findByUsername(String username) {
		Optional<User> user = userRepository.findByUsername(username);
		if(user.isPresent()) {
			return user.get();
		}
		return new User();
	}

	public void assignRole(User user, String rolename) {
		Optional<User> users = userRepository.findById(user.getId());
		if(users.isPresent() && rolename.equals(user.getRole().getName())) {
			 logger.info("Ask Role is given to userId: {}, username: {}", user.getId(), user.getUsername());
		}else{
			logger.info("Missmatch in Role for userId: {}, username: {}", user.getId(), user.getUsername());
		}

	}

	public User findByEmplId(String id) {
		User user = userRepository.findByEmployeeId(Long.parseLong(id)).orElseThrow(() -> new RuntimeException("Employee not found"));
		if(user != null ) {
			return user;
		}
		logger.info("Not a active user !! ");
		return new User();
	}

	public void removeUserById(Long id) {
		boolean userExist = userRepository.existsById(id);
		Optional<User> user = userRepository.findById(id);
		if(userExist) {
			userRepository.deleteById(id);
			logger.info("Role request is rejected for user: {}", user.get().getUsername());
		}
		else
			logger.warn("User not exist in our Database", user.get().getUsername());
	}

	public List<User> getSkilledDomainExpert(String componentName, String verticalName) {
		return userRepository.findDomainExpertByComponentAndVertical(componentName, verticalName);
	}
}
