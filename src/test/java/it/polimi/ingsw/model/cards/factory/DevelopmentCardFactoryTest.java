package it.polimi.ingsw.model.cards.factory;

import it.polimi.ingsw.model.FileNames;
import it.polimi.ingsw.model.cards.card.DevelopmentCard;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class DevelopmentCardFactoryTest {

    @Test
    public void testLoadCardFromFile() {
        DevelopmentCardFactory developmentCardFactory = new DevelopmentCardFactory();
        List<DevelopmentCard> developmentCards;
        developmentCards = developmentCardFactory.loadCardFromFile(FileNames.DEV_CARD_FILE.value());
        assertEquals(48, developmentCards.size());
    }
}