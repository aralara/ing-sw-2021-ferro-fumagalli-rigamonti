package it.polimi.ingsw.client.gui;

public enum SceneNames {
    LOADING("/fxml/Loading.fxml"),
    CONNECTION_MENU("/fxml/ConnectionMenu.fxml"),
    NICKNAME_MENU("/fxml/NicknameMenu.fxml"),
    GAME_MODE_MENU("/fxml/GameModeMenu.fxml"),
    MULTI_PLAYER_WAITING("/fxml/MultiPlayerWaiting.fxml"),
    LEADER_CHOICE_MENU("/fxml/LeaderChoiceMenu.fxml"),
    RESOURCE_CHOICE_MENU("/fxml/ResourceChoiceMenu.fxml"),
    PLAYER_BOARD("/fxml/PlayerBoard.fxml"),
    MARKET_BOARD("/fxml/MarketBoard.fxml"),
    CARD("/fxml/Card.fxml"),
    DECKS_BOARD("/fxml/DecksBoard.fxml");

    private final String value;

    SceneNames(final String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
