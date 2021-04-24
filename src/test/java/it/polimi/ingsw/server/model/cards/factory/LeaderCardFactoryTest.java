package it.polimi.ingsw.server.model.cards.factory;

import it.polimi.ingsw.server.model.FileNames;
import it.polimi.ingsw.server.model.cards.card.LeaderCard;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class LeaderCardFactoryTest {

    @Test
    public void testLoadCardFromFile() {
        LeaderCardFactory leaderCardFactory = new LeaderCardFactory();
        List<LeaderCard> leaderCards;
        leaderCards = leaderCardFactory.loadCardFromFile(FileNames.LEADER_CARD_FILE.value());
        assertEquals(16, leaderCards.size());
    }
}