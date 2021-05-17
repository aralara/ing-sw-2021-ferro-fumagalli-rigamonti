package it.polimi.ingsw.utils;

import java.util.Arrays;

public enum TurnStatus {

    LOAD_TURN_NORMAL(1),
    LOAD_TURN_LAST_ROUND(2),
    LOAD_TURN_END_GAME(3);

    private final int value;

    TurnStatus(final int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }

    public static TurnStatus getStatus(int n) {
        return  Arrays.stream(values()).filter(ts -> ts.value() == n).findFirst().orElse(LOAD_TURN_NORMAL);
    }
}

