package com.Senla.BuySell.dto.ad;

import com.Senla.BuySell.dto.comment.CommentMapper;
import com.Senla.BuySell.enums.AdType;
import com.Senla.BuySell.model.Ad;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring", uses = {CommentMapper.class})
public interface AdMapper {
    @Mapping(source = "user.id", target = "ownerId")
    @Mapping(source = "adType", target = "formatedAdType",qualifiedByName = "formatAdType")
    AdDto toDto(Ad ad);

    Ad toEntity(AdDto adDto);

    List<AdDto> toDtoList(List<Ad> adList);

    @Named("formatAdType")
    default String formatAdType(AdType adType){
      return adType.getDisplayName();
    }
}
