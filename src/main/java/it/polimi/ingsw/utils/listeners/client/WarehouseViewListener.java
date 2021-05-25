package it.polimi.ingsw.utils.listeners.client;

import it.polimi.ingsw.client.gui.controllers.PlayerBoardController;
import it.polimi.ingsw.server.model.storage.Shelf;

import java.beans.PropertyChangeEvent;
import java.util.List;

public class WarehouseViewListener extends PlayerBoardViewChangeListener {

    public WarehouseViewListener(PlayerBoardController playerBoardController) {
        super(playerBoardController);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void propertyChange(PropertyChangeEvent evt) {
        getPlayerBoardController().setWarehouse((List<Shelf>) evt.getNewValue());
    }
}
