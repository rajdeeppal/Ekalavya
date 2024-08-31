package com.ekalavya.org.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.session.HttpSessionEventPublisher;

@Configuration
public class SecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain adminSecurityFilterChain(HttpSecurity http) throws Exception {
            http
            .securityMatchers((matchers) -> matchers.requestMatchers("/admin/**"))
            .authorizeHttpRequests((authz) -> authz
                    .requestMatchers("/admins/**").hasAnyRole("SPWAdmin")
                    .requestMatchers("/admin/login", "/admin/validateOtp", "/admin/sendOtp").permitAll()
                    .anyRequest().authenticated()
            )
            .formLogin((form) -> form
                    .loginPage("/admin/login")
                    .defaultSuccessUrl("/admin/sendOtp", true)
            )
            .logout((logout) -> logout
                    .logoutUrl("/admin/logout")
                    .logoutSuccessUrl("/admin/login?logout")
                    .invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID")
            )
            //.authenticationProvider(otpAuthenticationProvider)
            .sessionManagement(sm -> sm
                .sessionFixation().migrateSession()
                .sessionConcurrency(con -> con
                .maximumSessions(1)
                .sessionRegistry(sessionRegistry())));

        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain userSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatchers((matchers) -> matchers.requestMatchers("/user/**"))
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/users/**").hasAnyRole("SPWUser")
                        .requestMatchers("/user/login").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin((form) -> form
                        .loginPage("/user/login")
                        .defaultSuccessUrl("/users/home", true)
                        .failureUrl("/user/login?error=true")
                )
                .authenticationProvider(daoAuthenticationProvider())
                .logout((logout) -> logout
                        .logoutUrl("/user/logout")
                        .logoutSuccessUrl("/user/login?logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                )
                .sessionManagement(sm -> sm
                    .sessionFixation().migrateSession()
                    .sessionConcurrency(con -> con
                    .maximumSessions(1)
                    .sessionRegistry(sessionRegistry()))
                );

        return http.build();
    }

    @Bean
    SessionRegistry sessionRegistry(){
        return new SessionRegistryImpl();
    }

    @Bean
    HttpSessionEventPublisher httpSessionEventPublisher(){
        return new HttpSessionEventPublisher();
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

}
