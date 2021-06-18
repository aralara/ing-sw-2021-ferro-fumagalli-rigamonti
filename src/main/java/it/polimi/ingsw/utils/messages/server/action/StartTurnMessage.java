package it.polimi.ingsw.utils.messages.server.action;

import it.polimi.ingsw.client.ClientController;

/**
 * Server message that notifies the player when it's the turn of a particular player
 */
public class StartTurnMessage implements ServerActionMessage {

    private final String playingNickname;


    public StartTurnMessage(String playingNickname) {
        this.playingNickname = playingNickname;
    }


    @Override
    public void doAction(ClientController client) {
        client.notifyStartTurn(playingNickname);
    }
}
