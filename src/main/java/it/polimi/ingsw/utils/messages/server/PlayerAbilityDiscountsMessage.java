package it.polimi.ingsw.utils.messages.server;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.exceptions.NotExistingNickname;
import it.polimi.ingsw.server.model.storage.Production;
import it.polimi.ingsw.server.model.storage.ResourceType;

import java.util.List;

public class PlayerAbilityDiscountsMessage implements ServerUpdateMessage {

    private final List<ResourceType> discounts;
    private final String nickname;


    public PlayerAbilityDiscountsMessage(List<ResourceType> discounts, String nickname) {
        this.discounts = discounts;
        this.nickname = nickname;
    }


    public List<ResourceType> getDiscounts() {
        return discounts;
    }

    public String getNickname(){
        return nickname;
    }

    @Override
    public void doUpdate(ClientController client) {
        try {
            client.playerBoardFromNickname(nickname).setActiveAbilityDiscounts(discounts);
        } catch(NotExistingNickname e){
            e.printStackTrace();
        }
    }
}
