package it.polimi.ingsw.utils.messages.server;

import it.polimi.ingsw.server.model.boards.LeaderBoard;
import it.polimi.ingsw.utils.messages.Message;

public class PlayerLeaderBoardMessage implements Message {

    private LeaderBoard leaderBoard;


    public PlayerLeaderBoardMessage(LeaderBoard leaderBoard) {
        this.leaderBoard = leaderBoard;
    }


    public LeaderBoard getLeaderBoard() {
        return leaderBoard;
    }
}
