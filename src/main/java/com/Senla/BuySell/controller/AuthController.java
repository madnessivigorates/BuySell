package com.Senla.BuySell.controller;

import com.Senla.BuySell.dto.jwt.JwtRequest;
import com.Senla.BuySell.dto.jwt.JwtResponse;
import com.Senla.BuySell.dto.user.UserDto;
import com.Senla.BuySell.dto.views.Views;
import com.Senla.BuySell.exceptions.AppError;
import com.Senla.BuySell.service.UserService;
import com.Senla.BuySell.utils.JwtTokenUtils;
import com.fasterxml.jackson.annotation.JsonView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
    private static final Logger logger = LogManager.getLogger(AuthController.class);

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
        logger.info("Попытка аутентификации для пользователя: {}", authRequest.username());
        try {
            authenticateUser(authRequest);
            logger.info("Аутентификация успешна для пользователя: {}", authRequest.username());
            UserDetails userDetails = userService.loadUserByUsername(authRequest.username());
            String token = jwtTokenUtils.generateToken(userDetails);
            logger.debug("JWT токен успешно сгенерирован для пользователя: {}", authRequest.username());
            return ResponseEntity.ok(new JwtResponse(token));
        } catch (BadCredentialsException e) {
            logger.error("Ошибка аутентификации для пользователя: {}", authRequest.username(), e);
            return handleAuthenticationError(e);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody @JsonView(Views.UserRegisterOrEdit.class) UserDto userDto) {
        logger.info("Попытка регистрации нового пользователя: {}", userDto.getUsername());
        if (userService.existsByUsername(userDto.getUsername())) {
            logger.warn("Регистрация отменена: пользователь с именем {} уже существует", userDto.getUsername());
            return handleUserAlreadyExistsError();
        }
        try {
            userService.registerNewUser(userDto, "ROLE_USER");
            logger.info("Пользователь {} успешно зарегистрирован", userDto.getUsername());
            return ResponseEntity.status(HttpStatus.CREATED).body("Регистрация прошла успешно");
        } catch (Exception e) {
            logger.error("Ошибка регистрации пользователя: {}", userDto.getUsername(), e);
            return handleRegistrationError(e);
        }
    }

    @PostMapping("/register/admin")
    public ResponseEntity<?> registerAdmin(@RequestBody @JsonView(Views.UserRegisterOrEdit.class) UserDto userDto) {
        logger.info("Попытка регистрации нового администратора: {}", userDto.getUsername());
        if (userService.existsByUsername(userDto.getUsername())) {
            logger.warn("Регистрация администратора отменена: пользователь с именем {} уже существует", userDto.getUsername());
            return handleUserAlreadyExistsError();
        }
        try {
            userService.registerNewUser(userDto, "ROLE_ADMIN");
            logger.info("Администратор {} успешно зарегистрирован", userDto.getUsername());
            return ResponseEntity.status(HttpStatus.CREATED).body("Администратор успешно зарегистрирован");
        } catch (Exception e) {
            logger.error("Ошибка регистрации администратора: {}", userDto.getUsername(), e);
            return handleRegistrationError(e);
        }
    }

    private void authenticateUser(JwtRequest authRequest) {
        logger.debug("Аутентификация пользователя {} в методе authenticateUser", authRequest.username());
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.username(), authRequest.password())
        );
    }

    private ResponseEntity<?> handleAuthenticationError(BadCredentialsException e) {
        logger.warn("Ошибка аутентификации: {}", e.getMessage());
        AppError appError = new AppError(HttpStatus.UNAUTHORIZED.value(), "Неправильный логин или пароль");
        return new ResponseEntity<>(appError, HttpStatus.UNAUTHORIZED);
    }

    private ResponseEntity<?> handleUserAlreadyExistsError() {
        logger.warn("Попытка создать пользователя с уже существующим именем");
        AppError appError = new AppError(HttpStatus.BAD_REQUEST.value(), "Пользователь с таким именем уже существует");
        return new ResponseEntity<>(appError, HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<?> handleRegistrationError(Exception e) {
        logger.error("Ошибка регистрации пользователя: {}", e.getMessage(), e);
        AppError appError = new AppError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Ошибка при регистрации пользователя: " + e.getMessage());
        return new ResponseEntity<>(appError, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}


