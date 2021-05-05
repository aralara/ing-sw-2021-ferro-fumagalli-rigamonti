package it.polimi.ingsw.utils.messages.server;

import it.polimi.ingsw.server.model.boards.FaithBoard;
import it.polimi.ingsw.utils.messages.Message;

public class PlayerFaithBPopeMessage implements Message {
    private boolean[] popeProgression;
    private String nickname;


    public PlayerFaithBPopeMessage(boolean[] popeProgression, String nickname) {
        this.popeProgression = new boolean[FaithBoard.POPE_PROGRESSION_SIZE];
        System.arraycopy(popeProgression, 0, this.popeProgression, 0, FaithBoard.POPE_PROGRESSION_SIZE);
        this.nickname = nickname;
    }


    public boolean[] getPopeProgression() {
        boolean[] popeProgressionCopy = new boolean[FaithBoard.POPE_PROGRESSION_SIZE];
        System.arraycopy(this.popeProgression, 0, popeProgressionCopy, 0, FaithBoard.POPE_PROGRESSION_SIZE);
        return popeProgressionCopy;
    }

    public String getNickname() {
        return nickname;
    }
}
