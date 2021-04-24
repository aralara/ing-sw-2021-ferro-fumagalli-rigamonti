package it.polimi.ingsw.utils.messages.server;

import it.polimi.ingsw.utils.messages.Message;

public class PlayerDisconnectedMessage implements Message {

    private String nickname;


    public PlayerDisconnectedMessage(String nickname) {
        this.nickname = nickname;
    }


    public String getNickname() {
        return nickname;
    }
}
