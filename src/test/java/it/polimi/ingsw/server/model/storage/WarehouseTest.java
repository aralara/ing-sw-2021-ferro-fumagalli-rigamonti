package it.polimi.ingsw.server.model.storage;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Tests methods of Warehouse class
 */
public class WarehouseTest{

    /**
     * Tests if a list of shelves is a valid configuration for the warehouse
     */
    @Test
    public void testValidate() {

        List<Shelf> temp2 = new ArrayList<>(List.of(new Shelf(ResourceType.COIN,new Resource(ResourceType.COIN,2),2,false),
                new Shelf(ResourceType.COIN,new Resource(ResourceType.COIN,2),2,false)));
        assertFalse(Warehouse.validate(temp2));

        List<Shelf> temp = new ArrayList<>(List.of(new Shelf(ResourceType.COIN,new Resource(ResourceType.COIN,2),2,false)));
        assertTrue(Warehouse.validate(temp));
        temp.add(new Shelf(ResourceType.SHIELD,new Resource(ResourceType.SHIELD,2),2,false));
        assertFalse(Warehouse.validate(temp));
        temp.remove(temp.size()-1);
        temp.add(new Shelf(ResourceType.COIN,new Resource(ResourceType.COIN,1),1,false));
        assertFalse(Warehouse.validate(temp));
        temp.remove(temp.size()-1);
        temp.add(new Shelf(ResourceType.SHIELD,new Resource(ResourceType.SHIELD,1),1,false));
        assertTrue(Warehouse.validate(temp));
        temp.add(new Shelf(ResourceType.SERVANT,new Resource(ResourceType.SERVANT,2),3,false));
        assertTrue(Warehouse.validate(temp));

        temp.add(new Shelf(ResourceType.STONE,new Resource(ResourceType.STONE,1),1,false));
        assertFalse(Warehouse.validate(temp));
        temp.remove(temp.size()-1);


        temp.add(new Shelf(ResourceType.STONE,new Resource(ResourceType.STONE,1),2,true));
        temp.add(new Shelf(ResourceType.STONE,new Resource(ResourceType.STONE,1),2,true));
        assertTrue(Warehouse.validate(temp));
        temp.add(new Shelf(ResourceType.STONE,new Resource(ResourceType.STONE,1),2,true));
        assertFalse(Warehouse.validate(temp));


    }

    /**
     * Tests methods getShelves
     */
    @Test
    public void testGetShelves() {
        Warehouse wh = new Warehouse();
        Shelf s1 = new Shelf(ResourceType.COIN,new Resource(ResourceType.COIN,2),2,false);
        wh.addShelf(s1);

        List<Shelf> temp = wh.getShelves();
        assertEquals(2,temp.get(3).getList().get(0).getQuantity());
        assertEquals(ResourceType.COIN,temp.get(3).getList().get(0).getResourceType());
    }

    /**
     * Tests if the configuration is correctly changed with method changeConfiguration
     */
    @Test
    public void testChangeConfiguration() {
        Warehouse wh = new Warehouse();
        List<Shelf> temp = new ArrayList<>();
        temp.add(new Shelf(ResourceType.STONE,new Resource(ResourceType.STONE,1),2,true));
        temp.add(new Shelf(ResourceType.STONE,new Resource(ResourceType.STONE,1),2,true));

        assertTrue(wh.changeConfiguration(temp));
        assertEquals(2,wh.getList().get(0).getQuantity());
        assertEquals(ResourceType.STONE,wh.getList().get(0).getResourceType());


        temp.add(new Shelf(ResourceType.COIN,new Resource(ResourceType.COIN,1),1,false));
        temp.add(new Shelf(ResourceType.SHIELD,new Resource(ResourceType.SHIELD,1),2,false));
        temp.add(new Shelf(ResourceType.STONE,new Resource(ResourceType.STONE,1),3,false));
        assertTrue(wh.changeConfiguration(temp));

        assertEquals(3,wh.getList().get(0).getQuantity());
        assertEquals(ResourceType.STONE,wh.getList().get(0).getResourceType());
        assertEquals(1,wh.getList().get(1).getQuantity());
        assertEquals(ResourceType.COIN,wh.getList().get(1).getResourceType());
        assertEquals(1,wh.getList().get(2).getQuantity());
        assertEquals(ResourceType.SHIELD,wh.getList().get(2).getResourceType());

        temp.add(new Shelf(ResourceType.STONE,new Resource(ResourceType.STONE,1),3,false));
        assertFalse(wh.changeConfiguration(temp));
    }

