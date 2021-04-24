package it.polimi.ingsw.utils.messages.server;

import it.polimi.ingsw.utils.messages.Message;

public class StartTurnMessage implements Message {

    private String playingNickname;


    public StartTurnMessage(String playingNickname) {
        this.playingNickname = playingNickname;
    }


    public String getPlayingNickname() {
        return playingNickname;
    }
}
