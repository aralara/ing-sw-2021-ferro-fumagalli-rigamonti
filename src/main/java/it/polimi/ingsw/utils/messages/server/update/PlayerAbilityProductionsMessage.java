package it.polimi.ingsw.utils.messages.server.update;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.utils.exceptions.NotExistingNicknameException;
import it.polimi.ingsw.server.model.storage.Production;

import java.util.List;

/**
 * TODO: fare javadoc
 */
public class PlayerAbilityProductionsMessage implements ServerUpdateMessage {

    private final List<Production> productions;
    private final String nickname;


    public PlayerAbilityProductionsMessage(List<Production> productions, String nickname) {
        this.productions = productions;
        this.nickname = nickname;
    }


    @Override
    public void doUpdate(ClientController client) {
        try {
            client.playerBoardFromNickname(nickname).setActiveAbilityProductions(productions);
        } catch(NotExistingNicknameException e){
            e.printStackTrace();
        }
    }
}
