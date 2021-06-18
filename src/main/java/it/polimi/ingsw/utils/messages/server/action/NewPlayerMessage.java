package it.polimi.ingsw.utils.messages.server.action;

import it.polimi.ingsw.client.ClientController;

/**
 * Server message that notifies the player of another new player entering their lobby
 */
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
