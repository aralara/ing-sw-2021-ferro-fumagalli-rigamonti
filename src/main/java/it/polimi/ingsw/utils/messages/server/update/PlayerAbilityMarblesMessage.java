package it.polimi.ingsw.utils.messages.server.update;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.exceptions.NotExistingNicknameException;
import it.polimi.ingsw.server.model.storage.ResourceType;

import java.util.List;

public class PlayerAbilityMarblesMessage implements ServerUpdateMessage {

    private final List<ResourceType> marbles;
    private final String nickname;


    public PlayerAbilityMarblesMessage(List<ResourceType> marbles, String nickname) {
        this.marbles = marbles;
        this.nickname = nickname;
    }


    public List<ResourceType> getMarbles() {
        return marbles;
    }

    public String getNickname(){
        return nickname;
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
