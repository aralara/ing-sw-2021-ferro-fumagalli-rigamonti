package it.polimi.ingsw.utils.messages.server;

import it.polimi.ingsw.server.model.storage.Warehouse;
import it.polimi.ingsw.utils.messages.Message;

public class PlayerWarehouseMessage implements Message {

    private Warehouse developmentBoard;


    public PlayerWarehouseMessage(Warehouse developmentBoard) {
        this.developmentBoard = developmentBoard;
    }


    public Warehouse getDevelopmentBoard() {
        return developmentBoard;
    }
}
