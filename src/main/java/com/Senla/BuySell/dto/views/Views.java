package com.Senla.BuySell.dto.views;

public class Views {
    public static class Summary {}

    public static class UserDetailed extends Summary {}
    public static class UserPersonal extends UserDetailed {}
    public static class UserRegisterOrEdit {}

    public static class AdDetailed extends Summary {}
    public static class AdPersonal extends AdDetailed {}
    public static class AdCreate extends AdEdit{}
    public static class AdEdit {}

    public static class ReviewCreate{};

    public static class MessageCreate{};

    public static class RemovedAdCreate{};

    public static class CommentCreate{};

    public static class Admin extends Summary {}



}