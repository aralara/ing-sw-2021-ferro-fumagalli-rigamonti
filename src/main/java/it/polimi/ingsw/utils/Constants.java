package it.polimi.ingsw.utils;

public enum Constants {

    MARKET_COLUMN_SIZE(4),
    MARKET_ROW_SIZE(3),
    FAITH_TOTAL_VATICAN_REPORTS(3);

    private final int value;

    Constants(final int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }

}
