package it.polimi.ingsw.model.market;

import it.polimi.ingsw.model.storage.ResourceType;
import org.junit.Test;

import static org.junit.Assert.*;

public class MarbleTest {

    @Test
    public void testGetters() {
        MarbleColors color = MarbleColors.RED;
        ResourceType resourceType = ResourceType.FAITH;
        Marble marble = new Marble(color, resourceType);

        assertEquals(color, marble.getColor());
        assertEquals(resourceType, marble.getResourceType());
    }
}