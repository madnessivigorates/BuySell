package com.Senla.BuySell.controller;

import com.Senla.BuySell.dto.user.UserDto;
import com.Senla.BuySell.dto.views.Views;
import com.Senla.BuySell.service.UserService;
import com.fasterxml.jackson.annotation.JsonView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private static final Logger logger = LogManager.getLogger(UserController.class);

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    @JsonView(Views.Summary.class)
    public List<UserDto> getAllUsers() {
        logger.info("Запрос на получение списка всех пользователей");
        List<UserDto> users = userService.getAllUsers();
        logger.info("Список всех пользователей успешно получен. Количество пользователей: {}", users.size());
        return users;
    }

    @GetMapping("/get/{id}")
    @JsonView(Views.UserDetailed.class)
    public UserDto getUser(@PathVariable Long id) {
        logger.info("Запрос на получение данных пользователя с ID={}", id);
        UserDto user = userService.getUserById(id);
        logger.info("Данные пользователя с ID={} успешно получены", id);
        return user;
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/my-profile")
    @JsonView(Views.UserPersonal.class)
    public UserDto getUserPersonalProfile() {
        logger.info("Запрос на получение персонального профиля пользователя");
        Long userId = userService.getCurrentUserId();
        UserDto user = userService.getUserById(userId);
        logger.info("Персональный профиль пользователя с ID={} успешно получен", userId);
        return user;
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PutMapping("/my-profile/edit")
    public ResponseEntity<String> editProfile(@RequestBody @JsonView(Views.UserRegisterOrEdit.class) UserDto userDto) {
        logger.info("Запрос на обновление профиля пользователя с ID={}", userDto.getId());
        userService.updateUser(userDto);
        logger.info("Профиль пользователя с ID={} успешно обновлен", userDto.getId());
        return ResponseEntity.ok("Профиль успешно обновлен!");
    }
}


