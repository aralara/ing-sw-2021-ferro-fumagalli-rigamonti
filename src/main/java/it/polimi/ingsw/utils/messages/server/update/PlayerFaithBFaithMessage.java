package it.polimi.ingsw.utils.messages.server.update;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.exceptions.NotExistingNicknameException;

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
    public void doUpdate(ClientController client) {
        try {
            client.playerBoardFromNickname(nickname).getFaithBoard().setFaith(faith);
        } catch(NotExistingNicknameException e){
            e.printStackTrace();
        }
    }
}
