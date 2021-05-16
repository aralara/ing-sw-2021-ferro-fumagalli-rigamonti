package it.polimi.ingsw.utils.messages.server.action;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.utils.messages.server.action.ServerActionMessage;

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
