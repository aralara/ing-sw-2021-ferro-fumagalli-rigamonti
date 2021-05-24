package it.polimi.ingsw.utils.messages.client;

import it.polimi.ingsw.client.ClientController;

public class NewLobbyMessage extends ClientSetupMessage {

    private final int lobbySize;


    public NewLobbyMessage(int lobbySize) {
        this.lobbySize = lobbySize;
    }


    @Override
    public Integer doSetup() {
        return lobbySize;
    }

    @Override
    public void doACKResponseAction(ClientController client) {
        //client.ackNotification("Lobby size set successfully");  //TODO: tolta perchè nella gui rimane brutto, meglio avere meno opo up, tanto si capisce se uno ha settato la size in maniera corretta, meglio mandare messaggio solo di nack
    }

    @Override
    public void doNACKResponseAction(ClientController client) {
        client.ackNotification("Error in setting lobby size");
    }
}
