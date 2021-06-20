package it.polimi.ingsw.utils.listeners.client;

import it.polimi.ingsw.client.gui.controllers.PlayerBoardController;

import java.beans.PropertyChangeEvent;

/**
 * Listener for the leader board's hand progression in a PlayerBoardView
 */
public class LeaderBHandViewListener extends PlayerBoardViewChangeListener {

    /**
     * Constructor for the listener
     * @param playerBoardController Associated GUI Controller
     */
    public LeaderBHandViewListener(PlayerBoardController playerBoardController) {
        super(playerBoardController);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        getPlayerBoardController().showLeaderBHand();
    }
}
