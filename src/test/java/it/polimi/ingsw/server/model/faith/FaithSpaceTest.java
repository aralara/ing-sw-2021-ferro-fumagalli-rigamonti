package it.polimi.ingsw.server.model.faith;

import org.junit.Test;

import static org.junit.Assert.*;

public class FaithSpaceTest {

    @Test
    public void testGetters() {
        int VP=4, position=6;
        FaithSpace faithSpace = new FaithSpace(VP, position);
        assertEquals(VP, faithSpace.getVP());
        assertEquals(position, faithSpace.getPosition());
    }
}