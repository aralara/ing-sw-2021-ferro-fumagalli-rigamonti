package it.polimi.ingsw.utils.messages.client;

import it.polimi.ingsw.client.ClientController;

/**
 * Client message that chooses a size for a new lobby
 */
public class NewLobbyMessage extends ClientSetupMessage {

    private final int lobbySize;


    /**
     * Constructor for a NewLobbyMessage given a new lobbySize
     * @param lobbySize Size of the lobby that will be created
     */
    public NewLobbyMessage(int lobbySize) {
        this.lobbySize = lobbySize;
    }


    @Override
    public Integer doSetup() {
        return lobbySize;
    }

    @Override
    public void doACKResponseAction(ClientController client) {
        client.ackNotification("Lobby size set successfully", false);
    }

    @Override
    public void doNACKResponseAction(ClientController client) {
        client.ackNotification("Error in setting lobby size", true);
        client.askNewLobby(0, 0);   // Simulating a new lobby message
    }
}
