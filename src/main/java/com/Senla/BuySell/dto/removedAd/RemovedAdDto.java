package com.Senla.BuySell.dto.removedAd;

import com.Senla.BuySell.dto.views.Views;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonView;

import java.time.LocalDateTime;

@JsonPropertyOrder({
        "id",
        "sellerId",
        "title",
        "formatedAdType",
        "description",
        "price",
        "location",
        "formatReasonForSale",
        "removedAt",
})

public class RemovedAdDto {
    @JsonView(Views.Summary.class)
    private Long id;

    @JsonView(Views.Summary.class)
    private String title;

    @JsonView(Views.Summary.class)
    private String formatedAdType;

    @JsonView(Views.Summary.class)
    private String description;

    @JsonView(Views.Summary.class)
    private double price;

    @JsonView(Views.Summary.class)
    private String location;

    @JsonView(Views.Summary.class)
    private Long sellerId;

    @JsonView({Views.Summary.class,Views.RemovedAdCreate.class})
    private String formatReasonForSale;

    @JsonView(Views.Summary.class)
    private LocalDateTime removedAt;

    public RemovedAdDto() {
    }

    public RemovedAdDto(String formatReasonForSale) {
        this.formatReasonForSale = formatReasonForSale;
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

    public String getFormatedAdType() {
        return formatedAdType;
    }

    public void setFormatedAdType(String formatedAdType) {
        this.formatedAdType = formatedAdType;
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

    public String getFormatReasonForSale() {
        return formatReasonForSale;
    }

    public void setFormatReasonForSale(String formatReasonForSale) {
        this.formatReasonForSale = formatReasonForSale;
    }

    public LocalDateTime getRemovedAt() {
        return removedAt;
    }

    public void setRemovedAt(LocalDateTime removedAt) {
        this.removedAt = removedAt;
    }
}
