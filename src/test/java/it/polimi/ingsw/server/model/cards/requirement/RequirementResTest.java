package it.polimi.ingsw.server.model.cards.requirement;

import it.polimi.ingsw.server.model.boards.PlayerBoard;
import it.polimi.ingsw.server.model.games.MultiGame;
import it.polimi.ingsw.server.model.storage.Resource;
import it.polimi.ingsw.server.model.storage.ResourceType;
import it.polimi.ingsw.server.model.storage.Shelf;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class RequirementResTest {

    @Test
    public void testGetResource() {
        RequirementRes rr = new RequirementRes(new Resource(ResourceType.SHIELD,4));

        assertEquals(4,rr.getResource().getQuantity());
        assertEquals(ResourceType.SHIELD,rr.getResource().getResourceType());
    }

    @Test
    public void testCheckRequirement() {
        PlayerBoard pb = new PlayerBoard("Bonucci");
        Shelf s1 = new Shelf(ResourceType.SHIELD,new Resource(ResourceType.SHIELD,3),3,false );
        Shelf s2 = new Shelf(ResourceType.SHIELD,new Resource(ResourceType.SHIELD,2),2,true );

        pb.getWarehouse().changeConfiguration(new ArrayList<>(List.of(s1,s2)));

        RequirementRes rr = new RequirementRes(new Resource(ResourceType.SHIELD,4));
        assertTrue(rr.checkRequirement(pb));

        rr = new RequirementRes(new Resource(ResourceType.COIN,4));
        assertFalse(rr.checkRequirement(pb));

        rr = new RequirementRes(new Resource(ResourceType.SHIELD,6));
        assertFalse(rr.checkRequirement(pb));
    }
}