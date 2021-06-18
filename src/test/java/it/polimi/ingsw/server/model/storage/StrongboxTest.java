package it.polimi.ingsw.server.model.storage;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Tests methods of Strongbox class
 */
public class StrongboxTest{

    /**
     * Tests methods getList that returns a list of resource that contains every resource from the strongbox
     */
    @Test
    public void testGetList() {

        Strongbox instance = new Strongbox();
        instance.addResources(new ArrayList<>(List.of(new Resource(ResourceType.COIN,2))));

        assertEquals(2, instance.getList().get(0).getQuantity());
        assertEquals(ResourceType.COIN, instance.getList().get(0).getResourceType());

        instance.addResources(new ArrayList<>(List.of(new Resource(ResourceType.COIN,1))));
        assertEquals(3, instance.getList().get(0).getQuantity());
        assertEquals(ResourceType.COIN, instance.getList().get(0).getResourceType());

        instance.addResources(new ArrayList<>(List.of(new Resource(ResourceType.SHIELD,3))));
        assertEquals(3, instance.getList().get(0).getQuantity());
        assertEquals(ResourceType.COIN, instance.getList().get(0).getResourceType());
        assertEquals(3, instance.getList().get(1).getQuantity());
        assertEquals(ResourceType.SHIELD, instance.getList().get(1).getResourceType());

        instance.removeResources(new ArrayList<>(List.of(new Resource(ResourceType.COIN,2))));
        assertEquals(1, instance.getList().get(0).getQuantity());
        assertEquals(ResourceType.COIN, instance.getList().get(0).getResourceType());
        assertEquals(3, instance.getList().get(1).getQuantity());
        assertEquals(ResourceType.SHIELD, instance.getList().get(1).getResourceType());

        instance.removeResources(new ArrayList<>(List.of(new Resource(ResourceType.SHIELD,1))));
        assertEquals(1, instance.getList().get(0).getQuantity());
        assertEquals(ResourceType.COIN, instance.getList().get(0).getResourceType());
        assertEquals(2, instance.getList().get(1).getQuantity());
        assertEquals(ResourceType.SHIELD, instance.getList().get(1).getResourceType());

        assertFalse(instance.removeResources(new ArrayList<>(List.of(new Resource(ResourceType.SERVANT,1)))));
        assertFalse(instance.removeResources(new ArrayList<>(List.of(new Resource(ResourceType.COIN,2)))));
        assertFalse(instance.removeResources(new ArrayList<>(List.of(new Resource(ResourceType.SHIELD,4)))));
    }
}