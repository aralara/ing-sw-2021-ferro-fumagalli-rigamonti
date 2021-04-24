package it.polimi.ingsw.server.model.cards.factory;

import it.polimi.ingsw.server.model.FileNames;
import it.polimi.ingsw.server.model.cards.card.LorenzoDev;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class LorenzoDevFactoryTest {

    @Test
    public void testLoadCardFromFile() {
        LorenzoDevFactory lorenzoDevFactory = new LorenzoDevFactory();
        List<LorenzoDev> lorenzoDevs;
        lorenzoDevs = lorenzoDevFactory.loadCardFromFile(FileNames.LORENZO_DEV_FILE.value());
        assertEquals(4, lorenzoDevs.size());
    }
}