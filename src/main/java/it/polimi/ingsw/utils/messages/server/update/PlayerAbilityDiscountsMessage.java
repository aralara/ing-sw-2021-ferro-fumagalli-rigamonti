package it.polimi.ingsw.utils.messages.server.update;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.utils.exceptions.NotExistingNicknameException;
import it.polimi.ingsw.server.model.storage.ResourceType;

import java.util.List;

/**
 * Server update message for the player's discount ability
 */
public class PlayerAbilityDiscountsMessage implements ServerUpdateMessage {

    private final List<ResourceType> discounts;
    private final String nickname;


    /**
     * Constructor for a PlayerAbilityDiscountsMessage given a list of discounts and a nickname
     * @param discounts List of ResourceTypes containing the player's available discounts
     * @param nickname Nickname of the player
     */
    public PlayerAbilityDiscountsMessage(List<ResourceType> discounts, String nickname) {
        this.discounts = discounts;
        this.nickname = nickname;
    }


    @Override
    public void doUpdate(ClientController client) {
        try {
            client.playerBoardFromNickname(nickname).setActiveAbilityDiscounts(discounts);
        } catch(NotExistingNicknameException e){
            e.printStackTrace();
        }
    }
}
