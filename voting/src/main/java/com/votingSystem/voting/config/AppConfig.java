package com.votingSystem.voting.config;

import com.votingSystem.voting.repository.VoterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class AppConfig {

    private final VoterRepository voterRepository;

    //    In Java, you can use a lambda to implement a functional interface.
    //    So the lambda here is automatically treated as an implementation
    //    of UserDetailsService.
    @Bean
    public UserDetailsService userDetailsService() {
        return username ->
                voterRepository
                        .findByEmail(username)
                        .orElseThrow(() -> new UsernameNotFoundException("User Not Found"));
    }

    // Data Access Object Responsible to fetch UserDetails and encode password
    // Spring Security uses it when someone tries to log in with username + password.
    // It checks credentials and returns an Authentication object if successful.
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService());
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration  configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
