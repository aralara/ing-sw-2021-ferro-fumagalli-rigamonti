package it.polimi.ingsw.server.model.boards;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests methods of Player class
 */
public class PlayerTest {

    /**
     * Tests if getters return the correct values
     */
    @Test
    public void testGettersSetters() {
        String nickname = "testNickname";
        Player player = new Player(nickname);
        assertEquals(nickname, player.getNickname());
        assertEquals(-1, player.getTotalVP());
        assertEquals(0, player.getFinalPosition());
        player.setTotalVP(56);
        assertEquals(56, player.getTotalVP());
        assertEquals(0, player.getFinalPosition());
        player.setFinalPosition(2);
        assertEquals(56, player.getTotalVP());
        assertEquals(2, player.getFinalPosition());
    }
}