package it.polimi.ingsw.utils.messages.server;

import it.polimi.ingsw.server.model.storage.Strongbox;
import it.polimi.ingsw.utils.messages.Message;

public class PlayerStrongBoxMessage implements Message {

    private Strongbox developmentBoard;


    public PlayerStrongBoxMessage(Strongbox developmentBoard) {
        this.developmentBoard = developmentBoard;
    }


    public Strongbox getDevelopmentBoard() {
        return developmentBoard;
    }
}
