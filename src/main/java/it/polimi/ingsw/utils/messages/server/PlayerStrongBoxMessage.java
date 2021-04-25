package it.polimi.ingsw.utils.messages.server;

import it.polimi.ingsw.server.model.storage.Strongbox;
import it.polimi.ingsw.utils.messages.Message;

public class PlayerStrongBoxMessage implements Message {

    private Strongbox strongbox;
    private String nickname;


    public PlayerStrongBoxMessage(Strongbox developmentBoard, String nickname) {
        this.strongbox = developmentBoard;
        this.nickname = nickname;
    }


    public Strongbox getStrongbox() {
        return strongbox;
    }

    public String getNickname(){
        return nickname;
    }
}
