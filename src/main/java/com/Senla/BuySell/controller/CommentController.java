package com.Senla.BuySell.controller;

import com.Senla.BuySell.dto.comment.CommentDto;
import com.Senla.BuySell.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/comment")
public class CommentController {
    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ADMIN')")
    @PostMapping("/send/{adId}")
    public ResponseEntity<String> sendComment(@RequestBody CommentDto commentDto,
                                              @PathVariable Long adId) {
        commentService.sendComment(commentDto, adId);
        return ResponseEntity.ok("Комментарий успешно отправлен!");
    }
}

