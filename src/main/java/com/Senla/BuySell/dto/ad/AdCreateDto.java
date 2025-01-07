package com.Senla.BuySell.dto.ad;

import com.Senla.BuySell.enums.AdType;

public record AdCreateDto(String title, String adType, String description, double price, Long ownerId) {
}
