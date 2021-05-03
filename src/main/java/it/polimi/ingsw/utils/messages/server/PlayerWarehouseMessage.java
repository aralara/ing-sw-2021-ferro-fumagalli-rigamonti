package it.polimi.ingsw.utils.messages.server;

import it.polimi.ingsw.server.model.storage.Shelf;
import it.polimi.ingsw.utils.messages.Message;

import java.util.List;

public class PlayerWarehouseMessage implements Message {

    private List<Shelf> shelves;
    private String nickname;


    public PlayerWarehouseMessage(List<Shelf> shelves, String nickname) {
        this.shelves = shelves;
        this.nickname = nickname;
    }


    public List<Shelf> getShelves() {
        return shelves;
    }

    public String getNickname(){
        return nickname;
    }
}
