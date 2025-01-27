package com.Senla.BuySell.dto.ad;
import com.Senla.BuySell.dto.comment.CommentDto;
import com.Senla.BuySell.dto.views.Views;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonView;

import java.time.LocalDateTime;
import java.util.List;

@JsonPropertyOrder({
        "id",
        "sellerId",
        "sellerRating",
        "promoted",
        "promotedUntilInHours",
        "promotedUntil",
        "title",
        "formatedAdType",
        "description",
        "price",
        "location",
        "createdAt",
        "commentList"
})
public class AdDto {
    @JsonView(Views.Summary.class)
    private Long id;

    @JsonView({Views.Summary.class,Views.AdEdit.class})
    private String title;

    @JsonView({Views.Summary.class,Views.AdEdit.class})
    private String formatedAdType;

    @JsonView({Views.Summary.class,Views.AdEdit.class})
    private String description;

    @JsonView({Views.Summary.class,Views.AdEdit.class})
    private double price;

    @JsonView({Views.Summary.class,Views.AdEdit.class})
    private String location;

    @JsonView(Views.Summary.class)
    private Long sellerId;

    @JsonView(Views.Summary.class)
    private double sellerRating;

    @JsonView(Views.Summary.class)
    private LocalDateTime createdAt;

    @JsonView({Views.AdPersonal.class,Views.AdCreate.class})
    private boolean promoted;

    @JsonView(Views.AdCreate.class)
    private Long promotedUntilInHours;

    @JsonView(Views.AdPersonal.class)
    private LocalDateTime promotedUntil;

    @JsonView(Views.AdDetailed.class)
    private List<CommentDto> commentList;

    public AdDto() {
    }

    public AdDto(String title, String formatedAdType, String description, double price,String location,
                 boolean isPromoted, Long promotedUntilInHours) {
        this.promoted = isPromoted;
        this.promotedUntilInHours = promotedUntilInHours;
        this.title = title;
        this.formatedAdType = formatedAdType;
        this.description = description;
        this.price = price;
        this.location = location;
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

    public double getSellerRating() {
        return sellerRating;
    }

    public void setSellerRating(double sellerRating) {
        this.sellerRating = sellerRating;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isPromoted() {
        return promoted;
    }

    public void setPromoted(boolean promoted) {
        this.promoted = promoted;
    }

    public Long getPromotedUntilInHours() {
        return promotedUntilInHours;
    }

    public void setPromotedUntilInHours(Long promotedUntilInHours) {
        this.promotedUntilInHours = promotedUntilInHours;
    }

    public LocalDateTime getPromotedUntil() {
        return promotedUntil;
    }

    public void setPromotedUntil(LocalDateTime promotedUntil) {
        this.promotedUntil = promotedUntil;
    }

    public List<CommentDto> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<CommentDto> commentList) {
        this.commentList = commentList;
    }
}
