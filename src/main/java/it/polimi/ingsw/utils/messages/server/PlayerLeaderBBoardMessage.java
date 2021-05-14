package it.polimi.ingsw.utils.messages.server;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.exceptions.NotExistingNickname;
import it.polimi.ingsw.server.model.cards.deck.Deck;

public class PlayerLeaderBBoardMessage implements ServerUpdateMessage {

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
    public void doUpdate(ClientController client) {
        try {
            client.playerBoardFromNickname(nickname).getLeaderBoard().setBoard(board);
        } catch(NotExistingNickname e){
            e.printStackTrace();
        }
    }
}
