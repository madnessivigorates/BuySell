package com.Senla.BuySell.services;

import com.Senla.BuySell.dto.review.ReviewDto;
import com.Senla.BuySell.dto.review.ReviewDtoMapper;
import com.Senla.BuySell.model.Review;
import com.Senla.BuySell.model.User;
import com.Senla.BuySell.repository.ReviewRepository;
import com.Senla.BuySell.service.ReviewService;
import com.Senla.BuySell.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private ReviewDtoMapper reviewDtoMapper;

    @Mock
    private UserService userService;

    @InjectMocks
    private ReviewService reviewService;

    private User testSender;
    private User testReceiver;
    private Review testReview;
    private ReviewDto testReviewDto;

    @BeforeEach
    void setUp() {

        testSender = new User("senderUsername", "senderNickname", "password");
        testSender.setId(1L);

        testReceiver = new User("receiverUsername", "receiverNickname", "password");
        testReceiver.setId(2L);

        testReview = new Review(testSender, testReceiver, 5, "Отличный пользователь!");
        testReview.setId(1L);

        testReviewDto = new ReviewDto("Отличный пользователь!", 5);
        testReviewDto.setSenderId(1L);
        testReviewDto.setSenderNickname("senderNickname");
    }

    @Test
    void testAddReview() {
        when(userService.getCurrentUserId()).thenReturn(1L); // Текущий пользователь - отправитель
        when(userService.findUserById(2L, "Получатель не найден")).thenReturn(testReceiver);
        when(userService.findUserById(1L, "Отправитель не найден")).thenReturn(testSender);

        reviewService.addReview(2L, testReviewDto);

        verify(reviewRepository, times(1)).save(any(Review.class));
    }

    @Test
    void testGetReviews() {
        when(userService.findUserById(2L, "Пользователь не найден.")).thenReturn(testReceiver);
        when(reviewRepository.findByReceiver(testReceiver)).thenReturn(Collections.singletonList(testReview));
        when(reviewDtoMapper.toDtoList(Collections.singletonList(testReview)))
                .thenReturn(Collections.singletonList(testReviewDto));

        List<ReviewDto> reviews = reviewService.getReviews(2L);

        assertNotNull(reviews, "Список отзывов не должен быть null.");
        assertEquals(1, reviews.size(), "Список отзывов должен содержать 1 элемент.");
        assertEquals("Отличный пользователь!", reviews.get(0).getComment(), "Комментарий отзыва не совпадает.");

        verify(reviewRepository, times(1)).findByReceiver(testReceiver);
        verify(reviewDtoMapper, times(1)).toDtoList(Collections.singletonList(testReview));
    }

}
