package it.polimi.ingsw.model.cards.card;

import it.polimi.ingsw.model.cards.ability.AbilityDiscount;
import it.polimi.ingsw.model.cards.requirement.*;
import it.polimi.ingsw.model.storage.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import static org.junit.Assert.*;

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
}