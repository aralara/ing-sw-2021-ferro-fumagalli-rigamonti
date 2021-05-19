package it.polimi.ingsw.client.gui;

public enum SceneNames {
    CONNECTION_MENU("/fxml/ConnectionMenu.fxml"),
    NICKNAME_MENU("/fxml/NicknameMenu.fxml"),
    GAME_MODE_MENU("/fxml/GameModeMenu.fxml"),
    SINGLE_PLAYER_MENU("/fxml/SinglePlayerMenu.fxml"),
    MULTI_PLAYER_MENU("/fxml/MultiPlayerMenu.fxml"),
    MULTI_PLAYER_WAITING("/fxml/MultiPlayerWaiting.fxml"),
    PLAYER_BOARD("/fxml/PlayerBoard.fxml"),
    MARKET_BOARD("/fxml/MarketBoard.fxml"),
    DECKS_BOARD("/fxml/DecksBoard.fxml");

    private final String value;

    SceneNames(final String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
