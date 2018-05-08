package com.account.domain.enums;

public enum Currency {
    USD, EUR, BR;

    public static Currency fromValue(String value) {
        for (Currency currency : values())
            if (currency.name().equalsIgnoreCase(value)) {
                return currency;
            }
        return null;
    }
}
