package it.polimi.ingsw.server.view;

import it.polimi.ingsw.server.GameHandler;
import it.polimi.ingsw.utils.messages.Message;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class VirtualView extends ClientHandler{

    private final String nickname;
    private final GameHandler gameHandler;

    public VirtualView(Socket client, ObjectOutputStream out, ObjectInputStream in, String nickname, GameHandler gameHandler) {
        super(client, out, in);
        this.nickname = nickname;
        this.gameHandler = gameHandler;
    }

    public String getNickname() {
        return nickname;
    }

    @Override
    public void onMessageReceived(Message message) {
        gameHandler.handleMessage(this, message);
    }
}
