package it.polimi.ingsw.utils.listeners.client;

import it.polimi.ingsw.client.gui.controllers.PlayerBoardController;

import java.beans.PropertyChangeEvent;

/**
 * Listener for the strongbox in a PlayerBoardView
 */
public class StrongboxViewListener extends PlayerBoardViewChangeListener {

    /**
     * Constructor for the listener
     * @param playerBoardController Associated GUI Controller
     */
    public StrongboxViewListener(PlayerBoardController playerBoardController) {
        super(playerBoardController);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        getPlayerBoardController().showStrongbox();
    }
}
