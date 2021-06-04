package it.polimi.ingsw.utils.listeners.client;

import it.polimi.ingsw.client.gui.controllers.PlayerBoardController;

import java.beans.PropertyChangeEvent;
import java.util.List;

public class LeaderBHandViewListener extends PlayerBoardViewChangeListener {

    public LeaderBHandViewListener(PlayerBoardController playerBoardController) {
        super(playerBoardController);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void propertyChange(PropertyChangeEvent evt) {
        getPlayerBoardController().setLeaderBHand(/*(List<Integer>)evt.getNewValue()*/);
    }
}
