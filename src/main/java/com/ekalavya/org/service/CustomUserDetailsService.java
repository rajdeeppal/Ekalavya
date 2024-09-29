package com.ekalavya.org.service;

import com.ekalavya.org.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        com.ekalavya.org.entity.User user = userRepository.findByEmployeeId(Long.valueOf(username))
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        if (!"Y".equals(user.getIsActive())) {
            throw new BadCredentialsException("User is not active yet.");
        }
        return new User(String.valueOf(user.getEmpId()), user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority(user.getRole().getName())));
    }
}
