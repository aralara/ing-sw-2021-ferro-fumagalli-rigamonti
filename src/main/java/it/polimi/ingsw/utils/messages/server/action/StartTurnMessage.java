package it.polimi.ingsw.utils.messages.server.action;

import it.polimi.ingsw.client.ClientController;

/**
 * Server message that notifies the player when it's the turn of a particular player
 */
public class StartTurnMessage implements ServerActionMessage {

    private final String playingNickname;


    /**
     * Constructor for a StartTurnMessage given a nickname
     * @param playingNickname Nickname of the player starting their turn
     */
    public StartTurnMessage(String playingNickname) {
        this.playingNickname = playingNickname;
    }


    @Override
    public void doAction(ClientController client) {
        client.notifyStartTurn(playingNickname);
    }
}
