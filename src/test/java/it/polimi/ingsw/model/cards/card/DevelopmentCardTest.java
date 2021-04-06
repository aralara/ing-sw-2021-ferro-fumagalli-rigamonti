package it.polimi.ingsw.model.cards.card;

import it.polimi.ingsw.model.storage.Production;
import it.polimi.ingsw.model.storage.Resource;
import it.polimi.ingsw.model.storage.ResourceType;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class DevelopmentCardTest {

    @Test
    public void testMakeClone() {
        Production prod = new Production(
                new ArrayList<>(){{
                    add(new Resource(ResourceType.SHIELD, 2));
                }},
                new ArrayList<>(){{
                    add(new Resource(ResourceType.STONE, 2));
                }});
        DevelopmentCard card1 = new DevelopmentCard(19, CardColors.PURPLE, 3, prod,
                new ArrayList<>(){{
                    add(new Resource(ResourceType.SERVANT, 2));
                    add(new Resource(ResourceType.COIN, 1));
                }}
        );

        DevelopmentCard card2 = card1.makeClone();

        assertEquals(card1.getVP(), card2.getVP());
        assertEquals(card1.getColor(), card2.getColor());

        assertEquals(card1.getProduction().getProduced().get(0).getResourceType(),
                card2.getProduction().getProduced().get(0).getResourceType());
        assertEquals(card1.getProduction().getProduced().get(0).getQuantity(),
                card2.getProduction().getProduced().get(0).getQuantity());
        assertEquals(card1.getProduction().getConsumed().get(0).getResourceType(),
                card2.getProduction().getConsumed().get(0).getResourceType());
        assertEquals(card1.getProduction().getProduced().get(0).getQuantity(),
                card2.getProduction().getProduced().get(0).getQuantity());

        assertEquals(card1.getCost().get(0).getResourceType(), card2.getCost().get(0).getResourceType());
        assertEquals(card1.getCost().get(1).getResourceType(), card2.getCost().get(1).getResourceType());
        assertEquals(card1.getCost().get(0).getQuantity(), card2.getCost().get(0).getQuantity());
        assertEquals(card1.getCost().get(1).getQuantity(), card2.getCost().get(1).getQuantity());

        assertNotEquals(card1, card2);

        assertNotEquals(card1.getProduction(), card2.getProduction());
        assertNotEquals(card1.getProduction().getConsumed(), card2.getProduction().getConsumed());
        assertNotEquals(card1.getProduction().getProduced(), card2.getProduction().getProduced());
        assertNotEquals(card1.getProduction().getConsumed().get(0), card2.getProduction().getConsumed().get(0));
        assertNotEquals(card1.getProduction().getProduced().get(0), card2.getProduction().getProduced().get(0));

        assertNotEquals(card1.getCost(), card2.getCost());
        assertNotEquals(card1.getCost().get(0), card2.getCost().get(0));
        assertNotEquals(card1.getCost().get(1), card2.getCost().get(1));
    }

    @Test
    public void testGetVP() {
    }

    @Test
    public void testGetCost() {
    }

    @Test
    public void testGetLevel() {
    }

    @Test
    public void testGetColor() {
    }

    @Test
    public void testGetProduction() {
    }
}