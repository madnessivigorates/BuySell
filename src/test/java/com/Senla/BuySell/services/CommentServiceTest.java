package com.Senla.BuySell.services;

import com.Senla.BuySell.dto.comment.CommentDto;
import com.Senla.BuySell.enums.AdType;
import com.Senla.BuySell.model.Ad;
import com.Senla.BuySell.model.User;
import com.Senla.BuySell.repository.CommentRepository;
import com.Senla.BuySell.service.AdService;
import com.Senla.BuySell.service.CommentService;
import com.Senla.BuySell.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private AdService adService;

    @Mock
    private UserService userService;

    @InjectMocks
    private CommentService commentService;

    private Ad testAd;
    private User testUser;
    private CommentDto testCommentDto;

    @BeforeEach
    void setUp() {
        testUser = new User("testuser", "testnickname", "password123");
        testUser.setId(1L);

        testAd = new Ad("Test Title", AdType.TRANSPORT, "Test Description", 100.0, "Test Location",
                testUser, false, null);
        testAd.setId(1L);

        testCommentDto = new CommentDto();
        testCommentDto.setContent("Тестовый коммент.");
    }

    @Test
    void testSendComment() {
        when(userService.getCurrentUserId()).thenReturn(1L);
        when(userService.findUserById(1L, "Пользователь не найден.")).thenReturn(testUser);
        when(adService.findAdById(1L)).thenReturn(testAd);

        commentService.sendComment(testCommentDto, 1L);

        verify(commentRepository, times(1)).save(argThat(comment -> {
            assertNotNull(comment, "Комментарий не должен быть null");
            assertEquals("Тестовый коммент.", comment.getContent(), "Содержимое комментария должно совпадать");
            assertEquals(testUser, comment.getSender(), "Отправитель комментария должен совпадать");
            assertEquals(testAd, comment.getAd(), "Объявление комментария должно совпадать");
            return true;
        }));
    }
}

