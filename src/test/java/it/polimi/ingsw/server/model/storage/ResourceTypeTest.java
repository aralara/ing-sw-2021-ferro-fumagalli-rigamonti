package it.polimi.ingsw.server.model.storage;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class ResourceTypeTest {

    @Test
    public void testGetValues() {
        List<ResourceType> values = ResourceType.getValues();
        assertEquals(values.get(0),ResourceType.COIN);
        assertEquals(values.get(1),ResourceType.SHIELD);
        assertEquals(values.get(2),ResourceType.SERVANT);
        assertEquals(values.get(3),ResourceType.STONE);
        assertEquals(values.get(4),ResourceType. FAITH);
        assertEquals(values.get(5),ResourceType. WILDCARD);
    }

    @Test
    public void testGetRealValues() {
        List<ResourceType> values = ResourceType.getRealValues();
        assertEquals(values.get(0),ResourceType.COIN);
        assertEquals(values.get(1),ResourceType.SHIELD);
        assertEquals(values.get(2),ResourceType.SERVANT);
        assertEquals(values.get(3),ResourceType.STONE);
    }
}