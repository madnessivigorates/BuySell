package com.Senla.BuySell.service;

import com.Senla.BuySell.dto.comment.CommentDto;
import com.Senla.BuySell.model.Ad;
import com.Senla.BuySell.model.Comment;
import com.Senla.BuySell.model.User;
import com.Senla.BuySell.repository.AdRepository;
import com.Senla.BuySell.repository.CommentRepository;
import com.Senla.BuySell.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final AdRepository adRepository;
    private final UserRepository userRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository, AdRepository adRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.adRepository = adRepository;
        this.userRepository = userRepository;
    }

    public void sendComment(CommentDto commentDto, Long adId, Long userId) {
        Ad ad = adRepository.findById(adId)
                .orElseThrow(() -> new NoSuchElementException("Не удалось найти объявление."));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("Не удалось найти пользователя."));
        Comment comment = new Comment(commentDto.getContent(),user,ad);
        try {
            commentRepository.save(comment);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка отправки сообщения. " + e.getMessage());
        }
    }

}
