package it.polimi.ingsw.utils.messages.client;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.server.controller.GameHandler;
import it.polimi.ingsw.server.controller.Controller;
import it.polimi.ingsw.server.view.VirtualView;
import it.polimi.ingsw.utils.TurnStatus;
import it.polimi.ingsw.utils.messages.server.ack.ServerAckMessage;
import it.polimi.ingsw.utils.messages.server.action.EndGameMessage;
import it.polimi.ingsw.utils.messages.server.action.LastRoundMessage;
import it.polimi.ingsw.utils.messages.server.action.StartTurnMessage;

/**
 * TODO: fare javadoc
 */
public class EndTurnMessage extends ClientActionMessage {

    public EndTurnMessage() { }


    @Override
    public void doAction(VirtualView view) {
        GameHandler gameHandler = view.getGameHandler();
        Controller controller = gameHandler.getController();
        TurnStatus ts = TurnStatus.getStatus(controller.loadNextTurn());
        boolean success = true;
        switch(ts) {
            case LOAD_TURN_NORMAL:
                gameHandler.sendAll(new StartTurnMessage(controller.getPlayingNickname()));
                break;
            case LOAD_TURN_LAST_ROUND:
                gameHandler.sendAll(new LastRoundMessage());
                gameHandler.sendAll(new StartTurnMessage(controller.getPlayingNickname()));
                break;
            case LOAD_TURN_END_GAME:
                gameHandler.sendAll(new EndGameMessage(controller.getGame().getEndPlayerList()));
                break;
            case INVALID:
                success = false;
                break;
        }
        view.sendMessage(new ServerAckMessage(getUuid(), success));
    }

    @Override
    public void doACKResponseAction(ClientController client) {
        client.setMainActionPlayed(false);
    }

    @Override
    public void doNACKResponseAction(ClientController client) {
        client.ackNotification("Cannot end turn", true);
    }
}
