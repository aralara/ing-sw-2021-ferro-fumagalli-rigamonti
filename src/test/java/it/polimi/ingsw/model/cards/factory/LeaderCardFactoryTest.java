package it.polimi.ingsw.model.cards.factory;

import it.polimi.ingsw.model.FileNames;
import it.polimi.ingsw.model.cards.card.LeaderCard;
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