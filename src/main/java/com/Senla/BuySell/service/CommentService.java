package com.Senla.BuySell.service;

import com.Senla.BuySell.dto.comment.CommentDto;
import com.Senla.BuySell.model.Ad;
import com.Senla.BuySell.model.Comment;
import com.Senla.BuySell.model.User;
import com.Senla.BuySell.repository.CommentRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final AdService adService;
    private final UserService userService;

    public CommentService(CommentRepository commentRepository, AdService adService, UserService userService) {
        this.commentRepository = commentRepository;
        this.adService = adService;
        this.userService = userService;
    }

    @Transactional
    public void sendComment(CommentDto commentDto, Long adId) {
        Long senderId = userService.getCurrentUserId();
        Ad ad = adService.findAdById(adId);
        User user = userService.findUserById(senderId, "Пользователь не найден.");
        Comment comment = new Comment(commentDto.getContent(), user, ad);

        commentRepository.save(comment);
    }
}

