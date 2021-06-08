package it.polimi.ingsw.server.model.boards;

import it.polimi.ingsw.server.model.FileNames;
import it.polimi.ingsw.server.model.faith.FaithTrack;
import it.polimi.ingsw.server.model.storage.Resource;
import it.polimi.ingsw.server.model.storage.ResourceType;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class FaithBoardTest {

    @Test
    public void testAddFaith() {
        FaithBoard faithBoard = new FaithBoard();

        assertEquals(0, faithBoard.getFaith());
        faithBoard.addFaith(1);
        assertEquals(1, faithBoard.getFaith());
        faithBoard.addFaith(3);
        assertEquals(4, faithBoard.getFaith());
    }

    @Test
    public void testCalculateVP() {
        FaithBoard faithBoard = new FaithBoard();
        FaithTrack faithTrack = new FaithTrack();
        faithTrack.loadTrack(FileNames.VATICAN_REPORT_FILE.value(), FileNames.FAITH_SPACE_FILE.value());

        assertEquals(0, faithBoard.calculateVP(faithTrack));
        faithBoard.addFaith(3);
        assertEquals(1, faithBoard.calculateVP(faithTrack));
        faithBoard.addFaith(1);
        assertEquals(1, faithBoard.calculateVP(faithTrack));
        faithBoard.addFaith(4);
        assertEquals(2, faithBoard.calculateVP(faithTrack));
        faithBoard.addFaith(1);
        assertEquals(4, faithBoard.calculateVP(faithTrack));
        faithTrack.checkReportActivation(8);
        faithBoard.handleReportActivation(faithTrack);
        assertEquals(6, faithBoard.calculateVP(faithTrack));
        faithBoard.addFaith(9);
        faithTrack.checkReportActivation(8);
        faithBoard.handleReportActivation(faithTrack);
        assertEquals(14, faithBoard.calculateVP(faithTrack));
        faithTrack.checkReportActivation(17);
        faithBoard.handleReportActivation(faithTrack);
        assertEquals(17, faithBoard.calculateVP(faithTrack));
        faithBoard.addFaith(4);
        assertEquals(21, faithBoard.calculateVP(faithTrack));
        faithBoard.addFaith(3);
        faithTrack.checkReportActivation(24);
        faithBoard.handleReportActivation(faithTrack);
        assertEquals(29, faithBoard.calculateVP(faithTrack));

    }

    @Test
    public void testHandleReportActivation() {
        FaithTrack faithTrack = new FaithTrack();
        faithTrack.loadTrack(FileNames.VATICAN_REPORT_FILE.value(), FileNames.FAITH_SPACE_FILE.value());
        FaithBoard faithBoard = new FaithBoard();
        for(boolean popeProgressionValue : faithBoard.getPopeProgression()){
            assertFalse(popeProgressionValue);
        }

        faithBoard.addFaith(8);
        faithTrack.checkReportActivation(8);
        faithBoard.handleReportActivation(faithTrack);
        assertTrue(faithBoard.getPopeProgression()[0]);
        assertFalse(faithBoard.getPopeProgression()[1]);
        assertFalse(faithBoard.getPopeProgression()[2]);

        faithBoard.addFaith(3);
        faithTrack.checkReportActivation(16);
        faithBoard.handleReportActivation(faithTrack);
        assertTrue(faithBoard.getPopeProgression()[0]);
        assertFalse(faithBoard.getPopeProgression()[1]);
        assertFalse(faithBoard.getPopeProgression()[2]);

        faithBoard.addFaith(8);
        faithTrack.checkReportActivation(24);
        faithBoard.handleReportActivation(faithTrack);
        assertTrue(faithBoard.getPopeProgression()[0]);
        assertFalse(faithBoard.getPopeProgression()[1]);
        assertTrue(faithBoard.getPopeProgression()[2]);
    }

    @Test
    public void testTakeFaithFromResources() {
        FaithBoard faithBoard = new FaithBoard();
        assertEquals(0, faithBoard.getFaith());
        List<Resource> resources = new ArrayList<>();
        resources.add(new Resource(ResourceType.STONE, 2));
        faithBoard.takeFaithFromResources(resources);
        assertEquals(0, faithBoard.getFaith());
        resources.add(new Resource(ResourceType.FAITH, 1));
        resources.add(new Resource(ResourceType.COIN, 1));
        faithBoard.takeFaithFromResources(resources);
        assertEquals(1, faithBoard.getFaith());
        boolean faith=false;
        for(Resource resource : resources)
            if(resource.getResourceType()==ResourceType.FAITH)
                faith=true;
        assertFalse(faith);
        assertEquals(2, resources.size());
    }

}