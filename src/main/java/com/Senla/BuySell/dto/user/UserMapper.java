package com.Senla.BuySell.dto.user;

import com.Senla.BuySell.dto.ad.AdMapper;
import com.Senla.BuySell.model.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring",uses = {AdMapper.class})
public interface UserMapper {
    UserDto toDto(User user);

    User toEntity(UserDto userDto);

    List<UserDto> toDtoList(List<User> userList);
}
