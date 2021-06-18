package it.polimi.ingsw.server.model.cards.ability;

import it.polimi.ingsw.server.model.boards.PlayerBoard;
import it.polimi.ingsw.server.model.storage.ResourceType;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests methods of AbilityDiscount class
 */
public class AbilityDiscountTest {

    /**
     * Tests the ability getter
     */
    @Test
    public void testGetResourceTypeAndAbilityToString() {
        AbilityDiscount ad = new AbilityDiscount(ResourceType.COIN);
        assertEquals(ResourceType.COIN,ad.getResourceType());
        assertEquals(" • Special ability: You can get 1 COIN off the cost of development cards\n",
                ad.abilityToString());
    }

    /**
     * Tests the discount ability activation
     */
    @Test
    public void testActivateAbility() {
        PlayerBoard pb = new PlayerBoard("Bonucci");
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