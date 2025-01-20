package com.Senla.BuySell.dto.removedAd;

import com.Senla.BuySell.dto.views.Views;
import com.Senla.BuySell.enums.AdType;
import com.Senla.BuySell.enums.ReasonsForSale;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonView;

import java.time.LocalDateTime;

@JsonPropertyOrder({
        "id",
        "sellerId",
        "promoted",
        "promotedUntilInHours",
        "title",
        "formatedAdType",
        "description",
        "price",
        "location",
        "soldAt",
})

public class RemovedAdDto {
    @JsonView(Views.Summary.class)
    private Long id;

    @JsonView(Views.Summary.class)
    private String title;

    @JsonView(Views.Summary.class)
    private AdType adType;

    @JsonView(Views.Summary.class)
    private String description;

    @JsonView(Views.Summary.class)
    private double price;

    @JsonView(Views.Summary.class)
    private String location;

    @JsonView(Views.Summary.class)
    private Long sellerId;

    @JsonView(Views.Summary.class)
    private ReasonsForSale reason;

    @JsonView(Views.Summary.class)
    private LocalDateTime soldAt;

    public RemovedAdDto() {
    }

    public RemovedAdDto(ReasonsForSale reason) {
        this.reason = reason;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public AdType getAdType() {
        return adType;
    }

    public void setAdType(AdType adType) {
        this.adType = adType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Long getSellerId() {
        return sellerId;
    }

    public void setSellerId(Long sellerId) {
        this.sellerId = sellerId;
    }

    public ReasonsForSale getReason() {
        return reason;
    }

    public void setReason(ReasonsForSale reason) {
        this.reason = reason;
    }

    public LocalDateTime getSoldAt() {
        return soldAt;
    }

    public void setSoldAt(LocalDateTime soldAt) {
        this.soldAt = soldAt;
    }
}
