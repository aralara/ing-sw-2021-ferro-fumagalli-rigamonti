package it.polimi.ingsw.utils.listeners.client;

import it.polimi.ingsw.client.gui.controllers.PlayerBoardController;

import java.beans.PropertyChangeEvent;

public class LeaderBHandViewListener extends PlayerBoardViewChangeListener {

    public LeaderBHandViewListener(PlayerBoardController playerBoardController) {
        super(playerBoardController);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void propertyChange(PropertyChangeEvent evt) {
        getPlayerBoardController().showLeaderBHand();
    }
}
