package com.Senla.BuySell.dto.message;

import java.time.LocalDateTime;

public record MessageDto(String senderNickname,String content,LocalDateTime sentAt) {
}