    /**
     * Tests methods getList that return a list of resources contained in the warehouse
     */
    @Test
    public void testGetList() {
        Warehouse wh = new Warehouse();
        List<Shelf> temp = new ArrayList<>();
        temp.add(new Shelf(ResourceType.STONE,new Resource(ResourceType.STONE,1),2,true));
        temp.add(new Shelf(ResourceType.STONE,new Resource(ResourceType.STONE,1),2,true));
        temp.add(new Shelf(ResourceType.COIN,new Resource(ResourceType.COIN,1),1,false));
        temp.add(new Shelf(ResourceType.SHIELD,new Resource(ResourceType.SHIELD,1),2,false));
        temp.add(new Shelf(ResourceType.STONE,new Resource(ResourceType.STONE,1),3,false));
        assertTrue(wh.changeConfiguration(temp));

        assertEquals(3,wh.getList().get(0).getQuantity());
        assertEquals(ResourceType.STONE,wh.getList().get(0).getResourceType());
        assertEquals(1,wh.getList().get(1).getQuantity());
        assertEquals(ResourceType.COIN,wh.getList().get(1).getResourceType());
        assertEquals(1,wh.getList().get(2).getQuantity());
        assertEquals(ResourceType.SHIELD,wh.getList().get(2).getResourceType());

        assertEquals(2,wh.getList(true).get(0).getQuantity());
        assertEquals(ResourceType.STONE,wh.getList(true).get(0).getResourceType());

        assertEquals(1,wh.getList(false).get(0).getQuantity());
        assertEquals(ResourceType.COIN,wh.getList(false).get(0).getResourceType());
        assertEquals(1,wh.getList(false).get(1).getQuantity());
        assertEquals(ResourceType.SHIELD,wh.getList(false).get(1).getResourceType());
        assertEquals(1,wh.getList(false).get(2).getQuantity());
        assertEquals(ResourceType.STONE,wh.getList(false).get(2).getResourceType());

    }

    /**
     * Tests method addResources, always return false because we can only add a resource by change the configuration
     */
    @Test
    public void testAddResources() {
        Warehouse wh = new Warehouse();
        assertFalse(wh.addResources(new ArrayList<>(List.of(new Resource(ResourceType.COIN,2)))));
    }

    /**
     * Tests if a warehouse is empty
     */
    @Test
    public void testIsEmpty() {
        Warehouse wh = new Warehouse();
        assertTrue(wh.isEmpty());
        List<Shelf> temp = new ArrayList<>();
        temp.add(new Shelf(ResourceType.COIN,new Resource(ResourceType.COIN,1),1,false));
        temp.add(new Shelf(ResourceType.SHIELD,new Resource(ResourceType.SHIELD,1),2,false));
        temp.add(new Shelf(ResourceType.STONE,new Resource(ResourceType.STONE,1),3,false));
        temp.add(new Shelf(ResourceType.SHIELD,new Resource(ResourceType.SHIELD,2),2,true));
        temp.add(new Shelf(ResourceType.COIN,new Resource(ResourceType.COIN,2),2,true));
        wh.changeConfiguration(temp);
        assertFalse(wh.isEmpty());
    }

    /**
     * Tests if a list of resources is correctly removed from a warehouse
     */
    @Test
    public void testRemoveResources() {

        Warehouse wh = new Warehouse();
        List<Shelf> temp = new ArrayList<>();
        temp.add(new Shelf(ResourceType.COIN,new Resource(ResourceType.COIN,1),1,false));
        temp.add(new Shelf(ResourceType.SHIELD,new Resource(ResourceType.SHIELD,1),2,false));
        temp.add(new Shelf(ResourceType.STONE,new Resource(ResourceType.STONE,1),3,false));
        temp.add(new Shelf(ResourceType.SHIELD,new Resource(ResourceType.SHIELD,2),2,true));
        temp.add(new Shelf(ResourceType.COIN,new Resource(ResourceType.COIN,2),2,true));
        assertTrue(wh.changeConfiguration(temp));

        assertTrue(wh.removeResources(new ArrayList<>(List.of(new Resource(ResourceType.COIN,1))),false));
        assertTrue(wh.removeResources(new ArrayList<>(List.of(new Resource(ResourceType.SHIELD,1))),false));


        assertTrue(wh.removeResources(new ArrayList<>(List.of(new Resource(ResourceType.SHIELD,2))),true));

        assertFalse(wh.removeResources(new ArrayList<>(List.of(new Resource(ResourceType.COIN,10))),false));
        assertTrue(wh.removeResources(new ArrayList<>(List.of(new Resource(ResourceType.COIN,1))),true));

        assertTrue(wh.removeResources(new ArrayList<>(List.of(new Resource(ResourceType.COIN,5)))));
    }
}