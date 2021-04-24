package it.polimi.ingsw.utils.messages.server;

import it.polimi.ingsw.server.model.boards.DevelopmentBoard;
import it.polimi.ingsw.utils.messages.Message;

public class PlayerDevelopmentBoardMessage implements Message {

    private DevelopmentBoard developmentBoard;


    public PlayerDevelopmentBoardMessage(DevelopmentBoard developmentBoard) {
        this.developmentBoard = developmentBoard;
    }


    public DevelopmentBoard getDevelopmentBoard() {
        return developmentBoard;
    }
}
