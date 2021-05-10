package it.polimi.ingsw.utils.messages.server;

import it.polimi.ingsw.client.cli.CLI;

public class LobbyMessage implements ServerActionMessage {

    private final int lobbySize, waitingPlayers;  // If lobbySize is 0 the client must create a new lobby


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

    @Override
    public void doAction(CLI client) {     //TODO: ci sono print nel messaggio
        if (lobbySize == waitingPlayers)
            client.createNewLobby();
        else {
            System.out.print("There's already a " + lobbySize + " player lobby waiting for ");
            if(lobbySize - waitingPlayers == 1)
                System.out.println("another player");
            else
                System.out.println((lobbySize - waitingPlayers) + " more players");
            client.setNumberOfPlayers(lobbySize);
        }
    }
}
