package it.polimi.ingsw.server.model.storage;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Tests methods of Storage class
 */
public class StorageTest{

    /**
     * Tests if some lists of resources are correctly merged into a new list
     */
    @Test
    public void testMergeResourceList() {

        List<Resource> res = new ArrayList<>(List.of(new Resource(ResourceType.COIN,2)));
        List<Resource> res1 = new ArrayList<>(List.of(new Resource(ResourceType.SERVANT,4)));
        List<Resource> res2 = new ArrayList<>(List.of(new Resource(ResourceType.SHIELD,3)));

        assertEquals(4,Storage.mergeResourceList(res,res,res1,res2).get(0).getQuantity());
        assertEquals(ResourceType.COIN,Storage.mergeResourceList(res,res,res1,res2).get(0).getResourceType());
        assertEquals(4,Storage.mergeResourceList(res,res,res1,res2).get(1).getQuantity());
        assertEquals(ResourceType.SERVANT,Storage.mergeResourceList(res,res,res1,res2).get(1).getResourceType());
        assertEquals(3,Storage.mergeResourceList(res,res,res1,res2).get(2).getQuantity());
        assertEquals(ResourceType.SHIELD,Storage.mergeResourceList(res,res,res1,res2).get(2).getResourceType());
    }

    /**
     * Tests if a list of resources contains all the resources from another list of resources
     */
    @Test
    public void testCheckContainedResources() {

        List<Resource> res1 = new ArrayList<>(List.of(new Resource(ResourceType.SHIELD,3)));
        List<Resource> res2 = new ArrayList<>(List.of(new Resource(ResourceType.SHIELD,2)));

        assertTrue(Storage.checkContainedResources(res1,res2));
        assertFalse(Storage.checkContainedResources(res2,res1));

        List<Resource> res3 = new ArrayList<>(List.of(new Resource(ResourceType.SHIELD,3),
                new Resource(ResourceType.COIN,4),new Resource(ResourceType.SERVANT,2)));
        List<Resource> res4 = new ArrayList<>(List.of(new Resource(ResourceType.SHIELD,2),
                new Resource(ResourceType.COIN,3)));

        assertTrue(Storage.checkContainedResources(res3,res4));
        assertFalse(Storage.checkContainedResources(res4,res3));
    }

    /**
     * Tests if the resources from a list are correctly aggregated
     */
    @Test
    public void testAggregateResources() {

        List<Resource> res = new ArrayList<>(List.of(new Resource(ResourceType.COIN,2)));
        Storage.aggregateResources(res);
        assertEquals(2,res.get(0).getQuantity());
        assertEquals(ResourceType.COIN,res.get(0).getResourceType());

        res = new ArrayList<>(List.of(new Resource(ResourceType.COIN,2),
                new Resource(ResourceType.COIN,2)));

        Storage.aggregateResources(res);
        assertEquals(1,res.size());
        assertEquals(4,res.get(0).getQuantity());
        assertEquals(ResourceType.COIN,res.get(0).getResourceType());

        res = new ArrayList<>(List.of(new Resource(ResourceType.COIN,2),
                new Resource(ResourceType.COIN,2),new Resource(ResourceType.SERVANT,2),
                new Resource(ResourceType.COIN,2)));

        Storage.aggregateResources(res);
        assertEquals(2,res.size());
        assertEquals(6,res.get(0).getQuantity());
        assertEquals(ResourceType.COIN,res.get(0).getResourceType());
        assertEquals(2,res.get(1).getQuantity());
        assertEquals(ResourceType.SERVANT,res.get(1).getResourceType());

    }

    /**
     * Tests if a discount is correctly calculated
     */
    @Test
    public void testCalculateDiscount() {
        List<Resource> resources = new ArrayList<>();
        List<ResourceType> discount = new ArrayList<>();
        discount.add(ResourceType.STONE);
        resources.add(new Resource(ResourceType.COIN,2));
        assertEquals(Storage.calculateDiscount(resources,discount).get(0).getQuantity(),2);
        assertEquals(Storage.calculateDiscount(resources,discount).get(0).getResourceType(),ResourceType.COIN);
        discount.add(ResourceType.COIN);
        assertEquals(Storage.calculateDiscount(resources,discount).get(0).getQuantity(),1);
        assertEquals(Storage.calculateDiscount(resources,discount).get(0).getResourceType(),ResourceType.COIN);
    }

    /**
     * Tests if a resource is correctly discarded
     */
    @Test
    public void testIsDiscardedResCorrect() {
        List<Resource> resources1 = new ArrayList<>();
        List<Resource> resources2 = new ArrayList<>();

        resources1.add(new Resource(ResourceType.COIN,2));
        resources1.add(new Resource(ResourceType.STONE,2));
        resources1.add(new Resource(ResourceType.SERVANT,2));

        resources2.add(new Resource(ResourceType.STONE,2));
        resources2.add(new Resource(ResourceType.SERVANT,2));

        assertTrue(Storage.isDiscardedResCorrect(resources1,resources1));
        assertTrue(Storage.isDiscardedResCorrect(resources1,resources2));
        assertFalse(Storage.isDiscardedResCorrect(resources2,resources1));
    }

    /**
     * Tests method getTotalQuantity
     */
    @Test
    public void testGetTotalQuantity() {
        List<Resource> resources1 = new ArrayList<>();
        List<Resource> resources2 = new ArrayList<>();

        resources1.add(new Resource(ResourceType.COIN,2));
        resources1.add(new Resource(ResourceType.STONE,2));
        resources1.add(new Resource(ResourceType.SERVANT,2));

        resources2.add(new Resource(ResourceType.STONE,2));
        resources2.add(new Resource(ResourceType.SERVANT,2));

        assertEquals(Storage.getTotalQuantity(resources1),6);
        assertEquals(Storage.getTotalQuantity(resources2),4);
    }

    /**
     * Tests if a list of resources can be obtained from another using wildcards
     */
    @Test
    public void testCheckListModeled(){
        List<Resource> model = new ArrayList<>(), modeled = new ArrayList<>();
        List<ResourceType> possibleWildcards = new ArrayList<>();

        model.add(new Resource(ResourceType.WILDCARD, 2));

        modeled.add(new Resource(ResourceType.COIN, 1));
        modeled.add(new Resource(ResourceType.SHIELD, 1));

        assertFalse(Storage.checkListModeled(model, modeled, possibleWildcards));
        assertFalse(Storage.checkListModeled(modeled, model, possibleWildcards));

        possibleWildcards.add(ResourceType.SERVANT);
        assertFalse(Storage.checkListModeled(model, modeled, possibleWildcards));
        assertFalse(Storage.checkListModeled(modeled, model, possibleWildcards));

        possibleWildcards.add(ResourceType.SHIELD);
        assertFalse(Storage.checkListModeled(model, modeled, possibleWildcards));
        assertFalse(Storage.checkListModeled(modeled, model, possibleWildcards));

        possibleWildcards.add(ResourceType.COIN);
        assertTrue(Storage.checkListModeled(model, modeled, possibleWildcards));
        assertFalse(Storage.checkListModeled(modeled, model, possibleWildcards));
    }
}