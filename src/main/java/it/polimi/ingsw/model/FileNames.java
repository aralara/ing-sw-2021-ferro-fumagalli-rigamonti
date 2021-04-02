package it.polimi.ingsw.model;

public enum FileNames {
    MARKET_FILE("Market_File.txt"),
    DEV_CARD_FILE("Dev_Card_File.txt"),
    LEADER_CARD_FILE("Leader_Card_File.txt"),
    FAITH_TRACK_FILE("Faith_Track_File.txt"),
    LORENZO_CARD_FILE("Lorenzo_Card_File.txt");

    private final String value;

    FileNames(final String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
