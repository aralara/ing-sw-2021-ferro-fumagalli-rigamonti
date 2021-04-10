package it.polimi.ingsw.model.cards.ability;

import it.polimi.ingsw.model.boards.PlayerBoard;
import it.polimi.ingsw.model.games.MultiGame;
import it.polimi.ingsw.model.storage.ResourceType;
import org.junit.Test;

import static org.junit.Assert.*;

public class AbilityMarbleTest {

    @Test
    public void testGetResourceType() {
        AbilityMarble am = new AbilityMarble(ResourceType.STONE);
        assertEquals(ResourceType.STONE,am.getResourceType());
    }

    @Test
    public void testActivateAbility() {
        PlayerBoard pb = new PlayerBoard(new MultiGame("Bonucci"),"Bonucci");
        AbilityMarble ad = new AbilityMarble(ResourceType.SERVANT);
        ad.activateAbility(pb);

        assertEquals(1,pb.getAbilityMarbles().size());
        assertEquals(ResourceType.SERVANT, pb.getAbilityMarbles().get(0));

        AbilityMarble ad2 = new AbilityMarble(ResourceType.STONE);

        ad2.activateAbility(pb);

        assertEquals(2,pb.getAbilityMarbles().size());
        assertEquals(ResourceType.SERVANT, pb.getAbilityMarbles().get(0));
        assertEquals(ResourceType.STONE, pb.getAbilityMarbles().get(1));
    }
}