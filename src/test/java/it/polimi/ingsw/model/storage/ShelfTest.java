package it.polimi.ingsw.model.storage;

import org.junit.Test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

public class ShelfTest{

    @Test
    public void testGetResourceType() {
    }

    @Test
    public void testSetResourceType() {
        Shelf instance = new Shelf();
        instance.setResourceType(ResourceType.FAITH);
        instance.setResourceType(ResourceType.COIN);
        assertEquals(ResourceType.COIN, instance.getResourceType());
    }

    @Test
    public void testGetLevel() {
        Shelf instance = new Shelf(ResourceType.COIN,new Resource(ResourceType.COIN,2),2,false);
        assertEquals(2, instance.getLevel());
    }

    @Test
    public void testGetIsLeader() {
        Shelf instance = new Shelf(ResourceType.COIN,new Resource(ResourceType.COIN,2),2,false);
        assertFalse(instance.getIsLeader());
    }

    @Test
    public void testCheckAdd() {

        Shelf instance = new Shelf(ResourceType.COIN,new Resource(ResourceType.COIN,2),2,false);
        assertFalse(instance.checkAdd(new Resource(ResourceType.COIN, 1)));

        instance = new Shelf(ResourceType.COIN,new Resource(ResourceType.COIN,2),3,false);
        assertTrue(instance.checkAdd(new Resource(ResourceType.COIN, 1)));
        assertFalse(instance.checkAdd(new Resource(ResourceType.SERVANT, 1)));
        assertFalse(instance.checkAdd(new Resource(ResourceType.STONE, 1)));
        assertFalse(instance.checkAdd(new Resource(ResourceType.SHIELD, 1)));

        instance = new Shelf(ResourceType.COIN,new Resource(ResourceType.SERVANT,3),3,false);
        assertFalse(instance.checkAdd(new Resource(ResourceType.SERVANT, 1)));
        assertFalse(instance.checkAdd(new Resource(ResourceType.SERVANT, 0)));

        instance = new Shelf(ResourceType.COIN,new Resource(ResourceType.SERVANT,0),3,false);
        assertTrue(instance.checkAdd(new Resource(ResourceType.COIN, 2)));
        assertTrue(instance.checkAdd(new Resource(ResourceType.SERVANT, 1)));

        instance = new Shelf(ResourceType.COIN,new Resource(ResourceType.SERVANT,0),2,true);
        assertTrue(instance.checkAdd(new Resource(ResourceType.COIN, 2)));
        assertFalse(instance.checkAdd(new Resource(ResourceType.SERVANT, 1)));

    }

    @Test
    public void testGetList() {

        Shelf instance = new Shelf(ResourceType.COIN,new Resource(ResourceType.COIN,2),2,false);
        assertEquals(2, instance.getList().get(0).getQuantity());
        assertEquals(ResourceType.COIN, instance.getList().get(0).getResourceType());

        instance = new Shelf(ResourceType.COIN,new Resource(ResourceType.COIN,1),2,false);
        instance.addResources(new Resource(ResourceType.COIN,1));
        assertEquals(2, instance.getList().get(0).getQuantity());
        assertEquals(ResourceType.COIN, instance.getList().get(0).getResourceType());

        instance = new Shelf(ResourceType.COIN,new Resource(ResourceType.COIN,0),3,false);
        instance.addResources(new Resource(ResourceType.SHIELD,3));
        assertEquals(3, instance.getList().get(0).getQuantity());
        assertEquals(ResourceType.SHIELD, instance.getList().get(0).getResourceType());

    }

    @Test
    public void testAddResources() {

        Shelf instance = new Shelf(ResourceType.COIN,new Resource(ResourceType.COIN,0),2,false);
        assertTrue(instance.addResources(new Resource(ResourceType.COIN, 1)));

        instance = new Shelf(ResourceType.COIN,new Resource(ResourceType.COIN,0),2,false);
        assertTrue(instance.addResources(new Resource(ResourceType.COIN, 2)));

        instance = new Shelf(ResourceType.COIN,new Resource(ResourceType.COIN,0),2,false);
        assertFalse(instance.addResources(new Resource(ResourceType.COIN, 3)));

        instance = new Shelf(ResourceType.COIN,new Resource(ResourceType.COIN,0),2,false);
        assertTrue(instance.addResources(new Resource(ResourceType.SHIELD, 1)));

        instance = new Shelf(ResourceType.COIN,new Resource(ResourceType.COIN,0),2,false);
        assertFalse(instance.addResources(new Resource(ResourceType.SHIELD, 3)));

        instance = new Shelf(ResourceType.COIN,new Resource(ResourceType.COIN,0),2,true);
        assertTrue(instance.addResources(new Resource(ResourceType.COIN, 2)));

        instance = new Shelf(ResourceType.COIN,new Resource(ResourceType.COIN,0),2,true);
        assertFalse(instance.addResources(new Resource(ResourceType.COIN, 3)));

        instance = new Shelf(ResourceType.COIN,new Resource(ResourceType.COIN,0),2,true);
        assertFalse(instance.addResources(new Resource(ResourceType.SHIELD, 2)));
    }

