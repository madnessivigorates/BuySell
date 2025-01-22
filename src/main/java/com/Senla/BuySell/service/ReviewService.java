package com.Senla.BuySell.service;

import com.Senla.BuySell.dto.review.ReviewDto;
import com.Senla.BuySell.dto.review.ReviewDtoMapper;
import com.Senla.BuySell.model.Review;
import com.Senla.BuySell.model.User;
import com.Senla.BuySell.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ReviewDtoMapper reviewDtoMapper;
    private final UserService userService;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository, ReviewDtoMapper reviewDtoMapper, UserService userService) {
        this.reviewRepository = reviewRepository;
        this.reviewDtoMapper = reviewDtoMapper;
        this.userService = userService;
    }

    @Transactional
    public void addReview(Long receiverId, ReviewDto reviewDto) {
        Long senderId = userService.getCurrentUserId();
        User receiver = userService.findUserById(receiverId, "Получатель не найден");
        User sender = userService.findUserById(senderId, "Отправитель не найден");
        Review review = new Review(sender, receiver, reviewDto.getRating(), reviewDto.getComment());
        reviewRepository.save(review);
    }

    public List<ReviewDto> getReviews(Long userId) {
        User user = userService.findUserById(userId, "Пользователь не найден.");
        return reviewDtoMapper.toDtoList(reviewRepository.findByReceiver(user));
    }
}

