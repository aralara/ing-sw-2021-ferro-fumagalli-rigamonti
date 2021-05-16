package it.polimi.ingsw.utils.messages.server;

import it.polimi.ingsw.client.ClientController;

public class NewPlayerMessage implements ServerActionMessage {

    private final String playerNickname;


    public NewPlayerMessage(String playerNickname) {
        this.playerNickname = playerNickname;
    }


    @Override
    public void doAction(ClientController client) {
        client.notifyNewPlayer(playerNickname);
    }
}
