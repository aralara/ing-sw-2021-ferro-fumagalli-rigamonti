package it.polimi.ingsw.utils.messages.client;

import it.polimi.ingsw.utils.messages.Message;

public class ConnectionMessage implements Message {

    private String nickname;


    public ConnectionMessage(String nickname) {
        this.nickname = nickname;
    }


    public String getNickname() {
        return nickname;
    }
}
