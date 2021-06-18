package it.polimi.ingsw.utils.messages.server.update;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.utils.exceptions.NotExistingNicknameException;
import it.polimi.ingsw.server.model.storage.Resource;

import java.util.List;

/**
 * Server update message for the player's strongbox
 */
public class PlayerStrongBoxMessage implements ServerUpdateMessage {

    private final List<Resource> resources;
    private final String nickname;


    public PlayerStrongBoxMessage(List<Resource> resources, String nickname) {
        this.resources = resources;
        this.nickname = nickname;
    }


    @Override
    public void doUpdate(ClientController client) {
        try {
            client.playerBoardFromNickname(nickname).getStrongbox().setResources(resources);
        } catch(NotExistingNicknameException e){
            e.printStackTrace();
        }
    }
}
