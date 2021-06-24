package it.polimi.ingsw.utils.messages.client;

import it.polimi.ingsw.client.ClientController;

/**
 * Client message that establishes a connection to the server
 */
public class ConnectionMessage extends ClientSetupMessage {

    private final String nickname;


    /**
     * Constructor for a ConnectionMessage given a client
     * @param nickname Nickname of the player controlling the client
     */
    public ConnectionMessage(String nickname) {
        this.nickname = nickname;
    }


    @Override
    public String doSetup() {
        return nickname;
    }

    @Override
    public void doACKResponseAction(ClientController client) {
        client.ackNotification("Nickname set successfully", false);
    }

    @Override
    public void doNACKResponseAction(ClientController client) {
        client.ackNotification("The selected nickname is not available or contains unsupported characters",
                true);
        client.askNickname();
    }
}
