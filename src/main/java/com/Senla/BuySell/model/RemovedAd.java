package com.Senla.BuySell.model;

import com.Senla.BuySell.enums.AdType;
import com.Senla.BuySell.enums.ReasonsForSale;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "removed_ads")
public class RemovedAd {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "ad_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private AdType adType;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "price", nullable = false)
    private double price;

    @Column(name = "location", nullable = false)
    private String location;

    @ManyToOne
    @JoinColumn(name = "seller_id", nullable = false)
    private User seller;

    @Column(name = "reason", nullable = false)
    @Enumerated(EnumType.STRING)
    private ReasonsForSale reason;

    @Column(name = "removed_at", nullable = false)
    private LocalDateTime removedAt;

    public RemovedAd() {
    }

    public RemovedAd(String title, AdType adType, String description, double price, String location,
                     User seller, ReasonsForSale reason) {
        this.title = title;
        this.adType = adType;
        this.description = description;
        this.price = price;
        this.location = location;
        this.seller = seller;
        this.reason = reason;
        this.removedAt = LocalDateTime.now();
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

    public User getSeller() {
        return seller;
    }

    public void setSeller(User seller) {
        this.seller = seller;
    }

    public ReasonsForSale getReason() {
        return reason;
    }

    public void setReason(ReasonsForSale reason) {
        this.reason = reason;
    }

    public LocalDateTime getRemovedAt() {
        return removedAt;
    }

    public void setRemovedAt(LocalDateTime removedAt) {
        this.removedAt = removedAt;
    }
}
