package it.polimi.ingsw.utils.messages.server;

import it.polimi.ingsw.utils.messages.Message;

import java.sql.Array;

public class PlayerFaithBoardPopeMessage implements Message {
    private static final int POPE_PROGRESSION_SIZE = 3;

    private boolean[] popeProgression;
    private String nickname;


    public PlayerFaithBoardPopeMessage(boolean[] popeProgression, String nickname) {
        this.popeProgression = new boolean[POPE_PROGRESSION_SIZE];
        System.arraycopy(popeProgression, 0, this.popeProgression, 0, POPE_PROGRESSION_SIZE);
        this.nickname = nickname;
    }


    public boolean[] getPopeProgression() {
        boolean[] popeProgressionCopy = new boolean[POPE_PROGRESSION_SIZE];
        System.arraycopy(this.popeProgression, 0, popeProgressionCopy, 0, POPE_PROGRESSION_SIZE);
        return popeProgressionCopy;
    }

    public String getNickname() {
        return nickname;
    }
}
