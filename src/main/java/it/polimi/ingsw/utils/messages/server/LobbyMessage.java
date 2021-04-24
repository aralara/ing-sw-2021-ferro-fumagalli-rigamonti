package it.polimi.ingsw.utils.messages.server;

import it.polimi.ingsw.utils.messages.Message;

public class LobbyMessage implements Message {

    private int lobbySize, waitingPlayers;  // If lobbySize is 0 the client must create a new lobby


    public LobbyMessage(int lobbySize, int waitingPlayers){
        this.lobbySize = lobbySize;
        this.waitingPlayers = waitingPlayers;
    }


    public int getLobbySize() {
        return lobbySize;
    }

    public int getWaitingPlayers() {
        return waitingPlayers;
    }
}
