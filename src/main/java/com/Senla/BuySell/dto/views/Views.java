package com.Senla.BuySell.dto.views;

public class Views {
    public static class Summary {}

    public static class UserDetailed extends Summary {}
    public static class UserPersonal extends UserDetailed {}

    public static class AdDetailed extends Summary {}
    public static class AdPersonal extends AdDetailed {}

    public static class Admin extends Summary {}



}