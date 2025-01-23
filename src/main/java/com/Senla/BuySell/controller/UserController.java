package com.Senla.BuySell.controller;

import com.Senla.BuySell.dto.user.UserDto;
import com.Senla.BuySell.dto.views.Views;
import com.Senla.BuySell.service.UserService;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    @JsonView(Views.Summary.class)
    public List<UserDto> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/get/{id}")
    @JsonView(Views.UserDetailed.class)
    public UserDto getUser(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/my-profile")
    @JsonView(Views.UserPersonal.class)
    public UserDto getUserPersonalProfile() {
        Long userId = userService.getCurrentUserId();
        return userService.getUserById(userId);
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PutMapping("/my-profile/edit")
    public ResponseEntity<String> editProfile(@RequestBody UserDto userDto) {
        userService.updateUser(userDto);
        return ResponseEntity.ok("Профиль успешно обновлен!");
    }
}

