package it.polimi.ingsw.utils.messages.server;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.exceptions.NotExistingNickname;
import it.polimi.ingsw.server.model.storage.Shelf;

import java.util.List;

public class PlayerWarehouseMessage implements ServerUpdateMessage {

    private final List<Shelf> shelves;
    private final String nickname;


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

    @Override
    public void doUpdate(ClientController client) {
        try {
            client.playerBoardFromNickname(nickname).getWarehouse().setShelves(shelves);
        } catch(NotExistingNickname e){
            e.printStackTrace();
        }
    }
}
