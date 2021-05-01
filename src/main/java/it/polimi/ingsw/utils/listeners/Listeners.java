package it.polimi.ingsw.utils.listeners;

public enum Listeners {
    L_GAME_DEV_DECKS("developmentDecksListener"),
    L_GAME_FAITH("faithTrackListener"),
    L_GAME_MARKET("marketListener"),
    L_GAME_LORENZO("lorenzoBoardListener"),
    L_BOARD_DEV("developmentBoardListener"),
    L_BOARD_FAITH("faithBoardListener"),
    L_BOARD_LEADER("leaderBoardListener"),
    L_BOARD_STRONGBOX("strongboxListener"),
    L_BOARD_WAREHOUSE("warehouseListener");

    private final String value;

    Listeners(final String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
