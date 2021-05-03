package it.polimi.ingsw.server.view;

import it.polimi.ingsw.server.ClientHandler;
import it.polimi.ingsw.utils.messages.Message;

public class VirtualView {

    private String nickname;
    private ClientHandler client;

    public VirtualView(String nickname, ClientHandler client) {
        this.nickname = nickname;
        this.client = client;
    }

    public void sendUpdateMessage(Message message) {
        //TODO: metodo per inviare messaggio di update al client
    }
}
