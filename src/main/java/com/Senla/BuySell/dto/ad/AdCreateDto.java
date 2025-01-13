package com.Senla.BuySell.dto.ad;

public record AdCreateDto(String title, String adType, String description,
                          double price, Long ownerId, boolean isPromoted, long promotedUntil) {
}
