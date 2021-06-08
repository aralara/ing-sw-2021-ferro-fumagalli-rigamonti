package it.polimi.ingsw.server.model.storage;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class StorageTest{

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

    @Test
    public void testGetList() {
    }

    @Test
    public void testAddResources() {
    }

    @Test
    public void testRemoveResources() {
    }
}