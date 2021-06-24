package it.polimi.ingsw.utils.messages.server.action;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.server.model.boards.Player;

import java.util.List;

/**
 * Server message that notifies the client of the game's end via natural causes or disconnection
 */
public class EndGameMessage implements ServerActionMessage {

    private final List<Player> players;
    private final boolean disconnection;


    public EndGameMessage(List<Player> players, boolean disconnection) {
        this.players = players;
        this.disconnection = disconnection;
    }

    @Override
    public void doAction(ClientController client) {
        client.notifyEndGame(players, disconnection);
        client.destroy();
    }
}
