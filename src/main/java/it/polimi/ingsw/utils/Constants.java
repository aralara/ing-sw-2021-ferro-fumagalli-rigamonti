package it.polimi.ingsw.utils;

public enum Constants {

    MIN_LOBBY_SIZE(1),
    MAX_LOBBY_SIZE(4),
    MARKET_COLUMN_SIZE(4),
    MARKET_ROW_SIZE(3),
    FAITH_TOTAL_VATICAN_REPORTS(3),
    BASE_WAREHOUSE_SHELVES(3),
    BASE_DEVELOPMENT_SPACES(3),
    DEVELOPMENT_DECK_NUMBER(12),
    INITIAL_LEADER_CARDS(4);

    private final int value;

    Constants(final int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }
}
