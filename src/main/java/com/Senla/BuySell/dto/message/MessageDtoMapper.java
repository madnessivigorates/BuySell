package com.Senla.BuySell.dto.message;

import com.Senla.BuySell.model.Message;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MessageDtoMapper {
    @Mapping(source = "sender.nickname", target = "senderNickname")
    @Mapping(source = "sender.id", target = "senderId")
    MessageDto toDto(Message message);

    Message toEntity(MessageDto messageDto);

    List<MessageDto> toDtoList(List<Message> messageList);
}
