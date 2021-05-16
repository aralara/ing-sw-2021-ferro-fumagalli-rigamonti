package it.polimi.ingsw.utils.messages.server.action;

import it.polimi.ingsw.client.ClientController;

public class StartTurnMessage implements ServerActionMessage {

    private final String playingNickname;


    public StartTurnMessage(String playingNickname) {
        this.playingNickname = playingNickname;
    }


    public String getPlayingNickname() {
        return playingNickname;
    }

    @Override
    public void doAction(ClientController client) {
        client.notifyStartTurn(playingNickname);
    }
}
