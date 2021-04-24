package it.polimi.ingsw.server.model;

public enum FileNames {
    MARKET_FILE("src/main/resources/json/Market_File.json"),
    DEV_CARD_FILE("src/main/resources/json/Dev_Card_File.json"),
    LEADER_CARD_FILE("src/main/resources/json/Leader_Card_File.json"),
    FAITH_SPACE_FILE("src/main/resources/json/Faith_Space_File.json"),
    VATICAN_REPORT_FILE("src/main/resources/json/Vatican_Report_File.json"),
    LORENZO_DEV_FILE("src/main/resources/json/Lorenzo_Dev_File.json"),
    LORENZO_FAITH_FILE("src/main/resources/json/Lorenzo_Faith_File.json");

    private final String value;

    FileNames(final String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
