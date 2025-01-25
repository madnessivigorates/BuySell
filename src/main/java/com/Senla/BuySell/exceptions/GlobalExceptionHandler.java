package com.Senla.BuySell.exceptions;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LogManager.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> handleNoSuchElementException(NoSuchElementException ex) {
        logger.error("Ошибка: Ресурс не найден. Подробности: {}", ex.getMessage());
        String detailedMessage = "Ошибка: Ресурс не найден. " +
                "Возможно, вы указали неверный идентификатор или удаленный объект больше не существует. " +
                "Подробности: " + ex.getMessage();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(detailedMessage);
    }

    @ExceptionHandler(OwnershipException.class)
    public ResponseEntity<String> handleOwnershipException(OwnershipException ex) {
        logger.error("Ошибка доступа: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception ex) {
        logger.error("Внутренняя ошибка сервера: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Внутренняя ошибка сервера: " + ex.getMessage());
    }
}


