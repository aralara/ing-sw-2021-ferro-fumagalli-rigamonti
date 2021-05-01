package it.polimi.ingsw.server.model.cards.requirement;

import it.polimi.ingsw.server.model.boards.PlayerBoard;
import it.polimi.ingsw.server.model.cards.card.CardColors;
import it.polimi.ingsw.server.model.cards.card.DevelopmentCard;
import it.polimi.ingsw.server.model.games.MultiGame;
import it.polimi.ingsw.server.model.storage.Production;
import it.polimi.ingsw.server.model.storage.Resource;
import it.polimi.ingsw.server.model.storage.ResourceType;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class RequirementDevTest {

    @Test
    public void testGetColor() {
        RequirementDev rd = new RequirementDev(CardColors.YELLOW, 2,1);
        assertEquals(CardColors.YELLOW,rd.getColor());
    }

    @Test
    public void testGetLevel() {
        RequirementDev rd = new RequirementDev(CardColors.YELLOW, 2,1);
        assertEquals(2,rd.getLevel());
    }

    @Test
    public void testGetNumber() {
        RequirementDev rd = new RequirementDev(CardColors.YELLOW, 2,1);
        assertEquals(1,rd.getNumber());
    }

    @Test
    public void testCheckRequirement() {
        PlayerBoard pb = new PlayerBoard("Bonucci");

        pb.getDevelopmentBoard().addDevCard(new DevelopmentCard(2,CardColors.YELLOW,1,
                new Production(new ArrayList<>(List.of(new Resource(ResourceType.SERVANT,2))),
                        new ArrayList<>(List.of(new Resource(ResourceType.FAITH,2)))),
                        new ArrayList<>(List.of(new Resource(ResourceType.SHIELD,2)))),1);
        pb.getDevelopmentBoard().addDevCard(new DevelopmentCard(2,CardColors.BLUE,1,
                new Production(new ArrayList<>(List.of(new Resource(ResourceType.SERVANT,2))),
                        new ArrayList<>(List.of(new Resource(ResourceType.FAITH,2)))),
                new ArrayList<>(List.of(new Resource(ResourceType.SHIELD,2)))),2);
        pb.getDevelopmentBoard().addDevCard(new DevelopmentCard(2,CardColors.YELLOW,2,
                new Production(new ArrayList<>(List.of(new Resource(ResourceType.SERVANT,2))),
                        new ArrayList<>(List.of(new Resource(ResourceType.FAITH,2)))),
                new ArrayList<>(List.of(new Resource(ResourceType.SHIELD,2)))),1);


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