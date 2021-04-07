package it.polimi.ingsw.model.market;

import it.polimi.ingsw.model.FileNames;
import it.polimi.ingsw.model.storage.Resource;
import it.polimi.ingsw.model.storage.ResourceType;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class MarketTest {

    @Test
    public void testLoadMarket() {
        Market market = new Market();
        market.loadMarket(FileNames.MARKET_FILE.value());
        int[] matrixCounters = new int[ResourceType.values().length];

        matrixCounters[market.getFloatingMarble().getResourceType().ordinal()]++;
        for(int row=0; row<3; row++)
            for(int column=0; column<4; column++)
                matrixCounters[market.getMarbleAt(row, column).getResourceType().ordinal()]++;

        assertEquals(2, matrixCounters[0]); //YELLOW
        assertEquals(2, matrixCounters[1]); //BLUE
        assertEquals(2, matrixCounters[2]); //PURPLE
        assertEquals(2, matrixCounters[3]); //GREY
        assertEquals(1, matrixCounters[4]); //RED
        assertEquals(4, matrixCounters[5]); //WHITE
    }

    @Test
    public void testChooseCoordinates() {
        Market market = new Market();
        List<Resource> resources;
        market.loadMarket(FileNames.MARKET_FILE.value());

        resources = market.chooseCoordinates(2, -1);
        assertFalse(resources.isEmpty());
        assertTrue(resources.size()>0 && resources.size()<=4);

        resources = market.chooseCoordinates(1, -1);
        assertFalse(resources.isEmpty());
        assertTrue(resources.size()>0 && resources.size()<=4);

        resources = market.chooseCoordinates(0, -1);
        assertFalse(resources.isEmpty());
        assertTrue(resources.size()>0 && resources.size()<=4);

        resources = market.chooseCoordinates(-1, 3);
        assertFalse(resources.isEmpty());
        assertTrue(resources.size()>0 && resources.size()<=3);

        resources = market.chooseCoordinates(-1, 2);
        assertFalse(resources.isEmpty());
        assertTrue(resources.size()>0 && resources.size()<=3);

        resources = market.chooseCoordinates(-1, 1);
        assertFalse(resources.isEmpty());
        assertTrue(resources.size()>0 && resources.size()<=3);

        resources = market.chooseCoordinates(-1, 0);
        assertFalse(resources.isEmpty());
        assertTrue(resources.size()>0 && resources.size()<=3);
    }

    @Test
    public void testGetMarbleAt() {
    }

    @Test
    public void testGetFloatingMarble() {
    }
}