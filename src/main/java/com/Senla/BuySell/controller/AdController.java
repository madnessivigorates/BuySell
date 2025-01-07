package com.Senla.BuySell.controller;

import com.Senla.BuySell.dto.ad.AdCreateDto;
import com.Senla.BuySell.dto.ad.AdDto;
import com.Senla.BuySell.model.Ad;
import com.Senla.BuySell.service.AdService;
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
    public List<AdDto> getAllAds(){
        return adService.getAllAds();
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<?> getAllAds(@PathVariable Long id) {
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
    @PostMapping("/create")
    public ResponseEntity<AdDto> createAd(@RequestBody AdCreateDto adCreateDto) {
        AdDto createdAdd = adService.createAd(adCreateDto);
        return new ResponseEntity<>(createdAdd, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PutMapping("/edit/{id}")
    public ResponseEntity<AdDto> updateAd(@PathVariable Long id, @RequestBody AdDto adDto) {
        AdDto updatedAd = adService.updateAd(id, adDto);
        return new ResponseEntity<>(updatedAd, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAd(@PathVariable Long id) {
        adService.deleteAd(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
