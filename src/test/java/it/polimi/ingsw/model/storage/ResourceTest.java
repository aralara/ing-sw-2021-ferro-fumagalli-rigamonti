package it.polimi.ingsw.model.storage;

import org.junit.Test;

import static org.junit.Assert.*;

public class ResourceTest{

    @Test
    public void testAdd() {
        Resource res = new Resource(ResourceType.COIN, 2);

        res.add(new Resource(ResourceType.COIN, 2));
        assertEquals(ResourceType.COIN,res.getResourceType());
        assertEquals(4,res.getQuantity());

        assertFalse(res.add(new Resource(ResourceType.SHIELD, 2)));
    }

    @Test
    public void testSub() {
        Resource res = new Resource(ResourceType.COIN, 2);

        res.sub(new Resource(ResourceType.COIN, 1));
        assertEquals(ResourceType.COIN,res.getResourceType());
        assertEquals(1,res.getQuantity());

        assertFalse(res.sub(new Resource(ResourceType.SHIELD, 2)));
        assertFalse(res.sub(new Resource(ResourceType.COIN, 2)));
    }

    @Test
    public void testGetResourceType() {
    }

    @Test
    public void testGetQuantity() {
    }

    @Test
    public void testSetResourceType() {
        Resource res = new Resource();
        res.setResourceType(ResourceType.COIN);

        assertEquals(ResourceType.COIN,res.getResourceType());
    }

    @Test
    public void testSetQuantity() {
        Resource res = new Resource();
        res.setQuantity(4);

        assertEquals(4,res.getQuantity());
    }
}