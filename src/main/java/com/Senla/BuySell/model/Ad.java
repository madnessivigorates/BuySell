package com.Senla.BuySell.model;

import com.Senla.BuySell.enums.AdType;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "ads")
public class Ad {
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

    @Column(name = "is_promoted", nullable = false)
    private boolean isPromoted;

    @Column(name = "promoted_until")
    private LocalDateTime promotedUntil;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @OneToMany (mappedBy = "ad", fetch = FetchType.EAGER)
    private List<Comment> commentList;

    public Ad() {
    }

    public Ad(String title, AdType adType, String description, double price, String location,
              User seller, boolean isPromoted, LocalDateTime promotedUntil ) {
        this.title = title;
        this.adType = adType;
        this.description = description;
        this.price = price;
        this.location = location;
        this.seller = seller;
        this.isPromoted = isPromoted;
        this.promotedUntil = promotedUntil;
        this.createdAt = LocalDateTime.now();
    }

    public LocalDateTime getPromotedUntil() {
        return promotedUntil;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setPromotedUntil(LocalDateTime promotedUntil) {
        this.promotedUntil = promotedUntil;
    }

    public boolean isPromoted() {
        return isPromoted;
    }

    public void setPromoted(boolean promoted) {
        isPromoted = promoted;
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

    public List<Comment> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<Comment> commentList) {
        this.commentList = commentList;
    }

}
