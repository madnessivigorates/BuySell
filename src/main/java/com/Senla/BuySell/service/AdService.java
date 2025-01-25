package com.Senla.BuySell.service;

import com.Senla.BuySell.comparators.AdComparators;
import com.Senla.BuySell.dto.ad.AdDto;
import com.Senla.BuySell.dto.ad.AdMapper;
import com.Senla.BuySell.dto.removedAd.RemovedAdDto;
import com.Senla.BuySell.dto.removedAd.RemovedAdDtoMapper;
import com.Senla.BuySell.enums.AdType;
import com.Senla.BuySell.enums.ReasonsForSale;
import com.Senla.BuySell.exceptions.OwnershipException;
import com.Senla.BuySell.model.Ad;
import com.Senla.BuySell.model.RemovedAd;
import com.Senla.BuySell.model.User;
import com.Senla.BuySell.repository.AdRepository;
import com.Senla.BuySell.repository.RemovedAdRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Consumer;

@Service
public class AdService {
    private final AdRepository adRepository;
    private final AdMapper adMapper;
    private final UserService userService;
    private final RemovedAdRepository removedAdRepository;
    private final RemovedAdDtoMapper removedAdDtoMapper;

    @Autowired
    public AdService(
            AdRepository adRepository,
            AdMapper adMapper,
            UserService userService,
            RemovedAdRepository removedAdRepository,
            RemovedAdDtoMapper removedAdDtoMapper
    ) {
        this.adRepository = adRepository;
        this.adMapper = adMapper;
        this.userService = userService;
        this.removedAdRepository = removedAdRepository;
        this.removedAdDtoMapper = removedAdDtoMapper;
    }

    public List<AdDto> getAllAds(String adType, String keyword, String sortBy, boolean ascending) {
        AdType type = (adType != null && !adType.isEmpty()) ? AdType.fromDisplayName(adType) : null;
        List<AdDto> adDtos = adMapper.toDtoList(adRepository.findAllByAdTypeAndKeyword(type, keyword));
        adDtos.sort(sortAdDtoList(sortBy,ascending));
        return adDtos;
    }

    public AdDto getAdById(Long id) {
        return adMapper.toDto(findAdById(id));
    }

    public List<RemovedAdDto> getUserAdsHistory(Long sellerId){
        return removedAdDtoMapper.toDtoList(removedAdRepository.findBySellerId(sellerId));
    }

    public List<AdDto> getUserAds(){
        Long userId = userService.getCurrentUserId();
        return adMapper.toDtoList(adRepository.findBySellerId(userId));
    }

    @Transactional
    public AdDto createAd(AdDto adDto) {
        Long sellerId = userService.getCurrentUserId();
        User seller = userService.findUserById(sellerId, "Пользователь не найден");
        LocalDateTime promotedUntilInHours = LocalDateTime.now().plusHours(adDto.getPromotedUntilInHours());
        if(!adDto.isPromoted()){promotedUntilInHours = null;}
        Ad ad = new Ad(
                adDto.getTitle(),
                AdType.fromDisplayName(adDto.getFormatedAdType()),
                adDto.getDescription(),
                adDto.getPrice(),
                adDto.getLocation(),
                seller,
                adDto.isPromoted(),
                promotedUntilInHours
        );
        adRepository.save(ad);
        return adMapper.toDto(ad);
    }

    @Transactional
    public void promoteAd(Long adId, long hours) {
        Ad ad = findAdById(adId);
        checkOwnership(ad);
        ad.setPromoted(true);
        ad.setPromotedUntil(LocalDateTime.now().plusHours(hours));
        adRepository.save(ad);
    }

    @Transactional
    public AdDto updateAd(Long id, AdDto adDto) {
        Ad ad = findAdById(id);
        checkOwnership(ad);
        updateIfNotNullOrEmpty(adDto.getTitle(), ad::setTitle);
        updateIfNotNullOrEmpty(adDto.getFormatedAdType(), adType -> ad.setAdType(AdType.fromDisplayName(adType)));
        updateIfNotNullOrEmpty(adDto.getDescription(), ad::setDescription);
        updateIfNotNullOrEmpty(adDto.getPrice(), ad::setPrice);
        adRepository.save(ad);
        return adMapper.toDto(ad);
    }

    @Transactional
    public void removeAd(RemovedAdDto removedAdDto, Long id) {
        Ad ad = findAdById(id);
        checkOwnership(ad);
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

    public Ad findAdById(Long id) {
        return adRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Объявление с ID " + id + " не найдено."));
    }

    private void checkOwnership(Ad ad) {
        Long currentUserId = userService.getCurrentUserId();
        if (!ad.getSeller().getId().equals(currentUserId)) {
            throw new OwnershipException("Это не ваше объявление");
        }
    }

    private Comparator<AdDto> sortAdDtoList(String sortBy, boolean ascending) {
        Comparator<AdDto> comparator = switch (sortBy) {
            case "price" -> ascending ? AdComparators.BY_PRICE_ASC : AdComparators.BY_PRICE_DESC;
            case "location" -> ascending ? AdComparators.BY_CITY : AdComparators.BY_CITY.reversed();
            case "createdAt" -> ascending ? AdComparators.BY_CREATED_DATE : AdComparators.BY_CREATED_DATE_DESC;
            case "rating" -> ascending ? AdComparators.BY_RATING : AdComparators.BY_RATING_DESC;
            case "promoted" -> ascending ? AdComparators.BY_IS_PROMOTED.reversed() : AdComparators.BY_IS_PROMOTED;
            default -> ascending ? AdComparators.BY_DEFAULT : AdComparators.BY_DEFAULT.reversed();
        };
        return comparator;
    }


    private <T> void updateIfNotNullOrEmpty(T value, Consumer<T> setter) {
        if (value != null && !(value instanceof String && ((String) value).isEmpty())) {
            setter.accept(value);
        }
    }
}

