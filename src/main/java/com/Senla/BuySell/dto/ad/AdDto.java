package com.Senla.BuySell.dto.ad;
import com.Senla.BuySell.dto.comment.CommentDto;
import com.Senla.BuySell.dto.views.Views;
import com.Senla.BuySell.enums.AdType;
import com.Senla.BuySell.model.User;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonView;

import java.time.LocalDateTime;
import java.util.List;

@JsonPropertyOrder({
        "id",
        "title",
        "formatedAdType",
        "description",
        "price",
        "ownerId",
        "createdAt",
        "promoted",
        "promotedUntilInHours",
        "commentList"
})
public class AdDto {
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
    private Long ownerId;

    @JsonView(Views.Summary.class)
    private LocalDateTime createdAt;

    @JsonView(Views.AdPersonal.class)
    private boolean isPromoted;

    @JsonView(Views.AdPersonal.class)
    private Long promotedUntilInHours;

    @JsonView(Views.AdDetailed.class)
    private List<CommentDto> commentList;

    public AdDto(String title, String formatedAdType, String description, double price,
              Long ownerId, boolean isPromoted, Long promotedUntilInHours, LocalDateTime createdAt ) {
        this.title = title;
        this.formatedAdType = formatedAdType;
        this.description = description;
        this.price = price;
        this.ownerId = ownerId;
        this.isPromoted = isPromoted;
        this.promotedUntilInHours = promotedUntilInHours;
        this.createdAt = createdAt;
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

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isPromoted() {
        return isPromoted;
    }

    public void setPromoted(boolean promoted) {
        isPromoted = promoted;
    }

    public Long getPromotedUntilInHours() {
        return promotedUntilInHours;
    }

    public void setPromotedUntilInHours(Long promotedUntilInHours) {
        this.promotedUntilInHours = promotedUntilInHours;
    }

    public List<CommentDto> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<CommentDto> commentList) {
        this.commentList = commentList;
    }
}
