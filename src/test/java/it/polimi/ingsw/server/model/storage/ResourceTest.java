package it.polimi.ingsw.server.model.storage;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests methods of Resource class
 */
public class ResourceTest{

    /**
     * Tests if a resource is correctly cloned
     */
    @Test
    public void testMakeClone() {
        Resource res = new Resource(ResourceType.COIN, 2);

        Resource res2 = res.makeClone();

        assertEquals(ResourceType.COIN,res2.getResourceType());
        assertEquals(2,res2.getQuantity());
    }

    /**
     * Tests if a resource is correctly added
     */
    @Test
    public void testAdd() {
        Resource res = new Resource(ResourceType.COIN, 2);

        res.add(new Resource(ResourceType.COIN, 2));
        assertEquals(ResourceType.COIN,res.getResourceType());
        assertEquals(4,res.getQuantity());

        assertFalse(res.add(new Resource(ResourceType.SHIELD, 2)));
    }

    /**
     * Tests if a resource is correctly subtracted
     */
    @Test
    public void testSub() {
        Resource res = new Resource(ResourceType.COIN, 2);

        res.sub(new Resource(ResourceType.COIN, 1));
        assertEquals(ResourceType.COIN,res.getResourceType());
        assertEquals(1,res.getQuantity());

        assertFalse(res.sub(new Resource(ResourceType.SHIELD, 2)));
        assertFalse(res.sub(new Resource(ResourceType.COIN, 2)));
    }

    /**
     * Tests if a resourceType is correctly set
     */
    @Test
    public void testSetResourceType() {
        Resource res = new Resource();
        res.setResourceType(ResourceType.COIN);

        assertEquals(ResourceType.COIN,res.getResourceType());
    }

    /**
     * Tests if quantity is correctly set
     */
    @Test
    public void testSetQuantity() {
        Resource res = new Resource();
        res.setQuantity(4);

        assertEquals(4,res.getQuantity());
    }
}