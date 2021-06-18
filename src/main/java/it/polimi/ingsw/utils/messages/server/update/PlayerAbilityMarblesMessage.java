package it.polimi.ingsw.utils.messages.server.update;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.utils.exceptions.NotExistingNicknameException;
import it.polimi.ingsw.server.model.storage.ResourceType;

import java.util.List;

/**
 * TODO: fare javadoc
 */
public class PlayerAbilityMarblesMessage implements ServerUpdateMessage {

    private final List<ResourceType> marbles;
    private final String nickname;


    public PlayerAbilityMarblesMessage(List<ResourceType> marbles, String nickname) {
        this.marbles = marbles;
        this.nickname = nickname;
    }


    @Override
    public void doUpdate(ClientController client) {
        try {
            client.playerBoardFromNickname(nickname).setActiveAbilityMarbles(marbles);
        } catch(NotExistingNicknameException e){
            e.printStackTrace();
        }
    }
}
