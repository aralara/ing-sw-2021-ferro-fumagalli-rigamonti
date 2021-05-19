package it.polimi.ingsw.client.gui;

public enum SceneNames {
    CONNECTION_MENU("/fxml/ConnectionMenu.fxml"),
    NICKNAME_MENU("/fxml/NicknameMenu.fxml");

    private final String value;

    SceneNames(final String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
