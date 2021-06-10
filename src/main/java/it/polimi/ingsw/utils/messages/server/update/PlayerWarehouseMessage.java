package it.polimi.ingsw.utils.messages.server.update;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.utils.exceptions.NotExistingNicknameException;
import it.polimi.ingsw.server.model.storage.Shelf;

import java.util.List;

public class PlayerWarehouseMessage implements ServerUpdateMessage {

    private final List<Shelf> shelves;
    private final String nickname;


    public PlayerWarehouseMessage(List<Shelf> shelves, String nickname) {
        this.shelves = shelves;
        this.nickname = nickname;
    }


    @Override
    public void doUpdate(ClientController client) {
        try {
            client.playerBoardFromNickname(nickname).getWarehouse().setShelves(shelves);
        } catch(NotExistingNicknameException e){
            e.printStackTrace();
        }
    }
}
