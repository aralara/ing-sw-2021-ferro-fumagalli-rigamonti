package it.polimi.ingsw.utils.messages.server;

import it.polimi.ingsw.server.model.boards.FaithBoard;
import it.polimi.ingsw.utils.messages.Message;

public class PlayerFaithBoardMessage implements Message {

    private FaithBoard faithBoard;
    private String nickname;


    public PlayerFaithBoardMessage(FaithBoard faithBoard, String nickname) {
        this.faithBoard = faithBoard;
        this.nickname = nickname;
    }


    public FaithBoard getFaithBoard() {
        return faithBoard;
    }

    public String getNickname() {
        return nickname;
    }
}
