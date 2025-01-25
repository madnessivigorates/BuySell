package com.Senla.BuySell.dto.user;
import com.Senla.BuySell.dto.ad.AdDto;
import com.Senla.BuySell.dto.review.ReviewDto;
import com.Senla.BuySell.dto.views.Views;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonView;

import java.util.List;
@JsonPropertyOrder({
        "id",
        "rating",
        "nickname",
        "username",
        "password",
        "ads",
        "reviews"
})

public class UserDto {

    @JsonView(Views.Summary.class)
    private Long id;

    @JsonView({Views.Summary.class,Views.UserRegisterOrEdit.class})
    private String nickname;

    @JsonView({Views.UserPersonal.class,Views.UserRegisterOrEdit.class})
    private String username;

    @JsonView(Views.UserRegisterOrEdit.class)
    private String password;

    @JsonView(Views.UserDetailed.class)
    private List<AdDto> ads;

    @JsonView(Views.UserDetailed.class)
    private List<ReviewDto> reviews;

    @JsonView(Views.Summary.class)
    private double rating;

    public UserDto() {
    }

    public UserDto(String nickname, String username, String password) {
        this.nickname = nickname;
        this.username = username;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public List<AdDto> getAds() {
        return ads;
    }

    public List<ReviewDto> getReviews() {
        return reviews;
    }

    public void setReviews(List<ReviewDto> reviews) {
        this.reviews = reviews;
    }

    public void setAds(List<AdDto> ads) {
        this.ads = ads;
    }
}
