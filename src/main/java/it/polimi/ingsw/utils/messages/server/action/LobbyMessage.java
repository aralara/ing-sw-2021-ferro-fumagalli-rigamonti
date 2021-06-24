package it.polimi.ingsw.utils.messages.server.action;

import it.polimi.ingsw.client.ClientController;

/**
 * Server message that gives information to the player about the lobby size
 */
public class LobbyMessage implements ServerActionMessage {

    private final int lobbySize, waitingPlayers;  // If lobbySize is 0 the client must create a new lobby


    /**
     * Constructor for a LobbyMessage given its size and the waiting players
     * @param lobbySize Size of the already present lobby (0 if it doesn't exist)
     * @param waitingPlayers Number of waiting players inside the lobby
     */
    public LobbyMessage(int lobbySize, int waitingPlayers){
        this.lobbySize = lobbySize;
        this.waitingPlayers = waitingPlayers;
    }


    @Override
    public void doAction(ClientController client) {
        client.askNewLobby(lobbySize, waitingPlayers);
    }
}
