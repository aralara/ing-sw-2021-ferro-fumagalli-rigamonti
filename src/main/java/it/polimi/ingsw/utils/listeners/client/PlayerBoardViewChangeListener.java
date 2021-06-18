package it.polimi.ingsw.utils.listeners.client;

import it.polimi.ingsw.client.gui.controllers.PlayerBoardController;

import java.beans.PropertyChangeListener;

/**
 * TODO: fare javadoc
 */
public abstract class PlayerBoardViewChangeListener implements PropertyChangeListener {

    private final PlayerBoardController playerBoardController;

    public PlayerBoardViewChangeListener(PlayerBoardController playerBoardController) {
        this.playerBoardController = playerBoardController;
    }

    /**
     * Gets the playerBoardController attribute
     * @return Returns playerBoardController value
     */
    public PlayerBoardController getPlayerBoardController() {
        return playerBoardController;
    }
}
