package it.polimi.ingsw.server.model.boards;

import it.polimi.ingsw.server.model.FileNames;
import it.polimi.ingsw.server.model.cards.ability.AbilityWarehouse;
import it.polimi.ingsw.server.model.cards.card.CardColors;
import it.polimi.ingsw.server.model.cards.card.DevelopmentCard;
import it.polimi.ingsw.server.model.cards.card.LeaderCard;
import it.polimi.ingsw.server.model.cards.requirement.Requirement;
import it.polimi.ingsw.server.model.cards.requirement.RequirementRes;
import it.polimi.ingsw.server.model.faith.FaithTrack;
import it.polimi.ingsw.server.model.storage.*;
import it.polimi.ingsw.utils.exceptions.InvalidSpaceException;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Tests methods of PlayerBoard class
 */
public class PlayerBoardTest {

    /**
     * Tests if attributes are given correctly
     */
    @Test
    public void testGetters() {
        PlayerBoard pb = new PlayerBoard("user");
        assertEquals("user", pb.getPlayer().getNickname());
        assertNotNull(pb.getDevelopmentBoard());
        assertNotNull(pb.getLeaderBoard());
        assertNotNull(pb.getFaithBoard());
        assertNotNull(pb.getWarehouse());
        assertNotNull(pb.getStrongbox());
        assertFalse(pb.isFirstPlayer());
        pb.firstPlayer();
        assertTrue(pb.isFirstPlayer());
        assertFalse(pb.isTurnPlayed());
        pb.setTurnPlayed(true);
        assertTrue(pb.isTurnPlayed());
        assertNotNull(pb.getAbilityProductions());
        assertNotNull(pb.getAbilityMarbles());
        assertNotNull(pb.getAbilityDiscounts());
    }

    /**
     * Tests if a returned list containing all of the player productions correctly
     */
    @Test
    public void testCreateProductionStock() {
        PlayerBoard pb = new PlayerBoard("user");
        assertEquals(1, pb.createProductionStock().size());
        assertEquals("\tConsumed:  > 2 WILDCARD\n\tProduced:  > 1 WILDCARD\n",
                pb.createProductionStock().get(0).toString());
        List<Resource> res1 = new ArrayList<>(), res2 = new ArrayList<>();
        res2.add(new Resource(ResourceType.COIN, 2));
        res1.add(new Resource(ResourceType.STONE, 1));
        Production production = new Production(res1, res2);
        res2.add(new Resource(ResourceType.SERVANT, 1));
        try {
            pb.getDevelopmentBoard().addDevCard(new DevelopmentCard(
                    0, 2, CardColors.BLUE, 1, production, res2),0);
        } catch (InvalidSpaceException e) {
            e.printStackTrace();
        }
        assertEquals(2, pb.createProductionStock().size());
        assertEquals("\tConsumed:  > 1 STONE\n\tProduced:  > 2 COIN\n\t           > 1 SERVANT\n",
                pb.createProductionStock().get(1).toString());
    }

    /**
     * Tests if the total of VPs is calculated correctly
     */
    @Test
    public void testCalculateVP() {
        PlayerBoard pb = new PlayerBoard("user");
        List<Resource> res1 = new ArrayList<>(), res2 = new ArrayList<>();
        res2.add(new Resource(ResourceType.COIN, 2));
        res1.add(new Resource(ResourceType.STONE, 1));
        Production production = new Production(res1, res2);
        res2.add(new Resource(ResourceType.SERVANT, 1));
        try {
            pb.getDevelopmentBoard().addDevCard(new DevelopmentCard(
                    0, 2, CardColors.BLUE, 1, production, res2),0);
        } catch (InvalidSpaceException e) {
            e.printStackTrace();
        }
        FaithTrack faithTrack = new FaithTrack();
        faithTrack.loadTrack(FileNames.VATICAN_REPORT_FILE.value(), FileNames.FAITH_SPACE_FILE.value());
        pb.getFaithBoard().addFaith(4);
        pb.calculateVP(faithTrack);
        assertEquals(3, pb.getPlayer().getTotalVP());
    }

