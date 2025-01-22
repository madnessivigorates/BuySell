package com.Senla.BuySell.controller;

import com.Senla.BuySell.dto.review.ReviewDto;
import com.Senla.BuySell.dto.views.Views;
import com.Senla.BuySell.service.ReviewService;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {
    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PostMapping("/{receiverId}")
    public ResponseEntity<String> addReview(@PathVariable Long receiverId,
                                            @RequestBody ReviewDto reviewDto) {
        reviewService.addReview(receiverId, reviewDto);
        return ResponseEntity.ok("Отзыв успешно оставлен!");
    }

    @GetMapping("/{userId}")
    @JsonView(Views.Summary.class)
    public List<ReviewDto> getReviews(@PathVariable Long userId) {
        return reviewService.getReviews(userId);
    }
}

