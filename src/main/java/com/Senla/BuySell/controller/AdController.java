package com.Senla.BuySell.controller;

import com.Senla.BuySell.dto.ad.AdDto;
import com.Senla.BuySell.dto.views.Views;
import com.Senla.BuySell.enums.AdType;
import com.Senla.BuySell.service.AdService;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;


@RestController
@RequestMapping("/api/ads")
public class AdController {
    private final AdService adService;

    @Autowired
    public AdController(AdService adService) {
        this.adService = adService;
    }

    @GetMapping("/all")
    @JsonView(Views.Summary.class)
    public List<AdDto> getAllAds(@RequestParam(value = "adType", required = false) String adType,
                                 @RequestParam(value = "keyword", required = false) String keyword) {
        AdType type = null;
        if (adType != null && !adType.isEmpty()) {
            type = AdType.fromDisplayName(adType);
        }
        return adService.getAllAds(type, keyword);
    }

    @GetMapping("/get/{id}")
    @JsonView(Views.AdDetailed.class)
    public ResponseEntity<?> getAd(@PathVariable Long id) {
        try {
            AdDto adDto = adService.getAdById(id);
            return ResponseEntity.ok(adDto);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка: " + e.getMessage());
        }
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PostMapping("/promote/{id}")
    public ResponseEntity<?> promoteAd(@PathVariable Long id, @RequestParam long hours) {
        adService.promoteAd(id, hours);
        return ResponseEntity.ok("Вы успешно продвинули своё объявление!");
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<AdDto> createAd(@RequestBody AdDto adDto) {
        AdDto createdAdd = adService.createAd(adDto);
        return new ResponseEntity<>(createdAdd, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PutMapping("/edit/{id}")
    public ResponseEntity<AdDto> updateAd(@PathVariable Long id, @RequestBody AdDto adDto) {
        AdDto updatedAd = adService.updateAd(id, adDto);
        return new ResponseEntity<>(updatedAd, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @DeleteMapping("/remove/{id}")
    public ResponseEntity<Void> deleteAd(@PathVariable Long id) {
        adService.deleteAd(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
