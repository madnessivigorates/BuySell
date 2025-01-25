package com.Senla.BuySell.controller;

import com.Senla.BuySell.service.AdminService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private static final Logger logger = LogManager.getLogger(CommentController.class);
    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/statistics")
    public Map<String, Object> getStatistics(@RequestParam(required = false) String adType) {
        if (adType != null) {
            logger.info("Получение статистики для объявлений с типом: {}", adType);
        } else {
            logger.info("Получение общей статистики.");
        }
        Map<String, Object> statistics = adminService.getStatistics(adType);
        if (adType != null) {
            logger.info("Статистика для типа '{}': {}", adType, statistics);
        } else {
            logger.info("Общая статистика: {}", statistics);
        }
        return statistics;
    }

}

