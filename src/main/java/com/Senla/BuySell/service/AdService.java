package com.Senla.BuySell.service;

import com.Senla.BuySell.dto.ad.AdCreateDto;
import com.Senla.BuySell.dto.ad.AdDto;
import com.Senla.BuySell.dto.ad.AdMapper;
import com.Senla.BuySell.enums.AdType;
import com.Senla.BuySell.model.Ad;
import com.Senla.BuySell.model.User;
import com.Senla.BuySell.repository.AdRepository;
import com.Senla.BuySell.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public List<AdDto> getAllAds() {
        return adMapper.toDtoList(adRepository.findAll());
    }

    public AdDto getAdById(Long id) {
        return adRepository.findById(id)
                .map(adMapper::toDto)
                .orElseThrow(() -> new NoSuchElementException("Объявление с таким ID не найдено."));
    }

    public AdDto createAd(AdCreateDto adCreateDto) {
        User user = userRepository.findById(adCreateDto.ownerId())
                .orElseThrow(() -> new NoSuchElementException("Пользователь не найден."));
        Ad ad = new Ad(
                adCreateDto.title(),
                AdType.fromDisplayName(adCreateDto.adType()),
                adCreateDto.description(),
                adCreateDto.price(),
                user
        );
        adRepository.save(ad);
        return adMapper.toDto(ad);
    }

    public AdDto updateAd(Long id, AdDto adDto) {
        Ad ad = adRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Объявление не найдено."));
        updateIfNotNullOrEmpty(adDto.title(), ad::setTitle);
        updateIfNotNullOrEmpty(adDto.formatedAdType(), adType -> ad.setAdType(AdType.fromDisplayName(adType)));
        updateIfNotNullOrEmpty(adDto.description(), ad::setDescription);
        updateIfNotNullOrEmpty(adDto.price(), ad::setPrice);
        adRepository.save(ad);
        return adMapper.toDto(ad);
    }

    public void deleteAd(Long id) {
        if (!adRepository.existsById(id)) {
            throw new NoSuchElementException("Объявление с таким ID не найдено.");
        }
        adRepository.deleteById(id);
    }

    private <T> void updateIfNotNullOrEmpty(T value, Consumer<T> setter) {
        if (value != null && !(value instanceof String && ((String) value).isEmpty())) {
            setter.accept(value);
        }
    }
}

