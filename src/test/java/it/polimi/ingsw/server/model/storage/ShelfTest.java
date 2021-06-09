package it.polimi.ingsw.server.model.storage;

import org.junit.Test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

public class ShelfTest{
    /**
     * Tests if a resourceType is correctly set
     */
    @Test
    public void testSetResourceType() {
        Shelf instance = new Shelf();
        instance.setResourceType(ResourceType.FAITH);
        instance.setResourceType(ResourceType.COIN);
        assertEquals(ResourceType.COIN, instance.getResourceType());
    }

    /**
     * Tests if a level is correctly got
     */
    @Test
    public void testGetLevel() {
        Shelf instance = new Shelf(ResourceType.COIN,new Resource(ResourceType.COIN,2),2,false);
        assertEquals(2, instance.getLevel());
    }

    /**
     * Tests method getIsLeader
     */
    @Test
    public void testGetIsLeader() {
        Shelf instance = new Shelf(ResourceType.COIN,new Resource(ResourceType.COIN,2),2,false);
        assertFalse(instance.isLeader());
    }

    /**
     * Tests if isResourceTypeUnique correctly returns true if the list doesn't contain resources of the specified
     * resourceType
     */
    @Test
    public void testIsResourceTypeUnique() {
        List<Shelf> shelves = new ArrayList<>();
        shelves.add(new Shelf(ResourceType.COIN,new Resource(ResourceType.COIN,2),2,false));
        assertFalse(Shelf.isResourceTypeUnique(shelves,ResourceType.COIN));
        assertTrue(Shelf.isResourceTypeUnique(shelves,ResourceType.SHIELD));
        assertTrue(Shelf.isResourceTypeUnique(shelves,ResourceType.SERVANT));
        shelves.add(new Shelf(ResourceType.SHIELD,new Resource(ResourceType.SHIELD,1),1,false));
        assertFalse(Shelf.isResourceTypeUnique(shelves,ResourceType.COIN));
        assertFalse(Shelf.isResourceTypeUnique(shelves,ResourceType.SHIELD));
        assertTrue(Shelf.isResourceTypeUnique(shelves,ResourceType.SERVANT));
        assertTrue(Shelf.isResourceTypeUnique(shelves,ResourceType.STONE));
    }

    /**
     * Tests methods isEmpty
     */
    @Test
    public void testIsEmpty() {
        Shelf shelf = new Shelf(ResourceType.COIN,new Resource(ResourceType.COIN,2),2,false);
        assertFalse(shelf.isEmpty());
        shelf = new Shelf(ResourceType.COIN,new Resource(ResourceType.COIN,0),2,false);
        assertTrue(shelf.isEmpty());
    }

    /**
     * Tests if a resource can be added to a shelf
     */
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

    /**
     * Tests if getList correctly return the list of resources in a shelf
     */
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

    /**
     * Tests if a resource is correctly added to a shelf
     */
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

        instance = new Shelf(ResourceType.COIN,new Resource(ResourceType.COIN,0),2,false);
        assertTrue(instance.addResources(new ArrayList<>(List.of(new Resource(ResourceType.COIN, 2)))));

        instance = new Shelf(ResourceType.COIN,new Resource(ResourceType.COIN,0),2,false);
        assertTrue(instance.addResources(new ArrayList<>(List.of(new Resource(ResourceType.COIN, 1),
                new Resource(ResourceType.COIN, 1)))));

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

        instance = new Shelf(ResourceType.COIN,new Resource(ResourceType.COIN,0),2,false);
        assertFalse(instance.addResources(new ArrayList<>(List.of(new Resource(ResourceType.SERVANT, 2),
                new Resource(ResourceType.COIN, 2)))));
    }

    /**
     * Tests if a resource is correctly removed from a shelf
     */
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

        instance = new Shelf(ResourceType.COIN,new Resource(ResourceType.COIN,2),2,false);
        assertTrue(instance.removeResources(new ArrayList<>(List.of(new Resource(ResourceType.COIN, 1)))));

        instance = new Shelf(ResourceType.COIN,new Resource(ResourceType.COIN,2),2,false);
        assertTrue(instance.removeResources(new ArrayList<>(List.of(new Resource(ResourceType.COIN, 2)))));

        instance = new Shelf(ResourceType.COIN,new Resource(ResourceType.COIN,2),2,false);
        assertFalse(instance.removeResources(new ArrayList<>(List.of(new Resource(ResourceType.COIN, 3)))));

        instance = new Shelf(ResourceType.COIN,new Resource(ResourceType.COIN,2),2,false);
        assertFalse(instance.removeResources(new ArrayList<>(List.of(new Resource(ResourceType.SHIELD, 1)))));

        instance = new Shelf(ResourceType.COIN,new Resource(ResourceType.COIN,2),2,false);
        assertFalse(instance.removeResources(new ArrayList<>(List.of(new Resource(ResourceType.SERVANT, 3)))));

        instance = new Shelf(ResourceType.COIN,new Resource(ResourceType.COIN,2),2,false);
        assertFalse(instance.removeResources(new ArrayList<>(List.of(new Resource(ResourceType.SERVANT, 3),
                new Resource(ResourceType.COIN, 3)))));

        instance = new Shelf(ResourceType.COIN,new Resource(ResourceType.COIN,2),2,false);
        assertFalse(instance.removeResources(new ArrayList<>(List.of(new Resource(ResourceType.COIN, 2),
                new Resource(ResourceType.COIN, 2)))));

        instance = new Shelf(ResourceType.COIN,new Resource(ResourceType.COIN,2),2,false);
        assertFalse(instance.removeResources(new ArrayList<>(List.of(new Resource(ResourceType.COIN, 2),
                new Resource(ResourceType.SHIELD, 2)))));

        instance = new Shelf(ResourceType.COIN,new Resource(ResourceType.COIN,2),2,false);
        assertTrue(instance.removeResources(new ArrayList<>(List.of(new Resource(ResourceType.COIN, 1),
                new Resource(ResourceType.COIN, 1)))));

    }
}