package com.perm_tourism.backend.enums;

public enum Gender {
    MALE("Мужской"),
    FEMALE("Женский"),
    NOT_SPECIFIED("Не указано");

    private final String displayName;

    Gender(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
