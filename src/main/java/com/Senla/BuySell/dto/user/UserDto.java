package com.Senla.BuySell.dto.user;
import com.Senla.BuySell.dto.ad.AdDto;
import java.util.List;

public record UserDto(Long id, String nickname, double rating, List<AdDto> adList) {
}
