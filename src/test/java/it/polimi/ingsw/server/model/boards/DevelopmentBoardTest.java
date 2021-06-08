package it.polimi.ingsw.server.model.boards;

import it.polimi.ingsw.exceptions.InvalidSpaceException;
import it.polimi.ingsw.server.model.cards.card.CardColors;
import it.polimi.ingsw.server.model.cards.card.DevelopmentCard;
import it.polimi.ingsw.server.model.storage.Production;
import it.polimi.ingsw.server.model.storage.Resource;
import it.polimi.ingsw.server.model.storage.ResourceType;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class DevelopmentBoardTest {

    @Test
    public void testGetActiveProductions() {
        DevelopmentBoard developmentBoard = new DevelopmentBoard();

        List<Resource> consumed1 = new ArrayList<>();
        List<Resource> produced1 = new ArrayList<>();
        List<Resource> cost = new ArrayList<>();
        consumed1.add(new Resource(ResourceType.SHIELD, 1));
        produced1.add(new Resource(ResourceType.SERVANT, 2));
        produced1.add(new Resource(ResourceType.FAITH, 1));
        cost.add(new Resource(ResourceType.COIN, 2));
        Production production1 = new Production(consumed1, produced1);

        DevelopmentCard developmentCard = new DevelopmentCard(0,3, CardColors.PURPLE, 1, production1, cost);
        try {
            developmentBoard.addDevCard(developmentCard, 1);
        }
        catch (InvalidSpaceException e){
            e.printStackTrace();
        }
        List<Production> gotProductions = developmentBoard.getActiveProductions();

        assertEquals(1, gotProductions.size());

        boolean checked;
        for(Production production : gotProductions){
            for(Resource resourceCons : production.getConsumed()){
                checked = false;
                for (Resource resCons1 : consumed1) {
                    if (resourceCons.getResourceType() == resCons1.getResourceType()
                            && resourceCons.getQuantity() == resCons1.getQuantity())
                        checked = true;
                }
                assertTrue(checked);
            }
            for(Resource resourceProd : production.getProduced()){
                checked = false;
                for (Resource resProd1 : produced1) {
                    if (resourceProd.getResourceType() == resProd1.getResourceType()
                            && resourceProd.getQuantity() == resProd1.getQuantity())
                        checked = true;
                }
                assertTrue(checked);
            }
        }

        List<Resource> consumed2 = new ArrayList<>();
        List<Resource> produced2 = new ArrayList<>();
        consumed2.add(new Resource(ResourceType.COIN, 1));
        consumed2.add(new Resource(ResourceType.STONE, 2));
        produced2.add(new Resource(ResourceType.SHIELD, 3));
        Production production2 = new Production(consumed2, produced2);

        developmentCard = new DevelopmentCard(0,3, CardColors.PURPLE, 2, production2, cost);
        try {
            developmentBoard.addDevCard(developmentCard, 1);
        }
        catch (InvalidSpaceException e){
            e.printStackTrace();
        }
        gotProductions = developmentBoard.getActiveProductions();

        assertEquals(1, gotProductions.size());

        for(Production production : gotProductions){
            for(Resource resourceCons : production.getConsumed()){
                checked = false;
                for (Resource resCons1 : consumed1) {
                    if (resourceCons.getResourceType() == resCons1.getResourceType()
                            && resourceCons.getQuantity() == resCons1.getQuantity())
                        checked = true;
                }
                assertFalse(checked);
            }
            for(Resource resourceProd : production.getProduced()){
                checked = false;
                for (Resource resProd1 : produced1) {
                    if (resourceProd.getResourceType() == resProd1.getResourceType()
                            && resourceProd.getQuantity() == resProd1.getQuantity())
                        checked = true;
                }
                assertFalse(checked);
            }
            for(Resource resourceCons : production.getConsumed()){
                checked = false;
                for (Resource resCons2 : consumed2) {
                    if (resourceCons.getResourceType() == resCons2.getResourceType()
                            && resourceCons.getQuantity() == resCons2.getQuantity())
                        checked = true;
                }
                assertTrue(checked);
            }
            for(Resource resourceProd : production.getProduced()){
                checked = false;
                for (Resource resProd2 : produced2) {
                    if (resourceProd.getResourceType() == resProd2.getResourceType()
                            && resourceProd.getQuantity() == resProd2.getQuantity())
                        checked = true;
                }
                assertTrue(checked);
            }
        }
    }

    @Test
    public void testAddDevCardAndNumberOfCards() {
        DevelopmentBoard developmentBoard = new DevelopmentBoard();
        assertEquals(0, developmentBoard.numberOfCards());

        List<Resource> consumed = new ArrayList<>();
        List<Resource> produced = new ArrayList<>();
        List<Resource> cost = new ArrayList<>();
        consumed.add(new Resource(ResourceType.SHIELD, 1));
        produced.add(new Resource(ResourceType.SERVANT, 2));
        produced.add(new Resource(ResourceType.FAITH, 1));
        cost.add(new Resource(ResourceType.COIN, 2));
        Production production = new Production(consumed, produced);

        try {
            DevelopmentCard developmentCard = new DevelopmentCard(0,3, CardColors.PURPLE, 1, production, cost);
            assertTrue(developmentBoard.addDevCard(developmentCard, 0));
            assertEquals(1, developmentBoard.numberOfCards());

            developmentCard = new DevelopmentCard(0,3, CardColors.YELLOW, 1, production, cost);
            assertTrue(developmentBoard.addDevCard(developmentCard, 1));
            assertEquals(2, developmentBoard.numberOfCards());

            developmentCard = new DevelopmentCard(0,2, CardColors.YELLOW, 2, production, cost);
            assertFalse(developmentBoard.addDevCard(developmentCard, 2));
            assertEquals(2, developmentBoard.numberOfCards());

            assertTrue(developmentBoard.addDevCard(developmentCard, 1));
            assertEquals(3, developmentBoard.numberOfCards());

            developmentCard = new DevelopmentCard(0,3, CardColors.GREEN, 1, production, cost);
            assertFalse(developmentBoard.addDevCard(developmentCard, 1));
            assertEquals(3, developmentBoard.numberOfCards());

            assertTrue(developmentBoard.addDevCard(developmentCard, 2));
            assertEquals(4, developmentBoard.numberOfCards());

            developmentCard = new DevelopmentCard(0,1, CardColors.YELLOW, 2, production, cost);
            assertTrue(developmentBoard.addDevCard(developmentCard, 2));
            assertEquals(5, developmentBoard.numberOfCards());

            developmentCard = new DevelopmentCard(0,3, CardColors.BLUE, 3, production, cost);
            assertTrue(developmentBoard.addDevCard(developmentCard, 1));
            assertEquals(6, developmentBoard.numberOfCards());

            developmentCard = new DevelopmentCard(0,6, CardColors.YELLOW, 2, production, cost);
            assertTrue(developmentBoard.addDevCard(developmentCard, 0));
            assertEquals(7, developmentBoard.numberOfCards());
        }
        catch (InvalidSpaceException e){
            e.printStackTrace();
        }
    }

    @Test
    public void testCheckDevCardAddable() {
        DevelopmentBoard developmentBoard = new DevelopmentBoard();

        List<Resource> consumed = new ArrayList<>();
        List<Resource> produced = new ArrayList<>();
        List<Resource> cost = new ArrayList<>();
        consumed.add(new Resource(ResourceType.SHIELD, 1));
        produced.add(new Resource(ResourceType.SERVANT, 2));
        produced.add(new Resource(ResourceType.FAITH, 1));
        cost.add(new Resource(ResourceType.COIN, 2));
        Production production = new Production(consumed, produced);

        try {
            DevelopmentCard developmentCard = new DevelopmentCard(0,3, CardColors.GREEN, 2, production, cost);
            assertFalse(developmentBoard.checkDevCardAddable(developmentCard,0));

            developmentCard = new DevelopmentCard(0,3, CardColors.PURPLE, 1, production, cost);
            assertTrue(developmentBoard.checkDevCardAddable(developmentCard,0));
            developmentBoard.addDevCard(developmentCard, 0);

            developmentCard = new DevelopmentCard(0,3, CardColors.YELLOW, 1, production, cost);
            assertTrue(developmentBoard.checkDevCardAddable(developmentCard,1));
            developmentBoard.addDevCard(developmentCard, 1);

            developmentCard = new DevelopmentCard(0,3, CardColors.GREEN, 3, production, cost);
            assertFalse(developmentBoard.checkDevCardAddable(developmentCard,2));

            developmentCard = new DevelopmentCard(0,3, CardColors.YELLOW, 2, production, cost);
            assertTrue(developmentBoard.checkDevCardAddable(developmentCard,1));
            developmentBoard.addDevCard(developmentCard, 1);

            developmentCard = new DevelopmentCard(0,3, CardColors.GREEN, 1, production, cost);
            assertTrue(developmentBoard.checkDevCardAddable(developmentCard,2));
            developmentBoard.addDevCard(developmentCard, 2);

            developmentCard = new DevelopmentCard(0,3, CardColors.BLUE, 1, production, cost);
            assertFalse(developmentBoard.checkDevCardAddable(developmentCard,0));

            developmentCard = new DevelopmentCard(0,3, CardColors.GREEN, 3, production, cost);
            assertTrue(developmentBoard.checkDevCardAddable(developmentCard,1));
            developmentBoard.addDevCard(developmentCard, 1);

            developmentCard = new DevelopmentCard(0,3, CardColors.PURPLE, 2, production, cost);
            assertTrue(developmentBoard.checkDevCardAddable(developmentCard,2));
            developmentBoard.addDevCard(developmentCard, 2);

            developmentCard = new DevelopmentCard(0,3, CardColors.PURPLE, 2, production, cost);
            assertTrue(developmentBoard.checkDevCardAddable(developmentCard,0));
            developmentBoard.addDevCard(developmentCard, 0);

            developmentCard = new DevelopmentCard(0,3, CardColors.GREEN, 2, production, cost);
            assertFalse(developmentBoard.checkDevCardAddable(developmentCard,2));

            developmentCard = new DevelopmentCard(0,3, CardColors.PURPLE, 3, production, cost);
            assertTrue(developmentBoard.checkDevCardAddable(developmentCard,0));
            developmentBoard.addDevCard(developmentCard, 0);

            developmentCard = new DevelopmentCard(0,3, CardColors.YELLOW, 3, production, cost);
            assertTrue(developmentBoard.checkDevCardAddable(developmentCard,2));
            developmentBoard.addDevCard(developmentCard, 2);

            developmentCard = new DevelopmentCard(0,3, CardColors.BLUE, 3, production, cost);
            assertFalse(developmentBoard.checkDevCardAddable(developmentCard,0));
        }
        catch (InvalidSpaceException e){
            e.printStackTrace();
        }
    }

    @Test
    public void testCheckRequirement() {
        DevelopmentBoard developmentBoard = new DevelopmentBoard();

        List<Resource> consumed = new ArrayList<>();
        List<Resource> produced = new ArrayList<>();
        List<Resource> cost = new ArrayList<>();
        consumed.add(new Resource(ResourceType.SHIELD, 1));
        produced.add(new Resource(ResourceType.SERVANT, 2));
        produced.add(new Resource(ResourceType.FAITH, 1));
        cost.add(new Resource(ResourceType.COIN, 2));
        Production production = new Production(consumed, produced);

        try {
            DevelopmentCard developmentCard = new DevelopmentCard(0,3, CardColors.GREEN, 1, production, cost);
            developmentBoard.addDevCard(developmentCard, 1);

            developmentCard = new DevelopmentCard(0,3, CardColors.YELLOW, 1, production, cost);
            developmentBoard.addDevCard(developmentCard, 2);

            developmentCard = new DevelopmentCard(0,3, CardColors.PURPLE, 2, production, cost);
            developmentBoard.addDevCard(developmentCard, 2);

            developmentCard = new DevelopmentCard(0,3, CardColors.GREEN, 3, production, cost);
            developmentBoard.addDevCard(developmentCard, 2);
        }
        catch (InvalidSpaceException e){
            e.printStackTrace();
        }

        assertTrue(developmentBoard.checkRequirement(CardColors.PURPLE, 2, 1));
        assertTrue(developmentBoard.checkRequirement(CardColors.PURPLE, 1, 1));
        assertFalse(developmentBoard.checkRequirement(CardColors.PURPLE, 2, 2));
        assertTrue(developmentBoard.checkRequirement(CardColors.GREEN, 1, 2));
        assertTrue(developmentBoard.checkRequirement(CardColors.YELLOW, 1, 1));
        assertFalse(developmentBoard.checkRequirement(CardColors.YELLOW, 2, 1));
        assertFalse(developmentBoard.checkRequirement(CardColors.BLUE, 1, 1));
    }

    @Test
    public void testCalculateVP() {
        DevelopmentBoard developmentBoard = new DevelopmentBoard();
        assertEquals(0, developmentBoard.calculateVP());

        List<Resource> consumed = new ArrayList<>();
        List<Resource> produced = new ArrayList<>();
        List<Resource> cost = new ArrayList<>();
        consumed.add(new Resource(ResourceType.SHIELD, 1));
        produced.add(new Resource(ResourceType.SERVANT, 2));
        produced.add(new Resource(ResourceType.FAITH, 1));
        cost.add(new Resource(ResourceType.COIN, 2));
        Production production = new Production(consumed, produced);

        try {
            DevelopmentCard developmentCard = new DevelopmentCard(0,3, CardColors.PURPLE, 1, production, cost);
            developmentBoard.addDevCard(developmentCard, 0);
            assertEquals(3, developmentBoard.calculateVP());

            developmentCard = new DevelopmentCard(0,4, CardColors.YELLOW, 1, production, cost);
            developmentBoard.addDevCard(developmentCard, 1);
            assertEquals(7, developmentBoard.calculateVP());

            developmentCard = new DevelopmentCard(0,2, CardColors.YELLOW, 2, production, cost);
            developmentBoard.addDevCard(developmentCard, 1);
            assertEquals(9, developmentBoard.calculateVP());

            developmentCard = new DevelopmentCard(0,1, CardColors.GREEN, 1, production, cost);
            developmentBoard.addDevCard(developmentCard, 2);
            assertEquals(10, developmentBoard.calculateVP());

            developmentCard = new DevelopmentCard(0,7, CardColors.YELLOW, 2, production, cost);
            developmentBoard.addDevCard(developmentCard, 2);
            assertEquals(17, developmentBoard.calculateVP());

            developmentCard = new DevelopmentCard(0,12, CardColors.BLUE, 3, production, cost);
            developmentBoard.addDevCard(developmentCard, 1);
            assertEquals(29, developmentBoard.calculateVP());

            developmentCard = new DevelopmentCard(0,6, CardColors.YELLOW, 2, production, cost);
            developmentBoard.addDevCard(developmentCard, 0);
            assertEquals(35, developmentBoard.calculateVP());

            developmentCard = new DevelopmentCard(0,10, CardColors.BLUE, 3, production, cost);
            developmentBoard.addDevCard(developmentCard, 0);
            assertEquals(45, developmentBoard.calculateVP());

            developmentCard = new DevelopmentCard(0,11, CardColors.GREEN, 3, production, cost);
            developmentBoard.addDevCard(developmentCard, 2);
            assertEquals(56, developmentBoard.calculateVP());
        }
        catch (InvalidSpaceException e){
            e.printStackTrace();
        }
    }

    @Test
    public void testGetSpaces() {
        DevelopmentBoard developmentBoard = new DevelopmentBoard();
        assertEquals(3, developmentBoard.getSpaces().size());
    }
}