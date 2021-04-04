package it.polimi.ingsw.model.market;

import it.polimi.ingsw.model.FileNames;
import it.polimi.ingsw.model.storage.ResourceType;
import org.junit.Test;

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
        //hardly testable with randomized marbles
        /* maybe simply if it's selected row -> the size of the list is <= 4
        *  and if it's selected column -> the size of the list is <= 3 */
    }

    @Test
    public void testMoveFloatingMarble() {
        //hardly testable with randomized marbles
    }

    @Test
    public void testGetMarbleAt() {
        //hardly testable with randomized marbles
    }

    @Test
    public void testGetFloatingMarble() {
    }
}