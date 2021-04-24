package it.polimi.ingsw.utils.messages.client;

import it.polimi.ingsw.utils.messages.Message;

public class EndTurnMessage implements Message {

    private String nickname;


    public EndTurnMessage(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }
}
