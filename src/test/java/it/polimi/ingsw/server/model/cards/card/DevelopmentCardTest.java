package it.polimi.ingsw.server.model.cards.card;

import it.polimi.ingsw.server.model.storage.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import static org.junit.Assert.*;

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
            VP,
            color,
            level,
            production,
            cost
    );

    @Test
    public void testGetters() {
        assertEquals(VP, devCard.getVP());
        assertEquals(color, devCard.getColor());
        assertEquals(level, devCard.getLevel());
        assertEquals(production, devCard.getProduction());
        assertEquals(cost, devCard.getCost());
    }
}