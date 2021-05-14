package it.polimi.ingsw.utils.messages.server;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.exceptions.NotExistingNickname;

import static it.polimi.ingsw.utils.Constants.FAITH_TOTAL_VATICAN_REPORTS;

public class PlayerFaithBPopeMessage implements ServerUpdateMessage {
    private final boolean[] popeProgression;
    private final String nickname;


    public PlayerFaithBPopeMessage(boolean[] popeProgression, String nickname) {
        this.popeProgression = new boolean[FAITH_TOTAL_VATICAN_REPORTS.value()];
        System.arraycopy(popeProgression, 0, this.popeProgression, 0, FAITH_TOTAL_VATICAN_REPORTS.value());
        this.nickname = nickname;
    }


    public boolean[] getPopeProgression() {
        boolean[] popeProgressionCopy = new boolean[FAITH_TOTAL_VATICAN_REPORTS.value()];
        System.arraycopy(this.popeProgression, 0, popeProgressionCopy, 0, FAITH_TOTAL_VATICAN_REPORTS.value());
        return popeProgressionCopy;
    }

    public String getNickname() {
        return nickname;
    }

    @Override
    public void doUpdate(ClientController client) {
        try {
            client.playerBoardFromNickname(nickname).getFaithBoard().setPopeProgression(popeProgression);
        } catch(NotExistingNickname e){
            e.printStackTrace();
        }
    }
}
