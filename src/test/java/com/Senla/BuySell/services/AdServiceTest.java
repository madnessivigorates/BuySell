package com.Senla.BuySell.services;

import com.Senla.BuySell.dto.ad.AdDto;
import com.Senla.BuySell.dto.ad.AdMapper;
import com.Senla.BuySell.dto.removedAd.RemovedAdDto;
import com.Senla.BuySell.dto.removedAd.RemovedAdDtoMapper;
import com.Senla.BuySell.enums.AdType;
import com.Senla.BuySell.model.Ad;
import com.Senla.BuySell.model.RemovedAd;
import com.Senla.BuySell.model.User;
import com.Senla.BuySell.repository.AdRepository;
import com.Senla.BuySell.repository.RemovedAdRepository;
import com.Senla.BuySell.service.AdService;
import com.Senla.BuySell.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import java.time.LocalDateTime;

@ExtendWith(MockitoExtension.class)
class AdServiceTest {

    @Mock
    private AdRepository adRepository;

    @Mock
    private AdMapper adMapper;

    @Mock
    private UserService userService;

    @Mock
    private RemovedAdRepository removedAdRepository;

    @Mock
    private RemovedAdDtoMapper removedAdDtoMapper;

    @InjectMocks
    private AdService adService;

    private Ad testAd;
    private AdDto testAdDto;
    private User testSeller;

    @BeforeEach
    void setUp() {
        testSeller = new User("testuser", "testnickname", "password123");
        testSeller.setId(1L);

        testAd = new Ad("Test Title", AdType.TRANSPORT, "Test Description", 100.0, "Test Location",
                testSeller, false, null);
        testAd.setId(1L);

        testAdDto = new AdDto("Test Title", "Транспорт", "Test Description", 100.0, "Test Location",
                false, 0L);
        testAdDto.setId(1L);
        testAdDto.setSellerId(1L);
        testAdDto.setSellerRating(4.5);
        testAdDto.setCreatedAt(LocalDateTime.now());
    }

    @Test
    void testCreateAd() {
        when(userService.getCurrentUserId()).thenReturn(1L);
        when(userService.findUserById(1L, "Пользователь не найден")).thenReturn(testSeller);
        when(adRepository.save(any(Ad.class))).thenAnswer(invocation -> {
            Ad ad = invocation.getArgument(0);
            ad.setId(1L);
            return ad;
        });
        when(adMapper.toDto(any(Ad.class))).thenAnswer(invocation -> {
            Ad ad = invocation.getArgument(0);
            return new AdDto(ad.getTitle(), "Транспорт", ad.getDescription(), ad.getPrice(), ad.getLocation(),
                    ad.isPromoted(), ad.getPromotedUntil() != null ? 24L : null);
        });

        AdDto result = adService.createAd(testAdDto);

        assertNotNull(result, "Результат не может быть null");
        assertEquals(testAdDto.getTitle(), result.getTitle(), "Заголовки должны совпадать");
        assertEquals(testAdDto.getFormatedAdType(), result.getFormatedAdType(), "Тип объявления должен совпадать");
        assertEquals(testAdDto.isPromoted(), result.isPromoted(), "Статус продвижения должен совпадать");
        verify(adRepository, times(1)).save(any(Ad.class));
        verify(adMapper, times(1)).toDto(any(Ad.class));
    }

    @Test
    void testGetAdById() {
        when(adRepository.findById(1L)).thenReturn(Optional.of(testAd));
        when(adMapper.toDto(testAd)).thenReturn(testAdDto);

        AdDto result = adService.getAdById(1L);

        assertNotNull(result, "Результат не может быть null");
        assertEquals(testAdDto.getId(), result.getId(), "ID должен совпадать");
        assertEquals(testAdDto.getFormatedAdType(), result.getFormatedAdType(), "Тип объявления должен совпадать");
        verify(adRepository, times(1)).findById(1L);
        verify(adMapper, times(1)).toDto(testAd);
    }

    @Test
    void testGetUserAds() {
        when(userService.getCurrentUserId()).thenReturn(1L);
        when(adRepository.findBySellerId(1L)).thenReturn(List.of(testAd));
        when(adMapper.toDtoList(List.of(testAd))).thenReturn(List.of(testAdDto));

        List<AdDto> result = adService.getUserAds();

        assertNotNull(result, "Результат не может быть null");
        assertEquals(1, result.size(), "Размер списка должен совпадать");
        assertEquals(testAdDto.getTitle(), result.get(0).getTitle(), "Тип объявления должен совпадать");
        assertEquals(testAdDto.getSellerId(), result.get(0).getSellerId(), "Seller ID должен совпадать");
        verify(adRepository, times(1)).findBySellerId(1L);
        verify(adMapper, times(1)).toDtoList(anyList());
    }

    @Test
    void testPromoteAd() {
        when(adRepository.findById(1L)).thenReturn(Optional.of(testAd));
        when(userService.getCurrentUserId()).thenReturn(1L);

        adService.promoteAd(1L, 48L);

        assertTrue(testAd.isPromoted(), "Объявление должно быть продвинуто");
        assertNotNull(testAd.getPromotedUntil(), "Срок продвижения должен быть задан");
        verify(adRepository, times(1)).save(testAd);
    }

    @Test
    void testRemoveAd() {
        RemovedAdDto removedAdDto = new RemovedAdDto("Передумал");
        when(adRepository.findById(1L)).thenReturn(Optional.of(testAd));
        when(userService.getCurrentUserId()).thenReturn(1L);

        adService.removeAd(removedAdDto, 1L);

        verify(removedAdRepository, times(1)).save(any(RemovedAd.class));
        verify(adRepository, times(1)).deleteById(1L);
    }
}


