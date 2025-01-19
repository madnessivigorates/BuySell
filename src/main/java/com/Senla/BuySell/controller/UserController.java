package com.Senla.BuySell.controller;

import com.Senla.BuySell.dto.review.ReviewDto;
import com.Senla.BuySell.dto.user.UserDto;
import com.Senla.BuySell.dto.views.Views;
import com.Senla.BuySell.model.User;
import com.Senla.BuySell.service.UserService;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/all")
    @JsonView(Views.Summary.class)
    public List<UserDto> getAllUsers(){
        return userService.getAllUsers();
    }

    @GetMapping("/get/{id}")
    @JsonView(Views.UserDetailed.class)
    public ResponseEntity<?> getUser(@PathVariable Long id){
        try {
            UserDto userDto = userService.getUserById(id);
            return ResponseEntity.ok(userDto);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ошибка: " + e.getMessage());
        }
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PutMapping("/edit/{id}")
    public ResponseEntity<?> editProfile(@PathVariable Long id, @RequestBody UserDto userDto){
        try {
            userService.updateUser(id, userDto);
            return ResponseEntity.ok("Профиль успешно обновлен!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Ошибка: " + e.getMessage());
        }
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PostMapping("/{senderId}/{receiverId}/send-review")
    public ResponseEntity<String> addReview(@PathVariable Long senderId, @PathVariable Long receiverId,
                                            @RequestBody ReviewDto reviewDto) {
        userService.addReview(senderId, receiverId, reviewDto.rating(), reviewDto.comment());
        return ResponseEntity.ok("Отзыв успешно оставлен!");
    }

}
