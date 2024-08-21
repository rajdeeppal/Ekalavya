package com.ekalavya.org.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.util.Set;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;

/*    @Bean
    @Order(1)
    public SecurityFilterChain securityUserFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/pm/**").hasRole("PM")
                        .requestMatchers("/ceo/**").hasRole("CEO")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login") // Default login page
                        .loginProcessingUrl("/login")
                        .permitAll()
                        .successHandler(nonAdminAuthenticationSuccessHandler())
                        .failureUrl("/login?error=true")
                )
                .formLogin(form -> form
                        .loginPage("/adminLogin") // Admin login page
                        .loginProcessingUrl("/admin/validateOtp")
                        .permitAll()
                        .successHandler(adminAuthenticationSuccessHandler())
                        .failureUrl("/adminLogin?error=true")
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                )
                .csrf(csrf -> csrf.disable()); // Disable CSRF for simplicity (consider enabling it in production)

        return http.build();
    }*/

    @Bean
    public SecurityFilterChain securityAdminFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/admin/**").authenticated()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/adminLogin") // Admin login page
                        .loginProcessingUrl("/admin/validateOtp")
                        .permitAll()
                        .successHandler(adminAuthenticationSuccessHandler())
                        .failureUrl("/adminLogin?error=true")
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                )
                .csrf(csrf -> csrf.disable()); // Disable CSRF for simplicity (consider enabling it in production)

        return http.build();
    }

    @Bean
    public AuthenticationSuccessHandler nonAdminAuthenticationSuccessHandler() {
        return (HttpServletRequest request, HttpServletResponse response, Authentication authentication) -> {
            Set<SimpleGrantedAuthority> authorities = (Set<SimpleGrantedAuthority>) authentication.getAuthorities();

            if (authorities.contains(new SimpleGrantedAuthority("ROLE_PM"))) {
                response.sendRedirect("/pm/dashboard");
            } else if (authorities.contains(new SimpleGrantedAuthority("ROLE_CEO"))) {
                response.sendRedirect("/ceo/dashboard");
            } else {
                response.sendRedirect("/default");
            }
        };
    }

    @Bean
    public AuthenticationSuccessHandler adminAuthenticationSuccessHandler() {
        return (request, response, authentication) -> {
            response.sendRedirect("/admin/manageRoles");
        };
    }

/*    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }*/

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
