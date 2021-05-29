package it.polimi.ingsw.server.view;

import it.polimi.ingsw.server.GameHandler;
import it.polimi.ingsw.utils.messages.Message;

public abstract class VirtualView implements Runnable {

    private String nickname;
    private GameHandler gameHandler;

    public abstract void sendMessage(Message message);

    public abstract void onMessageReceived(Message message);

    @Override
    public void run() {

    }

    public void stop(boolean propagate) {

    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public GameHandler getGameHandler() {
        return  gameHandler;
    }

    public void setGameHandler(GameHandler gameHandler) {
        this.gameHandler = gameHandler;
    }
}
