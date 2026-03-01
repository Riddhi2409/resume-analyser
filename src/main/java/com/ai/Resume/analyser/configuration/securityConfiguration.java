// securityConfiguration.java
package com.ai.Resume.analyser.configuration;

import com.ai.Resume.analyser.service.failureHandler;
import com.ai.Resume.analyser.service.successHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class securityConfiguration {

    @Autowired
    private entryPointService userDetails;

    @Autowired
    private successHandler successHandler;

    @Autowired
    private failureHandler failureHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers(
                                "/resumeAnalyser/entry/v1/**",
                                "/",
                                "/login",
                                "/forgotpassword",
                                "/static/**",
                                "/index.html",
                                "/manifest.json",
                                "/assets/**")
                        .permitAll()
                        .anyRequest().authenticated())
                .cors(Customizer.withDefaults())                 // uses WebConfig CORS
                .csrf(AbstractHttpConfigurer::disable)          // disable CSRF for API POSTs
                .logout(AbstractHttpConfigurer::disable)
                .formLogin(form -> form
                        .loginPage("/login")
                        .successHandler(successHandler)
                        .failureHandler(failureHandler)
                        .permitAll())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(org.springframework.security.config.http.SessionCreationPolicy.IF_REQUIRED)) // STATEFUL sessions
                .build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider(userDetails);
        authenticationProvider.setPasswordEncoder(new BCryptPasswordEncoder(12));
        return authenticationProvider;
    }
}
