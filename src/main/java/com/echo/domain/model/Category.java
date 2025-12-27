package com.leitner.domain.model;

public enum Category {
    FIRST(1),
    SECOND(2),
    THIRD(4),
    FOURTH(8),
    FIFTH(16),
    SIXTH(32),
    SEVENTH(64);

    private final int frequencyDays;

    Category(int frequencyDays) {
        this.frequencyDays = frequencyDays;
    }

    public int getFrequencyDays() {
        return frequencyDays;
    }

    public Category next() {
        int ordinal = this.ordinal();
        if (ordinal >= values().length - 1) {
            return null;
        }
        return values()[ordinal + 1];
    }

    public static Category first() {
        return FIRST;
    }
}