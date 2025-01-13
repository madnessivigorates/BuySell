package com.Senla.BuySell.model;

import com.Senla.BuySell.enums.AdType;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

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

    @ManyToOne
    @JoinColumn(name = "seller_id", nullable = false)
    private User user;

    @Column(name = "is_promoted", nullable = false)
    private boolean isPromoted = false;

    @Column(name = "promoted_until")
    private LocalDateTime promotedUntil = null;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @OneToMany (mappedBy = "ad", fetch = FetchType.EAGER)
    private List<Comment> commentList;

    public Ad() {
    }

    public Ad(String title, AdType adType, String description, double price,
              User user, boolean isPromoted, LocalDateTime promotedUntil, LocalDateTime createdAt ) {
        this.title = title;
        this.adType = adType;
        this.description = description;
        this.price = price;
        this.user = user;
        this.isPromoted = isPromoted;
        this.promotedUntil = promotedUntil;
        this.createdAt = createdAt;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Comment> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<Comment> commentList) {
        this.commentList = commentList;
    }

}
