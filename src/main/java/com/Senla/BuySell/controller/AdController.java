package com.Senla.BuySell.controller;

import com.Senla.BuySell.dto.ad.AdDto;
import com.Senla.BuySell.dto.removedAd.RemovedAdDto;
import com.Senla.BuySell.dto.views.Views;
import com.Senla.BuySell.service.AdService;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ads")
public class AdController {
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
        return adService.getAllAds(adType, keyword, sortBy, ascending);
    }


    @GetMapping("/get/{id}")
    @JsonView(Views.AdDetailed.class)
    public AdDto getAd(@PathVariable Long id) {
        return adService.getAdById(id);
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/my")
    @JsonView(Views.AdPersonal.class)
    public List<AdDto> getUsersAd() {
        return adService.getUserAds();
    }

    @GetMapping("/history/{sellerId}")
    @JsonView(Views.Summary.class)
    public List<RemovedAdDto> getUserAdsHistory(@PathVariable Long sellerId) {
        return adService.getUserAdsHistory(sellerId);
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PostMapping("/promote/{id}")
    public ResponseEntity<String> promoteAd(@PathVariable Long id, @RequestParam long hours) {
        adService.promoteAd(id, hours);
        return ResponseEntity.ok("Вы успешно продвинули своё объявление!");
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<String> createAd(@RequestBody AdDto adDto) {
        adService.createAd(adDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("Вы успешно создали своё объявление!");
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PutMapping("/edit/{id}")
    public ResponseEntity<String> updateAd(@PathVariable Long id, @RequestBody AdDto adDto) {
        adService.updateAd(id, adDto);
        return ResponseEntity.ok("Вы успешно обновили своё объявление!");
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @DeleteMapping("/remove/{id}")
    public ResponseEntity<String> deleteAd(@PathVariable Long id, @RequestBody RemovedAdDto removedAdDto) {
        adService.removeAd(removedAdDto, id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Вы успешно сняли с продаж свое объявление!");
    }
}

