package com.Senla.BuySell.dto.review;

import com.Senla.BuySell.model.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReviewDtoMapper {
    @Mapping(source = "sender.nickname", target = "senderNickname")
    ReviewDto toDto(Review review);

    Review toEntity(ReviewDto reviewDto);

    List<ReviewDto> toDtoList(List<Review> reviewList);
}
