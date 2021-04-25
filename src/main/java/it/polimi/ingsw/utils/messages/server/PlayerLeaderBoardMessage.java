package it.polimi.ingsw.utils.messages.server;

import it.polimi.ingsw.server.model.boards.LeaderBoard;
import it.polimi.ingsw.utils.messages.Message;

public class PlayerLeaderBoardMessage implements Message {

    private LeaderBoard leaderBoard;
    private String nickname;


    public PlayerLeaderBoardMessage(LeaderBoard leaderBoard, String nickname) {
        this.leaderBoard = leaderBoard;
        this.nickname = nickname;
    }


    public LeaderBoard getLeaderBoard() {
        return leaderBoard;
    }

    public String getNickname(){
        return nickname;
    }
}
