package com.ekalavya.org;

import com.ekalavya.org.entity.Role;
import com.ekalavya.org.entity.User;
import com.ekalavya.org.repository.RoleRepository;
import com.ekalavya.org.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class AkalavyaApplication {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;

	public static void main(String[] args) {
		SpringApplication.run(AkalavyaApplication.class, args);
	}

//	@Bean
//	public CommandLineRunner initData() {
//		return args -> {
//			// Create a new Role (ensure you have a Role entity and repository)
//			Role adminRole = new Role();
//			adminRole.setName("SPWAdmin");
//			roleRepository.save(adminRole);
//
//			Role userRole = new Role();
//			userRole.setName("SPWUser");
//			roleRepository.save(userRole);
//
//
//			// Create a new User
//			User user = new User();
//			user.setUsername("debjit31");
//			user.setPassword(new BCryptPasswordEncoder().encode("password")); // Encrypt the password
//			user.setEmailid("debjit16.dc@gmail.com");
//			user.setDomain("domain1");
//			user.setRole(userRole);
//			user.setEmpId(1898635L);
//
//			User adminUser = new User();
//			adminUser.setUsername("admin");
//			adminUser.setPassword(new BCryptPasswordEncoder().encode("password")); // Encrypt the password
//			adminUser.setEmailid("debjit16.dc@gmail.com");
//			adminUser.setDomain("domain1");
//			adminUser.setRole(adminRole);
//			adminUser.setEmpId(1898636L);
//
//			// Save the User
//			userRepository.save(user);
//			userRepository.save(adminUser);
//		};
//	}

}
