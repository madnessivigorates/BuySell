package com.Senla.BuySell.services;

import com.Senla.BuySell.dto.message.MessageDto;
import com.Senla.BuySell.dto.message.MessageDtoMapper;
import com.Senla.BuySell.model.Message;
import com.Senla.BuySell.model.User;
import com.Senla.BuySell.repository.MessageRepository;
import com.Senla.BuySell.service.MessageService;
import com.Senla.BuySell.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MessageServiceTest {

    @Mock
    private MessageRepository messageRepository;

    @Mock
    private MessageDtoMapper messageDtoMapper;

    @Mock
    private UserService userService;

    @InjectMocks
    private MessageService messageService;

    private User testSender;
    private User testReceiver;
    private Message testMessage;
    private MessageDto testMessageDto;

    @BeforeEach
    void setUp() {
        testSender = new User("sender", "SenderNickname", "password123");
        testSender.setId(1L);

        testReceiver = new User("receiver", "ReceiverNickname", "password456");
        testReceiver.setId(2L);

        testMessage = new Message(testSender, testReceiver, "Тестовое сообщение.");
        testMessageDto = new MessageDto("Тестовое сообщение.");
    }

    @Test
    void testGetChatHistory() {
        when(userService.getCurrentUserId()).thenReturn(1L);
        when(messageRepository.findChatHistory(1L, 2L)).thenReturn(List.of(testMessage));
        when(messageDtoMapper.toDtoList(List.of(testMessage))).thenReturn(List.of(testMessageDto));

        List<MessageDto> chatHistory = messageService.getChatHistory(2L);

        assertNotNull(chatHistory, "История сообщений должная быть null");
        assertEquals(1, chatHistory.size(), "Размер списка сообщений должен быть 1");
        assertEquals("Тестовое сообщение.", chatHistory.get(0).getContent(), "Текс в сообщении должен совпадать");

        verify(messageRepository, times(1)).findChatHistory(1L, 2L);
    }

    @Test
    void testGetChatHistory_NotFound() {
        when(userService.getCurrentUserId()).thenReturn(1L);
        when(messageRepository.findChatHistory(1L, 2L)).thenReturn(List.of());

        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () ->
                messageService.getChatHistory(2L));

        assertEquals("История сообщений между пользователями не найдена.", exception.getMessage());
    }

    @Test
    void testSendMessage() {
        when(userService.getCurrentUserId()).thenReturn(1L);
        when(userService.findUserById(1L, "Отправитель не найден.")).thenReturn(testSender);
        when(userService.findUserById(2L, "Получатель не найден.")).thenReturn(testReceiver);

        messageService.sendMessage(testMessageDto, 2L);

        verify(messageRepository, times(1)).save(argThat(message -> {
            assertNotNull(message, "Сообщение не должно быть null");
            assertEquals("Тестовое сообщение.", message.getContent(), "Текс в сообщении должен совпадать");
            assertEquals(testSender, message.getSender(), "Отправитель должен совпадать");
            assertEquals(testReceiver, message.getReceiver(), "Получатель должен совпадать");
            return true;
        }));
    }
}