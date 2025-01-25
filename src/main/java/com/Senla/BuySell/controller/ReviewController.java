package com.Senla.BuySell.controller;

import com.Senla.BuySell.dto.review.ReviewDto;
import com.Senla.BuySell.dto.views.Views;
import com.Senla.BuySell.service.ReviewService;
import com.fasterxml.jackson.annotation.JsonView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {
    private static final Logger logger = LogManager.getLogger(ReviewController.class);

    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PostMapping("/{receiverId}")
    public ResponseEntity<String> addReview(@PathVariable Long receiverId,
                                            @JsonView(Views.ReviewCreate.class) @RequestBody ReviewDto reviewDto) {
        logger.info("Попытка добавления отзыва для пользователя с ID={}", receiverId);
        reviewService.addReview(receiverId, reviewDto);
        logger.info("Отзыв успешно добавлен для пользователя с ID={}", receiverId);
        return ResponseEntity.ok("Отзыв успешно оставлен!");
    }

    @GetMapping("/{userId}")
    @JsonView(Views.Summary.class)
    public List<ReviewDto> getReviews(@PathVariable Long userId) {
        logger.info("Получение отзывов для пользователя с ID={}", userId);
        List<ReviewDto> reviews = reviewService.getReviews(userId);
        logger.info("Отзывы успешно получены для пользователя с ID={}. Количество отзывов: {}", userId, reviews.size());
        return reviews;
    }
}


