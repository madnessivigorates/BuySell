package com.Senla.BuySell.controller;

import com.Senla.BuySell.dto.message.MessageDto;
import com.Senla.BuySell.dto.views.Views;
import com.Senla.BuySell.service.MessageService;
import com.fasterxml.jackson.annotation.JsonView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessageController {
    private static final Logger logger = LogManager.getLogger(MessageController.class);

    private final MessageService messageService;

    @Autowired
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/chat/{receiverId}")
    @JsonView(Views.Summary.class)
    public List<MessageDto> getChatHistory(@PathVariable Long receiverId) {
        logger.info("Получение истории чата с пользователем ID={}", receiverId);
        List<MessageDto> chatHistory = messageService.getChatHistory(receiverId);
        logger.info("История чата получена. Количество сообщений: {}", chatHistory.size());
        return chatHistory;
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PostMapping("/send/{receiverId}")
    public ResponseEntity<String> sendMessage(@PathVariable Long receiverId,
                                              @JsonView(Views.MessageCreate.class) @RequestBody MessageDto messageDto) {
        logger.info("Попытка отправки сообщения пользователю ID={}", receiverId);
        messageService.sendMessage(messageDto, receiverId);
        logger.info("Сообщение успешно отправлено пользователю ID={}", receiverId);
        return ResponseEntity.status(HttpStatus.CREATED).body("Сообщение успешно отправлено.");
    }
}



