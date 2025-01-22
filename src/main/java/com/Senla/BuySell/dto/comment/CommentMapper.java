package com.Senla.BuySell.dto.comment;

import com.Senla.BuySell.model.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    @Mapping(source = "sender.id", target = "senderId")
    @Mapping(source = "sender.nickname", target = "senderNickname")
    @Mapping(source = "ad.id", target = "adId")
    CommentDto toDto(Comment comment);

    Comment toEntity(CommentDto commentDto);

    List<CommentDto> toDtoList(List<Comment> Comment);
}
