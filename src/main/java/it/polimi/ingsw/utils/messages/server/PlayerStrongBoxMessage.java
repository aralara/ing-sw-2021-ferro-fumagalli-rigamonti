package it.polimi.ingsw.utils.messages.server;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.exceptions.NotExistingNickname;
import it.polimi.ingsw.server.model.storage.Resource;

import java.util.List;

public class PlayerStrongBoxMessage implements ServerUpdateMessage {

    private final List<Resource> resources;
    private final String nickname;


    public PlayerStrongBoxMessage(List<Resource> resources, String nickname) {
        this.resources = resources;
        this.nickname = nickname;
    }


    public List<Resource> getResources() {
        return resources;
    }

    public String getNickname(){
        return nickname;
    }

    @Override
    public void doUpdate(ClientController client) {
        try {
            client.playerBoardFromNickname(nickname).getStrongbox().setResources(resources);
        } catch(NotExistingNickname e){
            e.printStackTrace();
        }
    }
}
