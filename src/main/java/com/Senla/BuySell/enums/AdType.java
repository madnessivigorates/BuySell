package com.Senla.BuySell.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum AdType {
    TRANSPORT("Транспорт"),
    CLOTHING("Одежда"),
    ELECTRONICS("Электроника"),
    FURNITURE("Мебель"),
    REAL_ESTATE("Недвижимость"),
    SERVICES("Услуги"),
    TOYS("Игрушки"),
    BOOKS("Книги");

    private final String displayName;

    AdType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static AdType fromDisplayName(String displayName) {
        for (AdType adType : AdType.values()) {
            if (adType.getDisplayName().equalsIgnoreCase(displayName)) {
                return adType;
            }
        }
        throw new IllegalArgumentException("Unknown enum type: " + displayName);
    }
}

