package it.polimi.ingsw.utils.messages.server.update;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.utils.exceptions.NotExistingNicknameException;

/**
 * Server update message for the player's faith
 */
public class PlayerFaithBFaithMessage implements ServerUpdateMessage {

    private final int faith;
    private final String nickname;


    /**
     * Constructor for a PlayerFaithBFaithMessage given the faith and a nickname
     * @param faith Player's faith
     * @param nickname Nickname of the player
     */
    public PlayerFaithBFaithMessage(int faith, String nickname) {
        this.faith = faith;
        this.nickname = nickname;
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
