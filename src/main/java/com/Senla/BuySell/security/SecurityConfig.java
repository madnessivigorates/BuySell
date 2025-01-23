package com.Senla.BuySell.security;

import com.Senla.BuySell.exceptions.CustomAccessDeniedHandler;
import com.Senla.BuySell.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    private final UserService userService;
    private final JwtRequestFilter jwtRequestFilter;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    @Autowired
    public SecurityConfig(@Lazy UserService userService, JwtRequestFilter jwtRequestFilter, CustomAccessDeniedHandler customAccessDeniedHandler) {
        this.userService = userService;
        this.jwtRequestFilter = jwtRequestFilter;
        this.customAccessDeniedHandler = customAccessDeniedHandler;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http
                .cors().and().csrf().disable()
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(authorize ->
                        authorize
                                .requestMatchers("/api/auth").permitAll()
                                .requestMatchers("/api/register").permitAll()
                                .requestMatchers("/api/register/admin").permitAll()
                                .requestMatchers("/api/ads/get/{id}").permitAll()
                                .requestMatchers("/api/ads").permitAll()
                                .requestMatchers("/api/ads/history/{userId}").permitAll()
                                .requestMatchers("/api/users/get/{id}").permitAll()
                                .requestMatchers("/api/users").permitAll()
                                .requestMatchers("/api/reviews/{userId}").permitAll()
                                .anyRequest().authenticated())
                .exceptionHandling()
                .accessDeniedHandler(customAccessDeniedHandler)
                .and()
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();

    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(userService);
        return daoAuthenticationProvider;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception{
        return authenticationConfiguration.getAuthenticationManager();
    }


}

