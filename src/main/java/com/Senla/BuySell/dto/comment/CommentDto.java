package com.Senla.BuySell.dto.comment;

import com.Senla.BuySell.dto.views.Views;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonView;

@JsonPropertyOrder({
        "id",
        "userId",
        "nickname",
        "content",
        "adId",
})

public class CommentDto {
    @JsonView(Views.Summary.class)
    private Long id;

    @JsonView(Views.Summary.class)
    private String content;

    @JsonView(Views.Summary.class)
    private Long userId;

    @JsonView(Views.Summary.class)
    private String nickname;

    @JsonView(Views.Summary.class)
    private Long adId;

    public CommentDto(String content) {
        this.content = content;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Long getAdId() {
        return adId;
    }

    public void setAdId(Long adId) {
        this.adId = adId;
    }
}
