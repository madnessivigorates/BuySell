package com.Senla.BuySell.service;

import com.Senla.BuySell.dto.ad.AdDto;
import com.Senla.BuySell.dto.ad.AdMapper;
import com.Senla.BuySell.enums.AdType;
import com.Senla.BuySell.model.Ad;
import com.Senla.BuySell.model.User;
import com.Senla.BuySell.repository.AdRepository;
import com.Senla.BuySell.repository.UserRepository;
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

    @Autowired
    public AdService(AdRepository adRepository, AdMapper adMapper, UserRepository userRepository) {
        this.adRepository = adRepository;
        this.adMapper = adMapper;
        this.userRepository = userRepository;
    }

    public List<AdDto> getAllAds(AdType adType, String keyword) {
        List<Ad> ads = adRepository.findAllByAdTypeAndKeyword(adType, keyword);
        return adMapper.toDtoList(ads);
    }

    public AdDto getAdById(Long id) {
        return adRepository.findById(id)
                .map(adMapper::toDto)
                .orElseThrow(() -> new NoSuchElementException("Объявление с таким ID не найдено."));
    }

    public AdDto createAd(AdDto adDto) {
        User user = userRepository.findById(adDto.getOwnerId())
                .orElseThrow(() -> new NoSuchElementException("Пользователь не найден."));
        Ad ad = new Ad(
                adDto.getTitle(),
                AdType.fromDisplayName(adDto.getFormatedAdType()),
                adDto.getDescription(),
                adDto.getPrice(),
                user,
                adDto.isPromoted(),
                LocalDateTime.now().plusHours(adDto.getPromotedUntilInHours()),
                LocalDateTime.now()
        );
        adRepository.save(ad);
        return adMapper.toDto(ad);
    }

    public void promoteAd(Long adId, long hours) {
        Ad ad = adRepository.findById(adId)
                .orElseThrow(() -> new NoSuchElementException("Объявление не найдено."));
        ad.setPromoted(true);
        ad.setPromotedUntil(LocalDateTime.now().plusHours(hours));
        adRepository.save(ad);
    }

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

    public void deleteAd(Long id) {
        if (!adRepository.existsById(id)) {
            throw new NoSuchElementException("Объявление с таким ID не найдено.");
        }
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

