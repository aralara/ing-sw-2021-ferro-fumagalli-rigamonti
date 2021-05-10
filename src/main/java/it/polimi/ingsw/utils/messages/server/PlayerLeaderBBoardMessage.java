package it.polimi.ingsw.utils.messages.server;

import it.polimi.ingsw.client.cli.CLI;
import it.polimi.ingsw.server.model.cards.deck.Deck;

public class PlayerLeaderBBoardMessage implements ServerActionMessage {

    private final Deck board;
    private final String nickname;


    public PlayerLeaderBBoardMessage(Deck board, String nickname) {
        this.board = board;
        this.nickname = nickname;
    }


    public Deck getBoard() {
        return board;
    }

    public String getNickname(){
        return nickname;
    }

    @Override
    public void doAction(CLI client) {
        //TODO: stampare la board delle leader
    }
}