    /**
     * Tests if a card is bought correctly, tests if resources can be taken and consequently if they're taken
     * successfully from storages
     */
    @Test
    public void testBuyDevCardAndCanTakeFromStoragesAndTakeFromStorages() {
        PlayerBoard pb = new PlayerBoard("user");
        List<Resource> res1 = new ArrayList<>(), res2 = new ArrayList<>();
        res2.add(new Resource(ResourceType.COIN, 2));
        res1.add(new Resource(ResourceType.STONE, 1));
        Production production = new Production(res1, res2);
        res2.add(new Resource(ResourceType.SERVANT, 1));
        DevelopmentCard dc = new DevelopmentCard();
        try {
            pb.getDevelopmentBoard().addDevCard(new DevelopmentCard(
                    0, 2, CardColors.BLUE, 1, production, res2),0);
        } catch (InvalidSpaceException e) {
            e.printStackTrace();
        }

        List<RequestResources> requestResources = new ArrayList<>();
        requestResources.add(new RequestResources(res2, StorageType.WAREHOUSE));
        assertFalse(pb.buyDevCard(dc,0, requestResources));
        requestResources.clear();
        requestResources.add(new RequestResources(res2, StorageType.STRONGBOX));
        assertFalse(pb.buyDevCard(dc,0, requestResources));
        requestResources.clear();
        requestResources.add(new RequestResources(res2, StorageType.LEADER));
        assertFalse(pb.buyDevCard(dc,0, requestResources));

        requestResources.clear();
        pb.getWarehouse().addShelf(new Shelf(ResourceType.SERVANT, new Resource(ResourceType.SERVANT,1),
                1,false));
        pb.getWarehouse().addShelf(new Shelf(ResourceType.COIN, new Resource(ResourceType.COIN,1),
                2,true));
        res2 = new ArrayList<>();
        res2.add(new Resource(ResourceType.COIN, 1));
        pb.getStrongbox().addResources(res2);
        requestResources.add(new RequestResources(res2, StorageType.LEADER));
        requestResources.add(new RequestResources(res2, StorageType.STRONGBOX));
        res2 = new ArrayList<>();
        res2.add(new Resource(ResourceType.SERVANT, 1));
        requestResources.add(new RequestResources(res2, StorageType.WAREHOUSE));
        assertTrue(pb.buyDevCard(dc, 0, requestResources));

    }

    /**
     * Tests if productions can be activated and, in case, if the resources are stored correctly
     */
    @Test
    public void testActivateProductions() {
        PlayerBoard pb = new PlayerBoard("user");
        List<Resource> res1 = new ArrayList<>(), res2 = new ArrayList<>();
        res2.add(new Resource(ResourceType.COIN, 2));
        res1.add(new Resource(ResourceType.STONE, 1));
        Production production = new Production(res1, res2);
        res2.add(new Resource(ResourceType.SERVANT, 1));
        try {
            pb.getDevelopmentBoard().addDevCard(new DevelopmentCard(
                    0, 2, CardColors.BLUE, 1, production, res2),0);
        } catch (InvalidSpaceException e) {
            e.printStackTrace();
        }
        pb.getWarehouse().addShelf(new Shelf(ResourceType.STONE, new Resource(ResourceType.STONE,1),
                1,false));
        List<RequestResources> requestResources = new ArrayList<>();
        requestResources.add(new RequestResources(res1, StorageType.WAREHOUSE));
        res2 = new ArrayList<>();
        res2.add(new Resource(ResourceType.COIN, 2));
        assertTrue(pb.activateProductions(res2, requestResources));
        for(Resource resource : pb.getStrongbox().getList())
            if(resource.getResourceType() == ResourceType.COIN)
                assertEquals(2, resource.getQuantity());
    }

    /**
     * Tests if a leader can be activated and if it's moved correctly
     */
    @Test
    public void testPlayLeaderCard() {
        PlayerBoard pb = new PlayerBoard("user");
        List<Requirement> requirements = new ArrayList<>();
        requirements.add(new RequirementRes(new Resource(ResourceType.SHIELD, 5)));
        LeaderCard leaderCard = new LeaderCard(0, 3, requirements, new AbilityWarehouse(ResourceType.COIN));
        pb.getLeaderBoard().getHand().add(leaderCard);
        assertEquals(1, pb.getLeaderBoard().getHand().size());
        assertEquals(0, pb.getLeaderBoard().getBoard().size());
        assertFalse(pb.playLeaderCard(leaderCard));
        List<Resource> res = new ArrayList<>();
        res.add(new Resource(ResourceType.SHIELD, 5));
        pb.getStrongbox().addResources(res);
        assertTrue(pb.playLeaderCard(leaderCard));
        assertEquals(0, pb.getLeaderBoard().getHand().size());
        assertEquals(1, pb.getLeaderBoard().getBoard().size());
    }
}