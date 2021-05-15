package it.polimi.ingsw.utils.messages.server;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.client.cli.CLI;
import it.polimi.ingsw.client.cli.GraphicalCLI;

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
    public void doAction(ClientController client) {
        GraphicalCLI graphicalCLI = ((CLI) client).getGraphicalCLI();   //TODO: CAST A CLI ORRENDI, BRUTTI E ASSOLUTAMENTE TEMPORANEI IN TUTTO IL METODO
        if (lobbySize == waitingPlayers)
            ((CLI) client).createNewLobby();
        else {
            graphicalCLI.printString("There's already a " + lobbySize + " player lobby waiting for ");
            if(lobbySize - waitingPlayers == 1)
                graphicalCLI.printString("another player\n");
            else
                graphicalCLI.printString((lobbySize - waitingPlayers) + " more players\n");
            client.setNumberOfPlayers(lobbySize);
        }
    }
}
