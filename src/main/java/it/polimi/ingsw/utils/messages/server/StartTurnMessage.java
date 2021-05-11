package it.polimi.ingsw.utils.messages.server;

import it.polimi.ingsw.client.cli.CLI;

public class StartTurnMessage implements ServerActionMessage { //TODO: implements da Message a que'... RIGA CONTROLLAAAA grazie c:

    private String playingNickname;


    public StartTurnMessage(String playingNickname) {
        this.playingNickname = playingNickname;
    }


    public String getPlayingNickname() {
        return playingNickname;
    }

    @Override
    public void doAction(CLI client) {
        client.chooseAction(this);
    }
}
