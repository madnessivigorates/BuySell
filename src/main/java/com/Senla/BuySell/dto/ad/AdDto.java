package com.Senla.BuySell.dto.ad;
import com.Senla.BuySell.dto.comment.CommentDto;

import java.time.LocalDateTime;
import java.util.List;

public record AdDto(Long id, String title, String formatedAdType, String description,
                    double price, Long ownerId, LocalDateTime createdAt, List<CommentDto> commentList) {
}
