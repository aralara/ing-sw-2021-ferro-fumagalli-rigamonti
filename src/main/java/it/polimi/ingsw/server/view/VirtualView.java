package it.polimi.ingsw.server.view;

import it.polimi.ingsw.server.ClientHandler;
import it.polimi.ingsw.utils.messages.Message;

public class VirtualView {

    private final String nickname;
    private final ClientHandler client;

    public VirtualView(String nickname, ClientHandler client) {
        this.nickname = nickname;
        this.client = client;
    }

    public String getNickname() {
        return nickname;
    }

    public ClientHandler getClient() {
        return client;
    }

    public void sendMessage(Message message) {
        client.sendMessage(message);
    }
}
