package it.polimi.ingsw.utils.listeners.client;

import it.polimi.ingsw.client.gui.controllers.PlayerBoardController;

import java.beans.PropertyChangeEvent;
import java.util.List;

public class DevelopmentBSpacesViewListener extends PlayerBoardViewChangeListener {

    public DevelopmentBSpacesViewListener(PlayerBoardController playerBoardController) {
        super(playerBoardController);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void propertyChange(PropertyChangeEvent evt) {
        getPlayerBoardController().setDevelopmentBSpaces((List<List<Integer>>) evt.getNewValue());
    }
}