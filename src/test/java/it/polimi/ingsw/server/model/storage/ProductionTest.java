package it.polimi.ingsw.server.model.storage;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Tests methods of Production class
 */
public class ProductionTest{

    /**
     * Tests if a production is correctly turn into a String
     */
    @Test
    public void testToString() {
        Production temp = new Production();
        assertEquals(temp.toString(),"\tConsumed:  > 2 " + ResourceType.WILDCARD +"\n\tProduced:  > 1 " + ResourceType.WILDCARD + "\n");

        temp = new Production(new ArrayList<>(List.of(new Resource(ResourceType.COIN,4),new Resource(ResourceType.SHIELD,4))),
                new ArrayList<>(List.of(new Resource(ResourceType.SHIELD,4),new Resource(ResourceType.SHIELD,4))));

        assertEquals(temp.toString(),"\tConsumed:  > 4 " + ResourceType.COIN + "\n\t           > 4 " + ResourceType.SHIELD +
                "\n\tProduced:  > 4 " + ResourceType.SHIELD + "\n\t           > 4 "+ ResourceType.SHIELD +"\n");

    }

    /**
     * Tests if a production is correctly cloned
     */
    @Test
    public void testMakeClone() {
        Production temp = new Production(new ArrayList<>(List.of(new Resource(ResourceType.COIN,4))),
                new ArrayList<>(List.of(new Resource(ResourceType.SHIELD,4))));

        Production temp2 = temp.makeClone();

        assertEquals(4,temp2.getConsumed().get(0).getQuantity());
        assertEquals(ResourceType.COIN,temp2.getConsumed().get(0).getResourceType());
        assertNotEquals(temp, temp2);
    }

    /**
     * Tests if getConsumed return the correct list of resources consumed
     */
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

    /**
     * Tests if getProduced return the correct list of resources produced
     */
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

    /**
     * Tests if a production could have modeled another one
     */
    @Test
    public void testCanModel(){
        Production mainProd, checkProd;
        mainProd = new Production(new ArrayList<>(List.of(new Resource(ResourceType.WILDCARD,2))),
                new ArrayList<>(List.of(new Resource(ResourceType.SHIELD,1))));
        checkProd = new Production(new ArrayList<>(List.of(new Resource(ResourceType.COIN,2))),
                new ArrayList<>(List.of(new Resource(ResourceType.SHIELD,1))));
        assertTrue(mainProd.canModel(checkProd));
        assertFalse(checkProd.canModel(mainProd));
    }
}