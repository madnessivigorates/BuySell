package com.Senla.BuySell.service;

import com.Senla.BuySell.dto.ad.AdDto;
import com.Senla.BuySell.dto.ad.AdMapper;
import com.Senla.BuySell.dto.removedAd.RemovedAdDto;
import com.Senla.BuySell.dto.removedAd.RemovedAdDtoMapper;
import com.Senla.BuySell.enums.AdType;
import com.Senla.BuySell.enums.ReasonsForSale;
import com.Senla.BuySell.model.Ad;
import com.Senla.BuySell.model.RemovedAd;
import com.Senla.BuySell.model.User;
import com.Senla.BuySell.repository.AdRepository;
import com.Senla.BuySell.repository.RemovedAdRepository;
import com.Senla.BuySell.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Consumer;

@Service
public class AdService {
    private final AdRepository adRepository;
    private final AdMapper adMapper;
    private final UserRepository userRepository;
    private final RemovedAdRepository removedAdRepository;
    private final RemovedAdDtoMapper removedAdDtoMapper;

    @Autowired
    public AdService(AdRepository adRepository, AdMapper adMapper, UserRepository userRepository,
                     RemovedAdRepository removedAdRepository,RemovedAdDtoMapper removedAdDtoMapper) {
        this.adRepository = adRepository;
        this.adMapper = adMapper;
        this.userRepository = userRepository;
        this.removedAdRepository = removedAdRepository;
        this.removedAdDtoMapper = removedAdDtoMapper;
    }

    public List<AdDto> getAllAds(String adType, String keyword) {
        AdType type = null;
        if (adType != null && !adType.isEmpty()) {
            type = AdType.fromDisplayName(adType);
        }
        return adMapper.toDtoList(adRepository.findAllByAdTypeAndKeyword(type, keyword));
    }


    public AdDto getAdById(Long id) {
        return adRepository.findById(id)
                .map(adMapper::toDto)
                .orElseThrow(() -> new NoSuchElementException("Объявление с таким ID не найдено."));
    }

    public List<RemovedAdDto> getUserAdsHistory(Long sellerId){
        return removedAdDtoMapper.toDtoList(removedAdRepository.findBySellerId(sellerId));
    }

    @Transactional
    public AdDto createAd(AdDto adDto) {
        User seller = userRepository.findById(adDto.getSellerId())
                .orElseThrow(() -> new NoSuchElementException("Пользователь не найден."));
        Ad ad = new Ad(
                adDto.getTitle(),
                AdType.fromDisplayName(adDto.getFormatedAdType()),
                adDto.getDescription(),
                adDto.getPrice(),
                adDto.getLocation(),
                seller,
                adDto.isPromoted(),
                LocalDateTime.now().plusHours(adDto.getPromotedUntilInHours())
        );
        adRepository.save(ad);
        return adMapper.toDto(ad);
    }

    @Transactional
    public void promoteAd(Long adId, long hours) {
        Ad ad = adRepository.findById(adId)
                .orElseThrow(() -> new NoSuchElementException("Объявление не найдено."));
        ad.setPromoted(true);
        ad.setPromotedUntil(LocalDateTime.now().plusHours(hours));
        adRepository.save(ad);
    }

    @Transactional
    public AdDto updateAd(Long id, AdDto adDto) {
        Ad ad = adRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Объявление не найдено."));
        updateIfNotNullOrEmpty(adDto.getTitle(), ad::setTitle);
        updateIfNotNullOrEmpty(adDto.getFormatedAdType(), adType -> ad.setAdType(AdType.fromDisplayName(adType)));
        updateIfNotNullOrEmpty(adDto.getDescription(), ad::setDescription);
        updateIfNotNullOrEmpty(adDto.getPrice(), ad::setPrice);
        adRepository.save(ad);
        return adMapper.toDto(ad);
    }

    @Transactional
    public void removeAd(RemovedAdDto removedAdDto, Long id) {
        Ad ad = adRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Объявление с таким ID не найдено."));
        RemovedAd removedAd = new RemovedAd(
                ad.getTitle(),
                ad.getAdType(),
                ad.getDescription(),
                ad.getPrice(),
                ad.getLocation(),
                ad.getSeller(),
                ReasonsForSale.fromDisplayName(removedAdDto.getFormatReasonForSale())
        );
        removedAdRepository.save(removedAd);
        adRepository.deleteById(id);
    }

    @Scheduled(fixedRate = 60000)
    public void updatePromotions() {
        List<Ad> ads = adRepository.findAll();
        for (Ad ad : ads) {
            if (ad.getPromotedUntil() != null && ad.getPromotedUntil().isBefore(LocalDateTime.now())) {
                ad.setPromoted(false);
                ad.setPromotedUntil(null);
                adRepository.save(ad);
            }
        }
    }

    private <T> void updateIfNotNullOrEmpty(T value, Consumer<T> setter) {
        if (value != null && !(value instanceof String && ((String) value).isEmpty())) {
            setter.accept(value);
        }
    }
}

