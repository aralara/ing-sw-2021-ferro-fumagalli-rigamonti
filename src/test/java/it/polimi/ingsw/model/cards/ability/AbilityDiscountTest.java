package it.polimi.ingsw.model.cards.ability;

import it.polimi.ingsw.model.boards.PlayerBoard;
import it.polimi.ingsw.model.cards.requirement.RequirementRes;
import it.polimi.ingsw.model.games.MultiGame;
import it.polimi.ingsw.model.storage.Resource;
import it.polimi.ingsw.model.storage.ResourceType;
import it.polimi.ingsw.model.storage.Shelf;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class AbilityDiscountTest {

    @Test
    public void testGetResourceType() {
        AbilityDiscount ad = new AbilityDiscount(ResourceType.COIN);
        assertEquals(ResourceType.COIN,ad.getResourceType());

    }

    @Test
    public void testActivateAbility() {
        PlayerBoard pb = new PlayerBoard(new MultiGame("Bonucci"),"Bonucci");
        AbilityDiscount ad = new AbilityDiscount(ResourceType.COIN);
        ad.activateAbility(pb);

        assertEquals(1,pb.getAbilityDiscounts().size());
        assertEquals(ResourceType.COIN, pb.getAbilityDiscounts().get(0));

        AbilityDiscount ad2 = new AbilityDiscount(ResourceType.SHIELD);

        ad2.activateAbility(pb);

        assertEquals(2,pb.getAbilityDiscounts().size());
        assertEquals(ResourceType.COIN, pb.getAbilityDiscounts().get(0));
        assertEquals(ResourceType.SHIELD, pb.getAbilityDiscounts().get(1));
    }
}