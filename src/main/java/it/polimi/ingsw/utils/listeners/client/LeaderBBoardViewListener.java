package it.polimi.ingsw.utils.listeners.client;

import it.polimi.ingsw.client.gui.controllers.PlayerBoardController;

import java.beans.PropertyChangeEvent;

/**
 * Listener for the leader board's board in a PlayerBoardView
 */
public class LeaderBBoardViewListener extends PlayerBoardViewChangeListener {

    /**
     * Constructor for the listener
     * @param playerBoardController Associated GUI Controller
     */
    public LeaderBBoardViewListener(PlayerBoardController playerBoardController) {
        super(playerBoardController);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        getPlayerBoardController().showLeaderBBoard();
    }
}
