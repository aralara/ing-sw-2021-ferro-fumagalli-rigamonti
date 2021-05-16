package it.polimi.ingsw.utils.messages.client;

import it.polimi.ingsw.server.controller.Controller;
import it.polimi.ingsw.server.view.VirtualView;
import it.polimi.ingsw.utils.messages.server.action.StartTurnMessage;

public class EndTurnMessage implements ClientActionMessage {

    private final String nickname;


    public EndTurnMessage(String nickname) {
        this.nickname = nickname;
    }


    public String getNickname() {
        return nickname;
    }

    @Override
    public void doAction(VirtualView view, Controller controller) {
        controller.loadNextTurn();
        view.getGameHandler().sendAll(new StartTurnMessage(controller.getPlayingNickname()));
    }
}
