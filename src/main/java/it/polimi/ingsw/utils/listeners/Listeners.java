package it.polimi.ingsw.utils.listeners;

public enum Listeners {
    GAME_DEV_DECK("developmentDeckListener"),
    GAME_FAITH_REPORT("lastReportListener"),
    GAME_MARKET("marketListener"),
    GAME_LORENZO_FAITH("lorenzoFaithListener"),
    BOARD_DEV_SPACES("developmentBoardSpacesListener"),
    BOARD_FAITH_FAITH("faithBoardFaithListener"),
    BOARD_FAITH_POPE("faithBoardPopeListener"),
    BOARD_LEADER_HAND("leaderBoardHandListener"),
    BOARD_LEADER_BOARD("leaderBoardHandListener"),
    BOARD_STRONGBOX("strongboxListener"),
    BOARD_WAREHOUSE("warehouseListener");

    private final String value;

    Listeners(final String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}