package com.Senla.BuySell.service;

import com.Senla.BuySell.dto.message.MessageDto;
import com.Senla.BuySell.dto.message.MessageDtoMapper;
import com.Senla.BuySell.model.Message;
import com.Senla.BuySell.model.User;
import com.Senla.BuySell.repository.MessageRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class MessageService {
    private final MessageRepository messageRepository;
    private final MessageDtoMapper messageDtoMapper;
    private final UserService userService;

    @Autowired
    public MessageService(MessageRepository messageRepository,
                          MessageDtoMapper messageDtoMapper,
                          UserService userService) {
        this.messageRepository = messageRepository;
        this.messageDtoMapper = messageDtoMapper;
        this.userService = userService;
    }

    public List<MessageDto> getChatHistory(Long receiverId) {
        Long senderId = userService.getCurrentUserId();
        List<Message> messages = messageRepository.findChatHistory(senderId, receiverId);
        if (messages.isEmpty()) {
            throw new NoSuchElementException("История сообщений между пользователями не найдена.");
        }
        return messageDtoMapper.toDtoList(messages);
    }

    @Transactional
    public void sendMessage(MessageDto messageDto,Long receiverId) {
        Long senderId = userService.getCurrentUserId();
        User sender = userService.findUserById(senderId, "Отправитель не найден.");
        User receiver = userService.findUserById(receiverId, "Получатель не найден.");

        Message message = new Message(sender, receiver, messageDto.getContent());
        messageRepository.save(message);
    }
}

