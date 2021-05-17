package it.polimi.ingsw.utils.messages.client;

import it.polimi.ingsw.server.controller.Controller;
import it.polimi.ingsw.server.view.VirtualView;
import it.polimi.ingsw.utils.messages.server.action.EndGameMessage;
import it.polimi.ingsw.utils.messages.server.action.LastRoundMessage;
import it.polimi.ingsw.utils.messages.server.action.StartTurnMessage;

import java.util.stream.Collectors;

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
        int temp = controller.loadNextTurn();
        if(temp == 1){
            view.getGameHandler().sendAll(new StartTurnMessage(controller.getPlayingNickname()));
        }else if (temp == 2){
            view.getGameHandler().sendAll(new LastRoundMessage());
            view.getGameHandler().sendAll(new StartTurnMessage(controller.getPlayingNickname()));
        }else if(temp == 3){
            view.getGameHandler().sendAll(new EndGameMessage(  //TODO: controllare se va bene o fa schifo
                    controller.getGame().getPlayerBoards().stream().map(x->x.getPlayer()).collect(Collectors.toList())));
        }
    }
}
