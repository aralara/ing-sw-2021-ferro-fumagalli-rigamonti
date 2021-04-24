package it.polimi.ingsw.utils.messages.server;

import it.polimi.ingsw.server.model.boards.FaithBoard;
import it.polimi.ingsw.utils.messages.Message;

public class PlayerFaithBoardMEssage implements Message {

    private FaithBoard faithBoard;


    public PlayerFaithBoardMEssage(FaithBoard faithBoard) {
        this.faithBoard = faithBoard;
    }


    public FaithBoard getFaithBoard() {
        return faithBoard;
    }
}
