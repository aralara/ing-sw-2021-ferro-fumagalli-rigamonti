package it.polimi.ingsw.utils.messages.server;

import it.polimi.ingsw.utils.messages.Message;

public class NewPlayerMessage implements Message {

    private String playerNickname;


    public NewPlayerMessage(String playerNickname) {
        this.playerNickname = playerNickname;
    }


    public String getPlayerNickname() {
        return playerNickname;
    }
}
