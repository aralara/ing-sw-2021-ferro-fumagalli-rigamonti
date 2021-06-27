package it.polimi.ingsw.server.model;

/**
 * Names of loaded json files
 */
public enum FileNames {

    MARKET_FILE("/json/Market_File.json"),
    DEV_CARD_FILE("/json/Dev_Card_File.json"),
    LEADER_CARD_FILE("/json/Leader_Card_File.json"),
    FAITH_SPACE_FILE("/json/Faith_Space_File.json"),
    VATICAN_REPORT_FILE("/json/Vatican_Report_File.json"),
    LORENZO_DEV_FILE("/json/Lorenzo_Dev_File.json"),
    LORENZO_FAITH_FILE("/json/Lorenzo_Faith_File.json");

    private final String value;

    /**
     * Constructor for a FileNames entry
     * @param value Value of the entry
     */
    FileNames(final String value) {
        this.value = value;
    }

    /**
     * Gets the value of an entry
     * @return Returns the value
     */
    public String value() {
        return value;
    }
}
