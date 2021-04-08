package it.polimi.ingsw.model.cards.factory;

import it.polimi.ingsw.model.FileNames;
import it.polimi.ingsw.model.cards.card.LorenzoFaith;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class LorenzoFaithFactoryTest {

    @Test
    public void testLoadCardFromFile() {
        LorenzoFaithFactory lorenzoFaithFactory = new LorenzoFaithFactory();
        List<LorenzoFaith> lorenzoFaiths;
        lorenzoFaiths = lorenzoFaithFactory.loadCardFromFile(FileNames.LORENZO_FAITH_FILE.value());
        assertEquals(3, lorenzoFaiths.size());
    }
}