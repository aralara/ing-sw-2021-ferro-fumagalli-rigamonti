package it.polimi.ingsw.server.model.cards.card;

import it.polimi.ingsw.server.model.cards.ability.AbilityDiscount;
import it.polimi.ingsw.server.model.cards.requirement.*;
import it.polimi.ingsw.server.model.storage.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests methods of LeaderCard class TODO: fare javadoc
 */
public class LeaderCardTest {

    private static final int VP = 9;
    private static final List<Requirement> requirements = new ArrayList<>(){{
        add(new RequirementDev(
                CardColors.PURPLE,
                2,
                1
        ));
        add(new RequirementRes(new Resource(
                ResourceType.COIN,
                3
        )));
    }};
    private static final AbilityDiscount ability = new AbilityDiscount(
            ResourceType.SHIELD
    );

    private static final LeaderCard leadCard = new LeaderCard(
            0,
            VP,
            requirements,
            ability
    );

    @Test
    public void testGetters(){
        assertEquals(VP, leadCard.getVP());
        assertEquals(requirements, leadCard.getRequirements());
        assertEquals(ability, leadCard.getAbility());
    }

    @Test
    public void testToString(){
        LeaderCard leaderCard = new LeaderCard();
        assertEquals(" LEADER CARD \n• The leader card is covered, you can't see it!", leaderCard.toString());
        List<Requirement> requirementRes = new ArrayList<>();
        requirementRes.add(new RequirementRes(new Resource(ResourceType.SHIELD, 5)));
        leaderCard = new LeaderCard(1, 2, requirementRes, new AbilityDiscount(ResourceType.COIN));
        assertEquals(" LEADER CARD \n" +
                " • Requirements: 5 SHIELD\n" +
                " • Victory points: 2\n" +
                " • Special ability: You can get 1 COIN off the cost of development cards\n",
                leaderCard.toString());
    }
}