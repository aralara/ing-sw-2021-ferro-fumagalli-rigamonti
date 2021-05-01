package it.polimi.ingsw.server.model.cards.ability;

import it.polimi.ingsw.server.model.boards.PlayerBoard;
import it.polimi.ingsw.server.model.games.MultiGame;
import it.polimi.ingsw.server.model.storage.ResourceType;
import org.junit.Test;

import static org.junit.Assert.*;

public class AbilityWarehouseTest {

    @Test
    public void testGetResourceType() {
        AbilityWarehouse aw = new AbilityWarehouse(ResourceType.STONE);
        assertEquals(ResourceType.STONE,aw.getResourceType());
    }

    @Test
    public void testActivateAbility() {
        PlayerBoard pb = new PlayerBoard("Bonucci");
        AbilityWarehouse aw = new AbilityWarehouse(ResourceType.STONE);
        aw.activateAbility(pb);

        assertEquals(1,pb.getWarehouse().getShelves().size());
        assertEquals(ResourceType.STONE, pb.getWarehouse().getShelves().get(0).getResourceType());

        AbilityWarehouse aw2 = new AbilityWarehouse(ResourceType.SERVANT);

        aw2.activateAbility(pb);

        assertEquals(2,pb.getWarehouse().getShelves().size());
        assertEquals(ResourceType.STONE, pb.getWarehouse().getShelves().get(0).getResourceType());
        assertEquals(ResourceType.SERVANT, pb.getWarehouse().getShelves().get(1).getResourceType());
    }
}