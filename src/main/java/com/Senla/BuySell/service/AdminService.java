package com.Senla.BuySell.service;

import com.Senla.BuySell.controller.CommentController;
import com.Senla.BuySell.enums.AdType;
import com.Senla.BuySell.repository.AdRepository;
import com.Senla.BuySell.repository.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AdminService {
    private static final Logger logger = LogManager.getLogger(CommentController.class);
    private final UserRepository userRepository;
    private final AdRepository adRepository;

    @Autowired
    public AdminService(UserRepository userRepository, AdRepository adRepository) {
        this.userRepository = userRepository;
        this.adRepository = adRepository;
    }

    public Map<String, Object> getStatistics(String adType) {
        Map<String, Object> statistics = new HashMap<>();
        try {
            addTotalUsers(statistics);
            addAdsStatistics(adType, statistics);
        } catch (Exception e) {
            handleError(statistics, e);
        }

        return statistics;
    }

    private void addTotalUsers(Map<String, Object> statistics) {
        try {
            long totalUsers = userRepository.count();
            statistics.put("totalUsers", totalUsers);
        } catch (Exception e) {
            logger.error("Ошибка при получении количества пользователей", e);
            statistics.put("error", "Не удалось получить количество пользователей.");
        }
    }

    private void addAdsStatistics(String adType, Map<String, Object> statistics) {
        if (adType != null) {
            addAdsByType(adType, statistics);
        } else {
            addTotalAds(statistics);
        }
    }

    private void addAdsByType(String adType, Map<String, Object> statistics) {
        try {
            long adsByType = adRepository.countByAdType(AdType.fromDisplayName(adType));
            statistics.put("adsByType", adsByType);
            statistics.put("adType", adType);
        } catch (IllegalArgumentException e) {
            logger.error("Ошибка при обработке типа объявления: {}", adType, e);
            statistics.put("error", "Некорректный тип объявления: " + adType);
        }
    }

    private void addTotalAds(Map<String, Object> statistics) {
        try {
            long totalAds = adRepository.count();
            statistics.put("totalAds", totalAds);
        } catch (Exception e) {
            logger.error("Ошибка при получении количества объявлений", e);
            statistics.put("error", "Не удалось получить количество объявлений.");
        }
    }

    private void handleError(Map<String, Object> statistics, Exception e) {
        logger.error("Ошибка при получении статистики: ", e);
        statistics.put("error", "Произошла ошибка при получении статистики. Пожалуйста, попробуйте позже.");
    }

}
