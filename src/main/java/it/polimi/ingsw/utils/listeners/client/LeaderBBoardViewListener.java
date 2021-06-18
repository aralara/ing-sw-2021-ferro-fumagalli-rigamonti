package it.polimi.ingsw.utils.listeners.client;

import it.polimi.ingsw.client.gui.controllers.PlayerBoardController;

import java.beans.PropertyChangeEvent;

/**
 * TODO: fare javadoc
 */
public class LeaderBBoardViewListener extends PlayerBoardViewChangeListener {

    public LeaderBBoardViewListener(PlayerBoardController playerBoardController) {
        super(playerBoardController);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void propertyChange(PropertyChangeEvent evt) {
        getPlayerBoardController().showLeaderBBoard();
    }
}
