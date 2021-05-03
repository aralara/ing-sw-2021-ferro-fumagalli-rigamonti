package it.polimi.ingsw.utils.messages.server;

import it.polimi.ingsw.server.model.boards.FaithBoard;
import it.polimi.ingsw.utils.messages.Message;

public class PlayerFaithBoardFaithMessage implements Message {

    private int faith;
    private String nickname;


    public PlayerFaithBoardFaithMessage(int faith, String nickname) {
        this.faith = faith;
        this.nickname = nickname;
    }


    public int getFaith() {
        return faith;
    }

    public String getNickname() {
        return nickname;
    }
}
