package com.Senla.BuySell.enums;

public enum ReasonsForSale {
    SOLDOUT("Продал"),
    RECONSIDER("Передумал"),
    OTHER("Другое");

    private final String displayName;

    ReasonsForSale(String displayName){this.displayName = displayName;}

    public String getDisplayName(){ return displayName;}

    public static ReasonsForSale fromDisplayName(String displayName) {
        for (ReasonsForSale reasonsForSale : ReasonsForSale.values()) {
            if (reasonsForSale.getDisplayName().equalsIgnoreCase(displayName)) {
                return reasonsForSale;
            }
        }
        throw new IllegalArgumentException("Unknown enum type: " + displayName);
    }
}
