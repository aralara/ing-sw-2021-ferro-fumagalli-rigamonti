package it.polimi.ingsw.utils.messages.server.update;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.utils.exceptions.NotExistingNicknameException;
import it.polimi.ingsw.server.model.cards.deck.Deck;

/**
 * Server update message for the player's leaderboard board
 */
public class PlayerLeaderBBoardMessage implements ServerUpdateMessage {

    private final Deck board;
    private final String nickname;


    /**
     * Constructor for a PlayerLeaderBBoardMessage given the leaderboard and a nickname
     * @param board Player's leader board
     * @param nickname Nickname of the player
     */
    public PlayerLeaderBBoardMessage(Deck board, String nickname) {
        this.board = board;
        this.nickname = nickname;
    }


    @Override
    public void doUpdate(ClientController client) {
        try {
            client.playerBoardFromNickname(nickname).getLeaderBoard().setBoard(board);
        } catch(NotExistingNicknameException e){
            e.printStackTrace();
        }
    }
}
