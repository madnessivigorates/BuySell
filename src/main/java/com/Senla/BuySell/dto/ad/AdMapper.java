package com.Senla.BuySell.dto.ad;

import com.Senla.BuySell.dto.comment.CommentMapper;
import com.Senla.BuySell.enums.AdType;
import com.Senla.BuySell.model.Ad;
import com.Senla.BuySell.model.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring", uses = {CommentMapper.class})
public interface AdMapper {
    @Mapping(source = "seller.id", target = "sellerId")
    @Mapping(source = "seller.reviewList", target = "sellerRating", qualifiedByName = "averageRating")
    @Mapping(source = "adType", target = "formatedAdType",qualifiedByName = "formatAdType")
    AdDto toDto(Ad ad);

    Ad toEntity(AdDto adDto);

    List<AdDto> toDtoList(List<Ad> adList);

    @Named("formatAdType")
    default String formatAdType(AdType adType){
      return adType.getDisplayName();
    }

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
