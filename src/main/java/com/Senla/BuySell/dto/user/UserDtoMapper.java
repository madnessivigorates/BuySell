package com.Senla.BuySell.dto.user;

import com.Senla.BuySell.dto.ad.AdMapper;
import com.Senla.BuySell.model.Review;
import com.Senla.BuySell.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring",uses = {AdMapper.class})
public interface UserDtoMapper {
    @Mapping(source = "reviewList",target = "rating", qualifiedByName = "averageRating")
    UserDto toDto(User user);

    User toEntity(UserDto userDto);

    List<UserDto> toDtoList(List<User> userList);

    @Named("averageRating")
    default double calculateAverageRating(List<Review> reviewList) {
        if (reviewList == null || reviewList.isEmpty()) {
            return 0.0;
        }
        return reviewList.stream()
                .mapToInt(Review::getRating)
                .average()
                .orElse(0.0);
    }
}
