package it.polimi.ingsw.utils.listeners.client;

import it.polimi.ingsw.client.gui.controllers.PlayerBoardController;
import it.polimi.ingsw.server.model.storage.Resource;

import java.beans.PropertyChangeEvent;
import java.util.List;

public class StrongboxViewListener extends PlayerBoardViewChangeListener {

    public StrongboxViewListener(PlayerBoardController playerBoardController) {
        super(playerBoardController);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void propertyChange(PropertyChangeEvent evt) {
        getPlayerBoardController().setStrongbox((List<Resource>) evt.getNewValue());
    }
}
