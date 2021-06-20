package it.polimi.ingsw.utils;

import java.util.Arrays;

/**
 * Enum used to notify the player of the current state of the game
 */
public enum TurnStatus {

    INVALID(0),
    LOAD_TURN_NORMAL(1),
    LOAD_TURN_LAST_ROUND(2),
    LOAD_TURN_END_GAME(3);

    private final int value;

    /**
     * Enum constructor
     * @param value Integer value
     */
    TurnStatus(final int value) {
        this.value = value;
    }

    /**
     * Getter for value attribute
     * @return Returns value
     */
    public int value() {
        return value;
    }

    /**
     * Gets an enum object given its value
     * @param n Value of the wanted enum constant
     * @return Returns the corresponding enum constant if found, otherwise returns LOAD_TURN_NORMAL
     */
    public static TurnStatus getStatus(int n) {
        return  Arrays.stream(values()).filter(ts -> ts.value() == n).findFirst().orElse(LOAD_TURN_NORMAL);
    }
}

