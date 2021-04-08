package it.polimi.ingsw.model.cards.requirement;

import it.polimi.ingsw.model.boards.PlayerBoard;
import it.polimi.ingsw.model.games.Game;
import it.polimi.ingsw.model.games.MultiGame;
import it.polimi.ingsw.model.storage.Resource;
import it.polimi.ingsw.model.storage.ResourceType;
import it.polimi.ingsw.model.storage.Shelf;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class RequirementResTest {

    @Test
    public void getResource() {
    }

    @Test
    public void checkRequirement() {
    }

    @Test
    public void makeClone() {
        RequirementRes rr = new RequirementRes(new Resource(ResourceType.SERVANT,5));

        RequirementRes rr2 = rr.makeClone();
        assertNotEquals(rr,rr2);
        assertEquals(ResourceType.SERVANT,rr2.getResource().getResourceType());
        assertEquals(5,rr2.getResource().getQuantity());
    }
}