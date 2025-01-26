package com.Senla.BuySell.controller;

import com.Senla.BuySell.dto.ad.AdDto;
import com.Senla.BuySell.dto.removedAd.RemovedAdDto;
import com.Senla.BuySell.dto.views.Views;
import com.Senla.BuySell.service.AdService;
import com.fasterxml.jackson.annotation.JsonView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ads")
public class AdController {

    private static final Logger logger = LogManager.getLogger(AdController.class);

    private final AdService adService;

    @Autowired
    public AdController(AdService adService) {
        this.adService = adService;
    }

    @GetMapping()
    @JsonView(Views.Summary.class)
    public List<AdDto> getAllAds(
            @RequestParam(value = "adType", required = false) String adType,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "sortBy", required = false, defaultValue = "default") String sortBy,
            @RequestParam(value = "ascending", required = false, defaultValue = "true") boolean ascending
    ) {
        logger.info("Получен запрос на получение всех объявлений с параметрами: adType={}, keyword={}, sortBy={}, ascending={}",
                adType, keyword, sortBy, ascending);
        List<AdDto> ads = adService.getAllAds(adType, keyword, sortBy, ascending);
        logger.debug("Возвращено {} объявлений", ads.size());
        return ads;
    }

    @GetMapping("/get/{id}")
    @JsonView(Views.AdDetailed.class)
    public AdDto getAd(@PathVariable Long id) {
        logger.info("Получен запрос на получение объявления с ID={}", id);
        AdDto ad = adService.getAdById(id);
        if (ad != null) {
            logger.debug("Объявление с ID={} найдено", id);
        } else {
            logger.warn("Объявление с ID={} не найдено", id);
        }
        return ad;
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/my")
    @JsonView(Views.AdPersonal.class)
    public List<AdDto> getUsersAd() {
        logger.info("Получен запрос на получение объявлений текущего пользователя");
        List<AdDto> userAds = adService.getUserAds();
        logger.debug("Возвращено {} объявлений пользователя", userAds.size());
        return userAds;
    }

    @GetMapping("/history/{sellerId}")
    @JsonView(Views.Summary.class)
    public List<RemovedAdDto> getUserAdsHistory(@PathVariable Long sellerId) {
        logger.info("Получен запрос на получение истории объявлений продавца с ID={}", sellerId);
        List<RemovedAdDto> history = adService.getUserAdsHistory(sellerId);
        logger.debug("Возвращено {} исторических объявлений продавца с ID={}", history.size(), sellerId);
        return history;
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PostMapping("/promote/{id}")
    public ResponseEntity<String> promoteAd(@PathVariable Long id, @RequestParam long hours) {
        logger.info("Получен запрос на продвижение объявления с ID={} на {} часов", id, hours);
        adService.promoteAd(id, hours);
        logger.debug("Объявление с ID={} успешно продвинуто", id);
        return ResponseEntity.ok("Вы успешно продвинули своё объявление!");
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<String> createAd(@RequestBody @JsonView(Views.AdCreate.class) AdDto adDto) {
        logger.info("Получен запрос на создание нового объявления: {}", adDto);
        adService.createAd(adDto);
        logger.debug("Новое объявление успешно создано");
        return ResponseEntity.status(HttpStatus.CREATED).body("Вы успешно создали своё объявление!");
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PutMapping("/edit/{id}")
    public ResponseEntity<String> updateAd(@PathVariable Long id, @RequestBody @JsonView(Views.AdEdit.class) AdDto adDto) {
        logger.info("Получен запрос на обновление объявления с ID={}: {}", id, adDto);
        adService.updateAd(id, adDto);
        logger.debug("Объявление с ID={} успешно обновлено", id);
        return ResponseEntity.ok("Вы успешно обновили своё объявление!");
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @DeleteMapping("/remove/{id}")
    public ResponseEntity<String> deleteAd(@PathVariable Long id,
                                           @JsonView(Views.RemovedAdCreate.class) @RequestBody RemovedAdDto removedAdDto) {
        logger.info("Получен запрос на удаление объявления с ID={} по данным: {}", id, removedAdDto);
        adService.removeAd(removedAdDto, id);
        logger.debug("Объявление с ID={} успешно удалено", id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Вы успешно сняли с продаж свое объявление!");
    }
}

