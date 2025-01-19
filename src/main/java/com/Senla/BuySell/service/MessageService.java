package com.Senla.BuySell.service;

import com.Senla.BuySell.dto.message.MessageDto;
import com.Senla.BuySell.dto.message.MessageDtoMapper;
import com.Senla.BuySell.model.Message;
import com.Senla.BuySell.model.User;
import com.Senla.BuySell.repository.MessageRepository;
import com.Senla.BuySell.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class MessageService {
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final MessageDtoMapper messageDtoMapper;

    public MessageService(MessageRepository messageRepository,UserRepository userRepository,MessageDtoMapper messageDtoMapper) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
        this.messageDtoMapper = messageDtoMapper;
    }

    public List<MessageDto> getChatHistory(Long senderId, Long receiverId) {
        List<Message> messages = messageRepository.findChatHistory(senderId, receiverId);
        if (messages.isEmpty()) {
            throw new NoSuchElementException("История сообщений между пользователями не найдена.");
        }
        return messageDtoMapper.toDtoList(messages);
    }

    @Transactional
    public void sendMessage(MessageDto messageDto,Long senderId,Long receiverId) {
        User sender = findUserById(senderId, "Отправитель не найден.");
        User receiver = findUserById(receiverId, "Получатель не найден.");

        Message message = new Message(sender, receiver, messageDto.getContent());
        messageRepository.save(message);
    }

    private User findUserById(Long userId, String errorMessage) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException(errorMessage));
    }
}
