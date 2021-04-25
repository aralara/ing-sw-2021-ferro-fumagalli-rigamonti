package it.polimi.ingsw.utils.messages.server;

import it.polimi.ingsw.server.model.boards.DevelopmentBoard;
import it.polimi.ingsw.utils.messages.Message;

public class PlayerDevelopmentBoardMessage implements Message {

    private DevelopmentBoard developmentBoard;
    private String nickname;


    public PlayerDevelopmentBoardMessage(DevelopmentBoard developmentBoard, String nickname) {
        this.developmentBoard = developmentBoard;
        this.nickname = nickname;
    }


    public DevelopmentBoard getDevelopmentBoard() {
        return developmentBoard;
    }

    public String getNickname(){
        return nickname;
    }
}
