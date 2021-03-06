package it.polimi.ingsw.server.model.cards.card;

import it.polimi.ingsw.server.model.storage.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests methods of DevelopmentCard class
 */
public class DevelopmentCardTest {

    private static final int VP = 5;
    private static final CardColors color = CardColors.PURPLE;
    private static final int level = 2;
    private static final Production production = new Production(
            new ArrayList<>(){{
                add(new Resource(
                        ResourceType.COIN,
                        1));
            }},
            new ArrayList<>(){{
                add(new Resource(
                        ResourceType.SERVANT,
                        2));
            }}
    );
    private static final List<Resource> cost = new ArrayList<>(){{
        add(new Resource(
                ResourceType.COIN,
                4));
    }};
    private static final DevelopmentCard devCard = new DevelopmentCard(
            0,
            VP,
            color,
            level,
            production,
            cost
    );

    /**
     * Tests getters for a development card
     */
    @Test
    public void testGetters() {
        assertEquals(VP, devCard.getVP());
        assertEquals(color, devCard.getColor());
        assertEquals(level, devCard.getLevel());
        assertEquals(production, devCard.getProduction());
        assertEquals(cost, devCard.getCost());
    }

    /**
     * Tests methods that turn a development card into a String
     */
    @Test
    public void testToString(){
        DevelopmentCard developmentCard = new DevelopmentCard();
        assertEquals(" DEVELOPMENT CARD \n• The development card is covered, you can't see it!",
                developmentCard.toString());
        List<Resource> consumed = new ArrayList<>(), produced = new ArrayList<>(), cost = new ArrayList<>();
        consumed.add(new Resource(ResourceType.SERVANT, 2));
        produced.add(new Resource(ResourceType.COIN, 1));
        produced.add(new Resource(ResourceType.FAITH, 1));
        cost.add(new Resource(ResourceType.SHIELD, 3));
        developmentCard = new DevelopmentCard(1, 2, CardColors.BLUE, 1,
                new Production(consumed, produced), cost);
        assertEquals(" DEVELOPMENT CARD \n" +
                        " • This is a BLUE card level 1\n" +
                        " • Victory points: 2\n" +
                        " • Cost: 3 SHIELD\n" +
                        " • Production that can be activated:\n" +
                        " \tConsumed:  > 2 SERVANT\n" +
                        "\tProduced:  > 1 COIN\n" +
                        "\t           > 1 FAITH\n",
                developmentCard.toString());
    }

    /**
     * Tests hashcode and equals comparisons
     */
    @Test
    public void testHashCodeAndEquals() {
        DevelopmentCard developmentCard1 = new DevelopmentCard(0, VP, color, level, production, cost);
        DevelopmentCard developmentCard2 = new DevelopmentCard(1, VP, color, level, production, cost);
        assertEquals(devCard, developmentCard1);
        assertEquals(devCard.hashCode(), developmentCard1.hashCode());
        assertNotEquals(devCard, developmentCard2);
        assertNotEquals(devCard.hashCode(), developmentCard2.hashCode());
    }
}