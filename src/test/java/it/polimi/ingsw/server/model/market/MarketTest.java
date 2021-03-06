package it.polimi.ingsw.server.model.market;

import it.polimi.ingsw.utils.exceptions.InvalidColumnException;
import it.polimi.ingsw.utils.exceptions.InvalidRowException;
import it.polimi.ingsw.server.model.FileNames;
import it.polimi.ingsw.server.model.storage.Resource;
import it.polimi.ingsw.server.model.storage.ResourceType;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Tests methods of Market class
 */
public class MarketTest {

    /**
     * Tests marbles loaded from file are present at the right quantity
     */
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

    /**
     * Tests the chosen coordinates return a list of resources
     */
    @SuppressWarnings("ConstantConditions")
    @Test
    public void testChooseCoordinates() {
        Market market = new Market();
        List<Resource> resources;
        market.loadMarket(FileNames.MARKET_FILE.value());

        try {
            resources = market.chooseCoordinates(2, -1);
            assertFalse(resources.isEmpty());
            assertTrue(resources.size() > 0 && resources.size() <= 4);

            resources = market.chooseCoordinates(1, -1);
            assertFalse(resources.isEmpty());
            assertTrue(resources.size() > 0 && resources.size() <= 4);

            resources = market.chooseCoordinates(0, -1);
            assertFalse(resources.isEmpty());
            assertTrue(resources.size() > 0 && resources.size() <= 4);

            resources = market.chooseCoordinates(-1, 3);
            assertFalse(resources.isEmpty());
            assertTrue(resources.size() > 0 && resources.size() <= 3);

            resources = market.chooseCoordinates(-1, 2);
            assertFalse(resources.isEmpty());
            assertTrue(resources.size() > 0 && resources.size() <= 3);

            resources = market.chooseCoordinates(-1, 1);
            assertFalse(resources.isEmpty());
            assertTrue(resources.size() > 0 && resources.size() <= 3);

            resources = market.chooseCoordinates(-1, 0);
            assertFalse(resources.isEmpty());
            assertTrue(resources.size() > 0 && resources.size() <= 3);

            assertNotNull(market.getLastTook());
            market.resetLastTook();
            assertNull(market.getLastTook());
        }
        catch (InvalidRowException | InvalidColumnException e){
            e.printStackTrace();
        }
    }

    /**
     * Tests marbles' getters return a not null object and getMarbleMatrix, getMarbleAt methods give the same object
     */
    @Test
    public void testGetMarbles() {
        Market market = new Market();
        market.loadMarket(FileNames.MARKET_FILE.value());

        for(int row=0; row<3; row++)
            for(int column=0; column<3; column++) {
                assertNotNull(market.getMarbleAt(row, column));
                assertEquals(market.getMarbleMatrix()[row][column], market.getMarbleAt(row, column));
            }

        assertNotNull(market.getFloatingMarble());
    }
}