package it.polimi.ingsw.model.storage;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class RequestResourcesTest{

    @Test
    public void testGetStorageType() {
        RequestResources temp = new RequestResources(new ArrayList<>(List.of(new Resource(ResourceType.COIN,3))),StorageType.STRONGBOX);

        assertEquals(StorageType.STRONGBOX, temp.getStorageType());
    }

    @Test
    public void testGetList() {
        RequestResources temp = new RequestResources(new ArrayList<>(List.of(new Resource(ResourceType.COIN,3))),StorageType.STRONGBOX);

        assertEquals(3, temp.getList().get(0).getQuantity());
        assertEquals(ResourceType.COIN, temp.getList().get(0).getResourceType());

        temp = new RequestResources(new ArrayList<>(List.of(new Resource(ResourceType.COIN,3),
                new Resource(ResourceType.COIN,3))),StorageType.STRONGBOX);

        assertEquals(6, temp.getList().get(0).getQuantity());
        assertEquals(ResourceType.COIN, temp.getList().get(0).getResourceType());

        temp = new RequestResources(new ArrayList<>(List.of(new Resource(ResourceType.COIN,3),
                new Resource(ResourceType.COIN,3))),StorageType.STRONGBOX);

        assertEquals(6, temp.getList().get(0).getQuantity());
        assertEquals(ResourceType.COIN, temp.getList().get(0).getResourceType());
    }

    @Test
    public void testAddResources() {
        RequestResources temp = new RequestResources(new ArrayList<>(List.of(new Resource(ResourceType.COIN,3))),StorageType.STRONGBOX);

        assertEquals(3, temp.getList().get(0).getQuantity());
        assertEquals(ResourceType.COIN, temp.getList().get(0).getResourceType());

        assertTrue(temp.addResources(new ArrayList<>(List.of(new Resource(ResourceType.COIN,1)))));

        assertEquals(4, temp.getList().get(0).getQuantity());
        assertEquals(ResourceType.COIN, temp.getList().get(0).getResourceType());

        assertTrue(temp.addResources(new ArrayList<>(List.of(new Resource(ResourceType.SHIELD,1)))));

        assertEquals(4, temp.getList().get(0).getQuantity());
        assertEquals(ResourceType.COIN, temp.getList().get(0).getResourceType());
        assertEquals(1, temp.getList().get(1).getQuantity());
        assertEquals(ResourceType.SHIELD, temp.getList().get(1).getResourceType());

        assertTrue(temp.addResources(new ArrayList<>(List.of(new Resource(ResourceType.SERVANT,1),
                new Resource(ResourceType.SHIELD,2)))));

        assertEquals(4, temp.getList().get(0).getQuantity());
        assertEquals(ResourceType.COIN, temp.getList().get(0).getResourceType());
        assertEquals(3, temp.getList().get(1).getQuantity());
        assertEquals(ResourceType.SHIELD, temp.getList().get(1).getResourceType());
        assertEquals(1, temp.getList().get(2).getQuantity());
        assertEquals(ResourceType.SERVANT, temp.getList().get(2).getResourceType());
    }

    @Test
    public void testRemoveResources() {
        RequestResources temp = new RequestResources(new ArrayList<>(List.of(new Resource(ResourceType.COIN,3))),StorageType.STRONGBOX);

        assertEquals(3, temp.getList().get(0).getQuantity());
        assertEquals(ResourceType.COIN, temp.getList().get(0).getResourceType());

        assertTrue(temp.removeResources(new ArrayList<>(List.of(new Resource(ResourceType.COIN,1)))));

        assertEquals(2, temp.getList().get(0).getQuantity());
        assertEquals(ResourceType.COIN, temp.getList().get(0).getResourceType());

        assertFalse(temp.removeResources(new ArrayList<>(List.of(new Resource(ResourceType.COIN,3)))));
        assertFalse(temp.removeResources(new ArrayList<>(List.of(new Resource(ResourceType.SERVANT,1)))));

        temp = new RequestResources(new ArrayList<>(List.of(new Resource(ResourceType.COIN,3),
                new Resource(ResourceType.SHIELD,2))),StorageType.STRONGBOX);

        assertTrue(temp.removeResources(new ArrayList<>(List.of(new Resource(ResourceType.COIN,3)))));

        assertEquals(2, temp.getList().get(0).getQuantity());
        assertEquals(ResourceType.SHIELD, temp.getList().get(0).getResourceType());

        temp = new RequestResources(new ArrayList<>(List.of(new Resource(ResourceType.COIN,3),
                new Resource(ResourceType.SHIELD,2))),StorageType.STRONGBOX);

        assertTrue(temp.removeResources(new ArrayList<>(List.of(new Resource(ResourceType.COIN,2),
                new Resource(ResourceType.COIN,1)))));

        assertEquals(2, temp.getList().get(0).getQuantity());
        assertEquals(ResourceType.SHIELD, temp.getList().get(0).getResourceType());

        temp = new RequestResources(new ArrayList<>(List.of(new Resource(ResourceType.COIN,3),
                new Resource(ResourceType.SHIELD,2))),StorageType.STRONGBOX);

        assertTrue(temp.removeResources(new ArrayList<>(List.of(new Resource(ResourceType.COIN,2),
                new Resource(ResourceType.SHIELD,1),new Resource(ResourceType.COIN,1)))));

        assertEquals(1, temp.getList().get(0).getQuantity());
        assertEquals(ResourceType.SHIELD, temp.getList().get(0).getResourceType());

    }
}