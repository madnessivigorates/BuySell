package com.Senla.BuySell.controller;

import com.Senla.BuySell.dto.comment.SendCommentDto;
import com.Senla.BuySell.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/comment")
public class CommentController {
    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ADMIN')")
    @PostMapping("/send/{adId}/{userId}")
    public ResponseEntity<?> sendComment(@RequestBody SendCommentDto sendCommentDto,
                                         @PathVariable("adId") Long adId,
                                         @PathVariable("userId") Long userId) {
        try {
            commentService.sendComment(sendCommentDto, adId, userId);
            return ResponseEntity.ok("Комментарий успешно отправлен!");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка: " + e.getMessage());
        }
    }

}
