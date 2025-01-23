package com.Senla.BuySell.comparators;

import com.Senla.BuySell.dto.ad.AdDto;

import java.util.Comparator;

public class AdComparators {
    public static final Comparator<AdDto> BY_IS_PROMOTED = Comparator.comparing(AdDto::isPromoted, Comparator.reverseOrder());
    public static final Comparator<AdDto> BY_PRICE_ASC = Comparator.comparing(AdDto::getPrice).thenComparing(BY_IS_PROMOTED);
    public static final Comparator<AdDto> BY_PRICE_DESC = Comparator.comparing(AdDto::getPrice).reversed().thenComparing(BY_IS_PROMOTED);;
    public static final Comparator<AdDto> BY_CITY = Comparator.comparing(AdDto::getLocation).thenComparing(BY_IS_PROMOTED);;
    public static final Comparator<AdDto> BY_CREATED_DATE = Comparator.comparing(AdDto::getCreatedAt).thenComparing(BY_IS_PROMOTED);;
    public static final Comparator<AdDto> BY_CREATED_DATE_DESC = Comparator.comparing(AdDto::getCreatedAt).reversed().thenComparing(BY_IS_PROMOTED);;
    public static final Comparator<AdDto> BY_RATING_DESC = Comparator.comparing(AdDto::getSellerRating).reversed().thenComparing(BY_IS_PROMOTED);;
    public static final Comparator<AdDto> BY_RATING = Comparator.comparing(AdDto::getSellerRating).thenComparing(BY_IS_PROMOTED);;
    public static final Comparator<AdDto> BY_DEFAULT = BY_IS_PROMOTED
            .thenComparing(BY_RATING_DESC)
            .thenComparing(BY_CREATED_DATE_DESC.reversed());
}

