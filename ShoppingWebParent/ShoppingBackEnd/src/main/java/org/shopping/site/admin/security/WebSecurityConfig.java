package org.shopping.site.admin.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.security.SecureRandom;
import java.util.Base64;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    SecurityFilterChain configureHttpSecurity(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login").permitAll() // Permit all access to the login page
                        .anyRequest().authenticated() // All other requests need to be authenticated
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .usernameParameter("email") // Specifies the path to the login page
                        .defaultSuccessUrl("/", true) // Redirect to homepage on successful login
                        .permitAll() // Allow everyone to see the login page. Don't require authentication.
                )
                .logout(logout -> logout
                        .logoutUrl("/logout") // Specify the logout URL
                        .logoutSuccessUrl("/login") // Redirect to login page after logout
                        .invalidateHttpSession(true) // Invalidate session on logout
                        .deleteCookies("JSESSIONID") // Delete cookies on logout
                )
                .rememberMe(rememberMe -> rememberMe
                        .key(generateUniqueAndSecretKey()) // Key to identify remember-me tokens (change this value to a unique and secret key)
                        .tokenValiditySeconds(86400) // Token validity duration (in seconds) - 86400 seconds = 1 day
                );
        return httpSecurity.build();
    }
    private String generateUniqueAndSecretKey() {
        // Generate a random byte array as the secret key
        byte[] secretKey = new byte[32];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(secretKey);

        // Encode the byte array to Base64 for better representation
        return Base64.getEncoder().encodeToString(secretKey);
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public UserDetailsService userDetailsService() {
        return new ShoppingUserDetailsService();
    }
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }
    @Bean
    WebSecurityCustomizer configure() throws Exception {
        return (web -> web.ignoring().requestMatchers("/images/**", "/js/**", "/webjars/**"));
    }
}
