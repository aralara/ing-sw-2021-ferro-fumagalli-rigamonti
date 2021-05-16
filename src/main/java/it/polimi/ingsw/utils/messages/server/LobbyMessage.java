package it.polimi.ingsw.utils.messages.server;

import it.polimi.ingsw.client.ClientController;

public class LobbyMessage implements ServerActionMessage {

    private final int lobbySize, waitingPlayers;  // If lobbySize is 0 the client must create a new lobby


    public LobbyMessage(int lobbySize, int waitingPlayers){
        this.lobbySize = lobbySize;
        this.waitingPlayers = waitingPlayers;
    }


    @Override
    public void doAction(ClientController client) {
        client.askNewLobby(lobbySize, waitingPlayers);
    }
}
