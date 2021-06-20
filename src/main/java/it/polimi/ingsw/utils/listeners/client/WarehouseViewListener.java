package it.polimi.ingsw.utils.listeners.client;

import it.polimi.ingsw.client.gui.controllers.PlayerBoardController;

import java.beans.PropertyChangeEvent;

/**
 * Listener for the warehouse in a PlayerBoardView
 */
public class WarehouseViewListener extends PlayerBoardViewChangeListener {

    /**
     * Constructor for the listener
     * @param playerBoardController Associated GUI Controller
     */
    public WarehouseViewListener(PlayerBoardController playerBoardController) {
        super(playerBoardController);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        getPlayerBoardController().showWarehouse();
    }
}
