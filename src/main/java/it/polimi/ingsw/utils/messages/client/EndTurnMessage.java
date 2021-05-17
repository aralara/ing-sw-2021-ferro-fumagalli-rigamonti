package it.polimi.ingsw.utils.messages.client;

import it.polimi.ingsw.server.controller.Controller;
import it.polimi.ingsw.server.model.boards.PlayerBoard;
import it.polimi.ingsw.server.view.VirtualView;
import it.polimi.ingsw.utils.Constants;
import it.polimi.ingsw.utils.TurnStatus;
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
        TurnStatus ts = TurnStatus.getStatus(controller.loadNextTurn());
        switch(ts){
            case LOAD_TURN_NORMAL:
                view.getGameHandler().sendAll(new StartTurnMessage(controller.getPlayingNickname()));
                break;
            case LOAD_TURN_LAST_ROUND:
                view.getGameHandler().sendAll(new LastRoundMessage());
                view.getGameHandler().sendAll(new StartTurnMessage(controller.getPlayingNickname()));
                break;
            case LOAD_TURN_END_GAME:
                view.getGameHandler().sendAll(new EndGameMessage(controller.getGame().
                        getPlayerBoards().stream().map(PlayerBoard::getPlayer).collect(Collectors.toList())));
                break;
        }
    }
}
