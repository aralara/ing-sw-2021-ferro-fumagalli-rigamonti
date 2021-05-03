package it.polimi.ingsw.utils.messages.server;

import it.polimi.ingsw.server.model.storage.Resource;
import it.polimi.ingsw.utils.messages.Message;

import java.util.List;

public class PlayerStrongBoxMessage implements Message {

    private List<Resource> resources;
    private String nickname;


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
}
