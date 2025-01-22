package com.Senla.BuySell.controller;

import com.Senla.BuySell.dto.jwt.JwtRequest;
import com.Senla.BuySell.dto.jwt.JwtResponse;
import com.Senla.BuySell.dto.user.UserDto;
import com.Senla.BuySell.exceptions.AppError;
import com.Senla.BuySell.service.UserService;
import com.Senla.BuySell.utils.JwtTokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
@RequestMapping("/api")
public class AuthController {
    private final UserService userService;
    private final JwtTokenUtils jwtTokenUtils;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthController(UserService userService, JwtTokenUtils jwtTokenUtils, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.jwtTokenUtils = jwtTokenUtils;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/auth")
    public ResponseEntity<?> createAuthToken(@RequestBody JwtRequest authRequest) {
        try {
            authenticateUser(authRequest);
            UserDetails userDetails = userService.loadUserByUsername(authRequest.username());
            String token = jwtTokenUtils.generateToken(userDetails);
            return ResponseEntity.ok(new JwtResponse(token));
        } catch (BadCredentialsException e) {
            return handleAuthenticationError(e);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserDto userDto) {
        if (userService.existsByUsername(userDto.getUsername())) {
            return handleUserAlreadyExistsError();
        }
        try {
            userService.registerNewUser(userDto,"ROLE_USER");
            return ResponseEntity.status(HttpStatus.CREATED).body("Регистрация прошла успешно");
        } catch (Exception e) {
            return handleRegistrationError(e);
        }
    }

    @PostMapping("/register/admin")
    public ResponseEntity<?> registerAdmin(@RequestBody UserDto userDto) {
        if (userService.existsByUsername(userDto.getUsername())) {
            return handleUserAlreadyExistsError();
        }
        try {
            userService.registerNewUser(userDto,"ROLE_ADMIN");
            return ResponseEntity.status(HttpStatus.CREATED).body("Администратор успешно зарегистрирован");
        } catch (Exception e) {
            return handleRegistrationError(e);
        }
    }

    private void authenticateUser(JwtRequest authRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.username(), authRequest.password())
        );
    }

    private ResponseEntity<?> handleAuthenticationError(BadCredentialsException e) {
        AppError appError = new AppError(HttpStatus.UNAUTHORIZED.value(), "Неправильный логин или пароль");
        return new ResponseEntity<>(appError, HttpStatus.UNAUTHORIZED);
    }

    private ResponseEntity<?> handleUserAlreadyExistsError() {
        AppError appError = new AppError(HttpStatus.BAD_REQUEST.value(), "Пользователь с таким именем уже существует");
        return new ResponseEntity<>(appError, HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<?> handleRegistrationError(Exception e) {
        AppError appError = new AppError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Ошибка при регистрации пользователя: " + e.getMessage());
        return new ResponseEntity<>(appError, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