    @Test
    public void testTestAddResources() {
        Shelf instance = new Shelf(ResourceType.COIN,new Resource(ResourceType.COIN,0),2,false);
        assertTrue(instance.addResources(new ArrayList<>(List.of(new Resource(ResourceType.COIN, 2)))));

        instance = new Shelf(ResourceType.COIN,new Resource(ResourceType.COIN,0),2,false);
        assertTrue(instance.addResources(new ArrayList<>(List.of(new Resource(ResourceType.COIN, 1), new Resource(ResourceType.COIN, 1)))));

        instance = new Shelf(ResourceType.COIN,new Resource(ResourceType.COIN,0),2,false);
        assertFalse(instance.addResources(new ArrayList<>(List.of(new Resource(ResourceType.COIN, 3)))));

        instance = new Shelf(ResourceType.COIN,new Resource(ResourceType.COIN,0),2,false);
        assertFalse(instance.addResources(new ArrayList<>(List.of(new Resource(ResourceType.SHIELD, 3)))));

        instance = new Shelf(ResourceType.COIN,new Resource(ResourceType.COIN,1),2,false);
        assertTrue(instance.addResources(new ArrayList<>(List.of(new Resource(ResourceType.COIN, 1)))));

        instance = new Shelf(ResourceType.COIN,new Resource(ResourceType.COIN,0),2,true);
        assertFalse(instance.addResources(new ArrayList<>(List.of(new Resource(ResourceType.SERVANT, 2)))));

        instance = new Shelf(ResourceType.COIN,new Resource(ResourceType.COIN,0),2,false);
        assertTrue(instance.addResources(new ArrayList<>(List.of(new Resource(ResourceType.SERVANT, 2)))));
    }

    @Test
    public void testRemoveResources() {
        Shelf instance = new Shelf(ResourceType.COIN,new Resource(ResourceType.COIN,0),2,false);
        assertFalse(instance.removeResources(new Resource(ResourceType.COIN, 1)));

        instance = new Shelf(ResourceType.COIN,new Resource(ResourceType.COIN,2),2,false);
        assertTrue(instance.removeResources(new Resource(ResourceType.COIN, 2)));

        instance = new Shelf(ResourceType.COIN,new Resource(ResourceType.COIN,1),2,false);
        assertTrue(instance.removeResources(new Resource(ResourceType.COIN, 1)));

        instance = new Shelf(ResourceType.SHIELD,new Resource(ResourceType.SHIELD,2),2,false);
        assertTrue(instance.removeResources(new Resource(ResourceType.SHIELD, 1)));

        instance = new Shelf(ResourceType.COIN,new Resource(ResourceType.COIN,2),2,false);
        assertFalse(instance.removeResources(new Resource(ResourceType.SHIELD, 1)));

        instance = new Shelf(ResourceType.COIN,new Resource(ResourceType.COIN,2),2,true);
        assertFalse(instance.removeResources(new Resource(ResourceType.COIN, 3)));

    }

    @Test
    public void testTestRemoveResources() {
        Shelf instance = new Shelf(ResourceType.COIN,new Resource(ResourceType.COIN,2),2,false);
        assertTrue(instance.removeResources(new Resource(ResourceType.COIN, 1)));

        instance = new Shelf(ResourceType.COIN,new Resource(ResourceType.COIN,2),2,false);
        assertTrue(instance.removeResources(new Resource(ResourceType.COIN, 2)));

        instance = new Shelf(ResourceType.COIN,new Resource(ResourceType.COIN,2),2,false);
        assertFalse(instance.removeResources(new Resource(ResourceType.COIN, 3)));

        instance = new Shelf(ResourceType.COIN,new Resource(ResourceType.COIN,2),2,false);
        assertFalse(instance.removeResources(new Resource(ResourceType.SHIELD, 1)));

        instance = new Shelf(ResourceType.COIN,new Resource(ResourceType.COIN,2),2,false);
        assertFalse(instance.removeResources(new Resource(ResourceType.SERVANT, 3)));
    }
}