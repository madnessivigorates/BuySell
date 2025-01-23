package com.Senla.BuySell.dto.user;

import com.Senla.BuySell.dto.ad.AdMapper;
import com.Senla.BuySell.dto.review.ReviewDtoMapper;
import com.Senla.BuySell.model.Review;
import com.Senla.BuySell.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring",uses = {AdMapper.class, ReviewDtoMapper.class})
public interface UserDtoMapper {
    @Mapping(source = "reviewList",target = "rating", qualifiedByName = "averageRating")
    @Mapping(source = "reviewList",target = "reviews")
    @Mapping(source = "adList",target = "ads")
    UserDto toDto(User user);

    User toEntity(UserDto userDto);

    List<UserDto> toDtoList(List<User> userList);

}
