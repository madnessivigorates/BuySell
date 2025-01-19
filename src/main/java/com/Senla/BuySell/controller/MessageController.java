package com.Senla.BuySell.controller;

import com.Senla.BuySell.dto.message.MessageDto;
import com.Senla.BuySell.dto.views.Views;
import com.Senla.BuySell.service.MessageService;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/messages")
public class MessageController {
    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/chat/{senderId}/{receiverId}")
    @JsonView(Views.Summary.class)
    public ResponseEntity<?> getChatHistory(@PathVariable Long senderId, @PathVariable Long receiverId) {
        try {
            List<MessageDto> chatHistory = messageService.getChatHistory(senderId, receiverId);
            return ResponseEntity.ok(chatHistory);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PostMapping("/send/{senderId}/{receiverId}")
    public ResponseEntity<?>  sendMessage(@RequestBody MessageDto messageDto,
                                          @PathVariable Long senderId,
                                          @PathVariable Long receiverId) {
        try {
            messageService.sendMessage(messageDto, senderId, receiverId);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Сообщение успешно отправлено.");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Произошла ошибка при отправке сообщения.");
        }
    }
}
