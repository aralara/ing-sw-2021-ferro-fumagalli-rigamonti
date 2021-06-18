package it.polimi.ingsw.utils.messages.client;

import it.polimi.ingsw.client.ClientController;

/**
 * TODO: fare javadoc
 */
public class ConnectionMessage extends ClientSetupMessage {

    private final String nickname;


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
