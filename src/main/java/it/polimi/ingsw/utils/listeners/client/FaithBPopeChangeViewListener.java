package it.polimi.ingsw.utils.listeners.client;

import it.polimi.ingsw.client.gui.controllers.PlayerBoardController;

import java.beans.PropertyChangeEvent;

/**
 * Listener for the faith board's pope progression in a PlayerBoardView
 */
public class FaithBPopeChangeViewListener extends PlayerBoardViewChangeListener {

    /**
     * Constructor for the listener
     * @param playerBoardController Associated GUI Controller
     */
    public FaithBPopeChangeViewListener(PlayerBoardController playerBoardController) {
        super(playerBoardController);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        getPlayerBoardController().showFaithBPope();
    }
}