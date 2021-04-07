package it.polimi.ingsw.model.boards;

import it.polimi.ingsw.model.cards.ability.AbilityMarble;
import it.polimi.ingsw.model.cards.ability.SpecialAbility;
import it.polimi.ingsw.model.cards.card.LeaderCard;
import it.polimi.ingsw.model.cards.requirement.Requirement;
import it.polimi.ingsw.model.cards.requirement.RequirementRes;
import it.polimi.ingsw.model.storage.Resource;
import it.polimi.ingsw.model.storage.ResourceType;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class LeaderBoardTest {

    @Test
    public void testSetLeaderHand() {
    }

    @Test
    public void testDiscardLeaderHand() {
    }

    @Test
    public void testPlayLeaderHand() {
    }

    @Test
    public void testCalculateVP() {
        LeaderBoard leaderBoard = new LeaderBoard();
        List<LeaderCard> leaderCards = new ArrayList<>();
        List<Requirement> requirements = new ArrayList<>();
        requirements.add(new RequirementRes(new Resource(ResourceType.SERVANT, 2)));
        SpecialAbility ability = new AbilityMarble(ResourceType.COIN);
        leaderCards.add(new LeaderCard(3, requirements, ability));
        leaderCards.add(new LeaderCard(6, requirements, ability));
        leaderCards.add(new LeaderCard(9, requirements, ability));
        leaderCards.add(new LeaderCard(12, requirements, ability));

        assertEquals(0, leaderBoard.calculateVP());

        leaderBoard.setLeaderHand(leaderCards);
        assertEquals(0, leaderBoard.calculateVP());

        leaderBoard.discardLeaderHand(leaderCards.get(0));
        assertEquals(0, leaderBoard.calculateVP());

        leaderBoard.discardLeaderHand(leaderCards.get(3));
        assertEquals(0, leaderBoard.calculateVP());

        leaderBoard.playLeaderHand(leaderCards.get(2));
        assertEquals(9, leaderBoard.calculateVP());

        leaderBoard.playLeaderHand(leaderCards.get(1));
        assertEquals(15, leaderBoard.calculateVP());
    }
}