package it.polimi.ingsw.utils.messages.server;

import it.polimi.ingsw.server.model.cards.deck.Deck;
import it.polimi.ingsw.utils.messages.Message;

public class PlayerLeaderBBoardMessage implements Message {

    private Deck board;
    private String nickname;


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
}
