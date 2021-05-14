package it.polimi.ingsw.utils.messages.server;

import it.polimi.ingsw.client.cli.CLI;
import it.polimi.ingsw.exceptions.NotExistingNickname;

public class PlayerFaithBFaithMessage implements ServerUpdateMessage {

    private final int faith;
    private final String nickname;


    public PlayerFaithBFaithMessage(int faith, String nickname) {
        this.faith = faith;
        this.nickname = nickname;
    }


    public int getFaith() {
        return faith;
    }

    public String getNickname() {
        return nickname;
    }

    @Override
    public void doUpdate(CLI client) {
        try {
            client.playerBoardFromNickname(nickname).getFaithBoard().setFaith(faith);
        } catch(NotExistingNickname e){
            e.printStackTrace();
        }
    }
}
