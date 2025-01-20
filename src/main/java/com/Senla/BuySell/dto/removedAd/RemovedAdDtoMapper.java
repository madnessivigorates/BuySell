package com.Senla.BuySell.dto.removedAd;
import com.Senla.BuySell.enums.AdType;
import com.Senla.BuySell.enums.ReasonsForSale;
import com.Senla.BuySell.model.RemovedAd;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RemovedAdDtoMapper {
    @Mapping(source = "seller.id", target = "sellerId")
    @Mapping(source = "removedAt", target = "removedAt")
    @Mapping(source = "reason", target = "formatReasonForSale", qualifiedByName = "formatReasonForSale")
    @Mapping(source = "adType", target = "formatedAdType",qualifiedByName = "formatAdType")
    RemovedAdDto toDto(RemovedAd removedAd);

    RemovedAd toEntity(RemovedAdDto removedAdDto);

    List<RemovedAdDto> toDtoList(List<RemovedAd> removedAds);

    @Named("formatReasonForSale")
    default String formatAdType(ReasonsForSale reasonsForSale){
        return reasonsForSale.getDisplayName();
    }

    @Named("formatAdType")
    default String formatAdType(AdType adType){
        return adType.getDisplayName();
    }
}
