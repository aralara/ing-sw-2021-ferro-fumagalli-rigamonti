package it.polimi.ingsw.server.model.cards.ability;

import it.polimi.ingsw.server.model.boards.PlayerBoard;
import it.polimi.ingsw.server.model.storage.ResourceType;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests methods of AbilityWarehouse class
 */
public class AbilityWarehouseTest {

    /**
     * Tests the ability getter
     */
    @Test
    public void testGetResourceTypeAndAbilityToString() {
        AbilityWarehouse aw = new AbilityWarehouse(ResourceType.STONE);
        assertEquals(ResourceType.STONE,aw.getResourceType());
        assertEquals(" • Special ability: You can can gain an extra shelf to contain 2 units of STONE\n",
                aw.abilityToString());
    }

    /**
     * Tests the warehouse ability activation
     */
    @Test
    public void testActivateAbility() {
        PlayerBoard pb = new PlayerBoard("Bonucci");
        AbilityWarehouse aw = new AbilityWarehouse(ResourceType.STONE);
        aw.activateAbility(pb);

        assertEquals(4,pb.getWarehouse().getShelves().size());
        assertEquals(ResourceType.STONE, pb.getWarehouse().getShelves().get(3).getResourceType());

        AbilityWarehouse aw2 = new AbilityWarehouse(ResourceType.SERVANT);

        aw2.activateAbility(pb);

        assertEquals(5,pb.getWarehouse().getShelves().size());
        assertEquals(ResourceType.STONE, pb.getWarehouse().getShelves().get(3).getResourceType());
        assertEquals(ResourceType.SERVANT, pb.getWarehouse().getShelves().get(4).getResourceType());
    }
}