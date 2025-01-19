package com.Senla.BuySell.dto.message;

import com.Senla.BuySell.dto.views.Views;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonView;

import java.time.LocalDateTime;
@JsonPropertyOrder({
        "id",
        "senderNickname",
        "content",
        "sentAt",
})

public class MessageDto {
    @JsonView(Views.Summary.class)
    private Long id;

    @JsonView(Views.Summary.class)
    private String senderNickname;

    @JsonView(Views.Summary.class)
    private String content;

    @JsonView(Views.Summary.class)
    private LocalDateTime sentAt;

    public MessageDto(String content) {
        this.content = content;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSenderNickname() {
        return senderNickname;
    }

    public void setSenderNickname(String senderNickname) {
        this.senderNickname = senderNickname;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getSentAt() {
        return sentAt;
    }

    public void setSentAt(LocalDateTime sentAt) {
        this.sentAt = sentAt;
    }
}
