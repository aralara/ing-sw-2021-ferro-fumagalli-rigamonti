package it.polimi.ingsw.client.gui;

public enum SceneNames {
    LOADING("/fxml/Loading.fxml"),
    CONNECTION_MENU("/fxml/ConnectionMenu.fxml"),
    NICKNAME_MENU("/fxml/NicknameMenu.fxml"),
    GAME_MODE_MENU("/fxml/GameModeMenu.fxml"),
    MULTI_PLAYER_WAITING("/fxml/MultiPlayerWaiting.fxml"),
    SAVES_MENU("/fxml/Saves.fxml"),
    LEADER_CHOICE_MENU("/fxml/LeaderChoiceMenu.fxml"),
    RESOURCE_CHOICE_MENU("/fxml/ResourceChoiceMenu.fxml"),
    PLAYER_BOARD("/fxml/PlayerBoard.fxml"),
    MARKET_BOARD("/fxml/MarketBoard.fxml"),
    CARD("/fxml/Card.fxml"),
    DECKS_BOARD("/fxml/DecksBoard.fxml"),
    DEPOTS("/fxml/Depots.fxml"),
    RANKING("/fxml/Ranking.fxml");

    private final String value;

    /**
     * Constructor for a SceneNames entry
     * @param value Value of the entry
     */
    SceneNames(final String value) {
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
