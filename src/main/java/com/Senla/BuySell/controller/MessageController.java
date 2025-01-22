package com.Senla.BuySell.controller;

import com.Senla.BuySell.dto.message.MessageDto;
import com.Senla.BuySell.dto.views.Views;
import com.Senla.BuySell.service.MessageService;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessageController {
    private final MessageService messageService;

    @Autowired
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/chat/{receiverId}")
    @JsonView(Views.Summary.class)
    public List<MessageDto> getChatHistory(@PathVariable Long receiverId) {
        return messageService.getChatHistory(receiverId);
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PostMapping("/send/{receiverId}")
    public ResponseEntity<String> sendMessage(@RequestBody MessageDto messageDto,
                                              @PathVariable Long receiverId) {
        messageService.sendMessage(messageDto,receiverId);
        return ResponseEntity.status(HttpStatus.CREATED).body("Сообщение успешно отправлено.");
    }
}

