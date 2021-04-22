package it.polimi.ingsw.model.cards.ability;

import it.polimi.ingsw.model.boards.PlayerBoard;
import it.polimi.ingsw.model.games.MultiGame;
import it.polimi.ingsw.model.storage.ResourceType;
import org.junit.Test;

import static org.junit.Assert.*;

public class AbilityWarehouseTest {

    @Test
    public void testGetResourceType() {
        AbilityWarehouse aw = new AbilityWarehouse(ResourceType.STONE);
        assertEquals(ResourceType.STONE,aw.getResourceType());
    }

    @Test   //TODO: cambiato costruttore game
    public void testActivateAbility() {/*
        PlayerBoard pb = new PlayerBoard(new MultiGame("Bonucci"),"Bonucci");
        AbilityWarehouse aw = new AbilityWarehouse(ResourceType.STONE);
        aw.activateAbility(pb);

        assertEquals(1,pb.getWarehouse().getShelves().size());
        assertEquals(ResourceType.STONE, pb.getWarehouse().getShelves().get(0).getResourceType());

        AbilityWarehouse aw2 = new AbilityWarehouse(ResourceType.SERVANT);

        aw2.activateAbility(pb);

        assertEquals(2,pb.getWarehouse().getShelves().size());
        assertEquals(ResourceType.STONE, pb.getWarehouse().getShelves().get(0).getResourceType());
        assertEquals(ResourceType.SERVANT, pb.getWarehouse().getShelves().get(1).getResourceType());*/
    }
}