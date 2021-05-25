package it.polimi.ingsw.utils.listeners.client;

import it.polimi.ingsw.client.gui.controllers.PlayerBoardController;

import java.beans.PropertyChangeEvent;
import java.util.List;

public class LeaderBBoardViewListener extends PlayerBoardViewChangeListener {

    public LeaderBBoardViewListener(PlayerBoardController playerBoardController) {
        super(playerBoardController);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void propertyChange(PropertyChangeEvent evt) {
        getPlayerBoardController().setLeaderBBoard((List<Integer>)evt.getNewValue());
    }
}
