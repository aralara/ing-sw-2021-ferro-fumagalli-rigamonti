package it.polimi.ingsw.utils.messages.server.update;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.utils.exceptions.NotExistingNicknameException;

import static it.polimi.ingsw.utils.Constants.FAITH_TOTAL_VATICAN_REPORTS;

/**
 * Server update message for the player's pope spaces
 */
public class PlayerFaithBPopeMessage implements ServerUpdateMessage {
    private final boolean[] popeProgression;
    private final String nickname;


    /**
     * Constructor for a PlayerFaithBPopeMessage given the popeProgression and a nickname
     * @param popeProgression Player's pope progression
     * @param nickname Nickname of the player
     */
    public PlayerFaithBPopeMessage(boolean[] popeProgression, String nickname) {
        this.popeProgression = new boolean[FAITH_TOTAL_VATICAN_REPORTS.value()];
        System.arraycopy(popeProgression, 0, this.popeProgression, 0, FAITH_TOTAL_VATICAN_REPORTS.value());
        this.nickname = nickname;
    }


    @Override
    public void doUpdate(ClientController client) {
        try {
            client.playerBoardFromNickname(nickname).getFaithBoard().setPopeProgression(popeProgression);
        } catch(NotExistingNicknameException e){
            e.printStackTrace();
        }
    }
}
