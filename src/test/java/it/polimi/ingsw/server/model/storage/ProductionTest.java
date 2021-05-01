package it.polimi.ingsw.server.model.storage;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ProductionTest{

    @Test
    public void testMakeClone() {
        Production temp = new Production(new ArrayList<>(List.of(new Resource(ResourceType.COIN,4))),
                new ArrayList<>(List.of(new Resource(ResourceType.SHIELD,4))));

        Production temp2 = temp.makeClone();

        assertEquals(4,temp2.getConsumed().get(0).getQuantity());
        assertEquals(ResourceType.COIN,temp2.getConsumed().get(0).getResourceType());
        assertNotEquals(temp, temp2);
    }

    @Test
    public void testGetConsumed() {

        Production temp = new Production();

        assertEquals(2,temp.getConsumed().get(0).getQuantity());
        assertEquals(ResourceType.WILDCARD,temp.getConsumed().get(0).getResourceType());

        temp = new Production(new ArrayList<>(List.of(new Resource(ResourceType.COIN,4))),
                new ArrayList<>(List.of(new Resource(ResourceType.SHIELD,4))));

        assertEquals(4,temp.getConsumed().get(0).getQuantity());
        assertEquals(ResourceType.COIN,temp.getConsumed().get(0).getResourceType());

        temp = new Production(new ArrayList<>(List.of(new Resource(ResourceType.COIN,4),
                new Resource(ResourceType.SHIELD,2))),
                new ArrayList<>(List.of(new Resource(ResourceType.SHIELD,4))));

        assertEquals(4,temp.getConsumed().get(0).getQuantity());
        assertEquals(ResourceType.COIN,temp.getConsumed().get(0).getResourceType());
        assertEquals(2,temp.getConsumed().get(1).getQuantity());
        assertEquals(ResourceType.SHIELD,temp.getConsumed().get(1).getResourceType());
    }

    @Test
    public void testGetProduced() {

        Production temp = new Production();

        assertEquals(1,temp.getProduced().get(0).getQuantity());
        assertEquals(ResourceType.WILDCARD,temp.getProduced().get(0).getResourceType());

        temp = new Production(new ArrayList<>(List.of(new Resource(ResourceType.COIN,4))),
                new ArrayList<>(List.of(new Resource(ResourceType.SHIELD,4))));

        assertEquals(4,temp.getProduced().get(0).getQuantity());
        assertEquals(ResourceType.SHIELD,temp.getProduced().get(0).getResourceType());

        temp = new Production(new ArrayList<>(List.of(new Resource(ResourceType.COIN,4),
                new Resource(ResourceType.SHIELD,2))),
                new ArrayList<>(List.of(new Resource(ResourceType.SHIELD,4),
                        new Resource(ResourceType.FAITH,2))));

        assertEquals(4,temp.getProduced().get(0).getQuantity());
        assertEquals(ResourceType.SHIELD,temp.getProduced().get(0).getResourceType());
        assertEquals(2,temp.getProduced().get(1).getQuantity());
        assertEquals(ResourceType.FAITH,temp.getProduced().get(1).getResourceType());
    }
}