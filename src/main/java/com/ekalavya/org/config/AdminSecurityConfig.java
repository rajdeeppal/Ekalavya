package com.ekalavya.org.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.session.HttpSessionEventPublisher;

@Configuration
@Order(1)
public class AdminSecurityConfig {

    @Bean
    public SecurityFilterChain securityAdminFilterChain(HttpSecurity http) throws Exception {
        http
/*                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/adminLogin").permitAll()
                        .requestMatchers("/admin/sendOtp").permitAll()
                        .requestMatchers("/admin/validateOtp").permitAll()
                        .requestMatchers("/admin/manageRole").authenticated()
                        .anyRequest().denyAll() // Deny all other requests
                )
                .formLogin(form -> form
                        .loginPage("/adminLogin")
                        .permitAll()
                        .successHandler(adminAuthenticationSuccessHandler())
                        .failureUrl("/adminLogin?error=true")
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/adminLogin?logout")
                        .permitAll()
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                )
                .sessionManagement(session -> session
                        .invalidSessionUrl("/adminLogin?invalid-session=true")
                        .maximumSessions(1)
                        .expiredUrl("/adminLogin?expired=true")
                )*/
                .authorizeHttpRequests(authorize -> authorize
                        .anyRequest().permitAll()
                )
                .csrf(csrf -> csrf.disable());

        return http.build();
    }

    @Bean
    public AuthenticationSuccessHandler adminAuthenticationSuccessHandler() {
        return (HttpServletRequest request, HttpServletResponse response, Authentication authentication) -> {
            response.sendRedirect("/admin/manageRole");
        };
    }

    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }
}
