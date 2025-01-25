package com.Senla.BuySell.controller;

import com.Senla.BuySell.dto.comment.CommentDto;
import com.Senla.BuySell.dto.views.Views;
import com.Senla.BuySell.service.CommentService;
import com.fasterxml.jackson.annotation.JsonView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/comment")
public class CommentController {
    private static final Logger logger = LogManager.getLogger(CommentController.class);
    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ADMIN')")
    @PostMapping("/send/{adId}")
    public ResponseEntity<String> sendComment(@PathVariable Long adId,
                                              @JsonView(Views.CommentCreate.class) @RequestBody CommentDto commentDto) {
        logger.info("Попытка отправки комментария к объявлению с ID={}", adId);
        commentService.sendComment(commentDto, adId);
        logger.info("Комментарий успешно отправлен к объявлению с ID={}", adId);
        return ResponseEntity.ok("Комментарий успешно отправлен!");
    }
}



