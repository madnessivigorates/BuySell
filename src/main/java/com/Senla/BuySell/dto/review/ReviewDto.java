package com.Senla.BuySell.dto.review;

import com.Senla.BuySell.dto.views.Views;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonView;

@JsonPropertyOrder({
        "id",
        "senderId",
        "senderNickname",
        "comment",
        "rating"
})
public class ReviewDto {
    @JsonView(Views.Summary.class)
    private Long id;

    @JsonView(Views.Summary.class)
    private Long senderId;

    @JsonView(Views.Summary.class)
    private String senderNickname;

    @JsonView(Views.Summary.class)
    private String comment;

    @JsonView(Views.Summary.class)
    private int rating;

    public ReviewDto() {
    }

    public ReviewDto(String comment, int rating) {
        this.comment = comment;
        this.rating = rating;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public String getSenderNickname() {
        return senderNickname;
    }

    public void setSenderNickname(String senderNickname) {
        this.senderNickname = senderNickname;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
