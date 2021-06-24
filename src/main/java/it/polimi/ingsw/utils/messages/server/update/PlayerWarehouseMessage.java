package it.polimi.ingsw.utils.messages.server.update;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.utils.exceptions.NotExistingNicknameException;
import it.polimi.ingsw.server.model.storage.Shelf;

import java.util.List;

/**
 * Server update message for the player's warehouse
 */
public class PlayerWarehouseMessage implements ServerUpdateMessage {

    private final List<Shelf> shelves;
    private final String nickname;


    /**
     * Constructor for a PlayerWarehouseMessage given a list of shelves and a nickname
     * @param shelves Player's resources, contained in the shelves, contained in the Warehouse
     * @param nickname Nickname of the player
     */
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
