package it.polimi.ingsw.server.model.cards.ability;

import it.polimi.ingsw.server.model.boards.PlayerBoard;
import it.polimi.ingsw.server.model.games.MultiGame;
import it.polimi.ingsw.server.model.storage.Production;
import it.polimi.ingsw.server.model.storage.Resource;
import it.polimi.ingsw.server.model.storage.ResourceType;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class AbilityProductionTest {

    /**
     * Tests the ability getter
     */
    @Test
    public void testGetProductionAndAbilityToString() {
        AbilityProduction ap = new AbilityProduction(new Production(
                new ArrayList<>(List.of(new Resource(ResourceType.COIN,2))),
                new ArrayList<>(List.of(new Resource(ResourceType.SERVANT,1)))));

        assertEquals(2,ap.getProduction().getConsumed().get(0).getQuantity());
        assertEquals(ResourceType.COIN,ap.getProduction().getConsumed().get(0).getResourceType());
        assertEquals(1,ap.getProduction().getProduced().get(0).getQuantity());
        assertEquals(ResourceType.SERVANT,ap.getProduction().getProduced().get(0).getResourceType());
        assertEquals(" • Special ability: You can can gain access to the following production: \n " +
                        "\tConsumed:  > 2 COIN\n" +
                        "\tProduced:  > 1 SERVANT\n",
                ap.abilityToString());
    }

    /**
     * Tests the production ability activation
     */
    @Test
    public void testActivateAbility() {
        PlayerBoard pb = new PlayerBoard("Bonucci");
        AbilityProduction ap = new AbilityProduction(new Production(
                new ArrayList<>(List.of(new Resource(ResourceType.COIN,2))),
                new ArrayList<>(List.of(new Resource(ResourceType.SERVANT,1)))));
        ap.activateAbility(pb);

        assertEquals(1,pb.getAbilityProductions().size());
        assertEquals(2,pb.getAbilityProductions().get(0).getConsumed().get(0).getQuantity());
        assertEquals(ResourceType.COIN,pb.getAbilityProductions().get(0).getConsumed().get(0).getResourceType());
        assertEquals(1,pb.getAbilityProductions().get(0).getProduced().get(0).getQuantity());
        assertEquals(ResourceType.SERVANT,pb.getAbilityProductions().get(0).getProduced().get(0).getResourceType());

        AbilityProduction ap2 = new AbilityProduction(new Production(
                new ArrayList<>(List.of(new Resource(ResourceType.SHIELD,1))),
                new ArrayList<>(List.of(new Resource(ResourceType.FAITH,2),new Resource(ResourceType.COIN,1)))));

        ap2.activateAbility(pb);

        assertEquals(2,pb.getAbilityProductions().size());

        assertEquals(2,pb.getAbilityProductions().get(0).getConsumed().get(0).getQuantity());
        assertEquals(ResourceType.COIN,pb.getAbilityProductions().get(0).getConsumed().get(0).getResourceType());
        assertEquals(1,pb.getAbilityProductions().get(0).getProduced().get(0).getQuantity());
        assertEquals(ResourceType.SERVANT,pb.getAbilityProductions().get(0).getProduced().get(0).getResourceType());


        assertEquals(1,pb.getAbilityProductions().get(1).getConsumed().get(0).getQuantity());
        assertEquals(ResourceType.SHIELD,pb.getAbilityProductions().get(1).getConsumed().get(0).getResourceType());
        assertEquals(2,pb.getAbilityProductions().get(1).getProduced().get(0).getQuantity());
        assertEquals(ResourceType.FAITH,pb.getAbilityProductions().get(1).getProduced().get(0).getResourceType());
        assertEquals(1,pb.getAbilityProductions().get(1).getProduced().get(1).getQuantity());
        assertEquals(ResourceType.COIN,pb.getAbilityProductions().get(1).getProduced().get(1).getResourceType());
    }
}