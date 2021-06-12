package it.polimi.ingsw.server.model.cards.requirement;

import it.polimi.ingsw.utils.exceptions.InvalidSpaceException;
import it.polimi.ingsw.server.model.boards.PlayerBoard;
import it.polimi.ingsw.server.model.cards.card.CardColors;
import it.polimi.ingsw.server.model.cards.card.DevelopmentCard;
import it.polimi.ingsw.server.model.storage.Production;
import it.polimi.ingsw.server.model.storage.Resource;
import it.polimi.ingsw.server.model.storage.ResourceType;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class RequirementDevTest {

    /**
     * Tests the color getter
     */
    @Test
    public void testGetColor() {
        RequirementDev rd = new RequirementDev(CardColors.YELLOW, 2,1);
        assertEquals(CardColors.YELLOW,rd.getColor());
    }

    /**
     * Tests the level getter
     */
    @Test
    public void testGetLevel() {
        RequirementDev rd = new RequirementDev(CardColors.YELLOW, 2,1);
        assertEquals(2,rd.getLevel());
    }

    /**
     * Tests the number getter
     */
    @Test
    public void testGetNumber() {
        RequirementDev rd = new RequirementDev(CardColors.YELLOW, 2,1);
        assertEquals(1,rd.getNumber());
    }

    /**
     * Tests if a requirement is correctly turn into a string
     */
    @Test
    public void testRequirementToString(){
        RequirementDev requirementDev = new RequirementDev(CardColors.YELLOW, 2, 1);
        assertEquals("1 YELLOW level 2 development cards", requirementDev.requirementToString());
    }

    /**
     * Tests if a requirement is checked
     */
    @Test
    public void testCheckRequirement() {
        PlayerBoard pb = new PlayerBoard("Bonucci");

        try {
            pb.getDevelopmentBoard().addDevCard(new DevelopmentCard(0,2, CardColors.YELLOW, 1,
                    new Production(new ArrayList<>(List.of(new Resource(ResourceType.SERVANT, 2))),
                            new ArrayList<>(List.of(new Resource(ResourceType.FAITH, 2)))),
                    new ArrayList<>(List.of(new Resource(ResourceType.SHIELD, 2)))), 1);
            pb.getDevelopmentBoard().addDevCard(new DevelopmentCard(0,2, CardColors.BLUE, 1,
                    new Production(new ArrayList<>(List.of(new Resource(ResourceType.SERVANT, 2))),
                            new ArrayList<>(List.of(new Resource(ResourceType.FAITH, 2)))),
                    new ArrayList<>(List.of(new Resource(ResourceType.SHIELD, 2)))), 2);
            pb.getDevelopmentBoard().addDevCard(new DevelopmentCard(0,2, CardColors.YELLOW, 2,
                    new Production(new ArrayList<>(List.of(new Resource(ResourceType.SERVANT, 2))),
                            new ArrayList<>(List.of(new Resource(ResourceType.FAITH, 2)))),
                    new ArrayList<>(List.of(new Resource(ResourceType.SHIELD, 2)))), 1);
        }
        catch (InvalidSpaceException e){
            e.printStackTrace();
        }


        RequirementDev rr = new RequirementDev(CardColors.YELLOW, 2,1);
        assertTrue(rr.checkRequirement(pb));

        rr = new RequirementDev(CardColors.YELLOW, 1,2);
        assertTrue(rr.checkRequirement(pb));

        rr = new RequirementDev(CardColors.PURPLE, 1,1);
        assertFalse(rr.checkRequirement(pb));

        rr = new RequirementDev(CardColors.BLUE, 1,1);
        assertTrue(rr.checkRequirement(pb));

        rr = new RequirementDev(CardColors.BLUE, 2,1);
        assertFalse(rr.checkRequirement(pb));
    }
}