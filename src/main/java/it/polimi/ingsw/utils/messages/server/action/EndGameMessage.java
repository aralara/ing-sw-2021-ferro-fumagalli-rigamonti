package it.polimi.ingsw.utils.messages.server.action;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.server.model.boards.Player;

import java.util.List;

/**
 * Server message that notifies the client of the game's end
 */
public class EndGameMessage implements ServerActionMessage {

    private final List<Player> players;


    public EndGameMessage(List<Player> players) {
        this.players = players;
    }


    public List<Player> getPlayers() {
        return players;
    }

    @Override
    public void doAction(ClientController client) {
        client.notifyEndGame(players);
        client.destroy();
    }
}
