package it.polimi.ingsw.utils.messages.server.action;

import it.polimi.ingsw.server.model.boards.Player;
import it.polimi.ingsw.utils.messages.Message;

import java.util.List;

public class EndGameMessage implements Message {

    private List<Player> players;


    public EndGameMessage(List<Player> players) {
        this.players = players;
    }


    public List<Player> getPlayers() {
        return players;
    }
}
