package it.polimi.ingsw.utils.messages.client;

import it.polimi.ingsw.utils.messages.Message;

public class NewLobbyMessage implements Message {

    private int lobbySize;


    public NewLobbyMessage(int lobbySize) {
        this.lobbySize = lobbySize;
    }


    public int getLobbySize() {
        return lobbySize;
    }
}
