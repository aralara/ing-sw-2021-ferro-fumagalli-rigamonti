package it.polimi.ingsw.utils.messages.server;

import it.polimi.ingsw.server.model.storage.Warehouse;
import it.polimi.ingsw.utils.messages.Message;

public class PlayerWarehouseMessage implements Message {

    private Warehouse warehouse;
    private String nickname;


    public PlayerWarehouseMessage(Warehouse developmentBoard, String nickname) {
        this.warehouse = developmentBoard;
        this.nickname = nickname;
    }


    public Warehouse getWarehouse() {
        return warehouse;
    }

    public String getNickname(){
        return nickname;
    }
}
