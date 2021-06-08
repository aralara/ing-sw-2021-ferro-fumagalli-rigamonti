package it.polimi.ingsw.server.model.boards;

import it.polimi.ingsw.server.model.cards.ability.AbilityMarble;
import it.polimi.ingsw.server.model.cards.ability.SpecialAbility;
import it.polimi.ingsw.server.model.cards.card.LeaderCard;
import it.polimi.ingsw.server.model.cards.requirement.Requirement;
import it.polimi.ingsw.server.model.cards.requirement.RequirementRes;
import it.polimi.ingsw.server.model.storage.Resource;
import it.polimi.ingsw.server.model.storage.ResourceType;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class LeaderBoardTest {

    @Test
    public void testCalculateVP() {
        LeaderBoard leaderBoard = new LeaderBoard();
        List<LeaderCard> leaderCards = new ArrayList<>();
        List<Requirement> requirements = new ArrayList<>();
        requirements.add(new RequirementRes(new Resource(ResourceType.SERVANT, 2)));
        SpecialAbility ability = new AbilityMarble(ResourceType.COIN);
        leaderCards.add(new LeaderCard(1,3, requirements, ability));
        leaderCards.add(new LeaderCard(2,6, requirements, ability));
        leaderCards.add(new LeaderCard(3,9, requirements, ability));
        leaderCards.add(new LeaderCard(4,12, requirements, ability));

        assertEquals(0, leaderBoard.calculateVP());

        leaderBoard.setLeaderHand(leaderCards);
        assertEquals(0, leaderBoard.getBoard().size());
        assertEquals(4, leaderBoard.getHand().size());
        assertEquals(0, leaderBoard.calculateVP());

        leaderBoard.discardLeaderHand(leaderCards.get(0));
        assertEquals(0, leaderBoard.getBoard().size());
        assertEquals(3, leaderBoard.getHand().size());
        assertEquals(0, leaderBoard.calculateVP());

        leaderBoard.discardLeaderHand(leaderCards.get(3));
        assertEquals(0, leaderBoard.getBoard().size());
        assertEquals(2, leaderBoard.getHand().size());
        assertEquals(0, leaderBoard.calculateVP());

        leaderBoard.playLeaderHand(leaderCards.get(2));
        assertEquals(1, leaderBoard.getBoard().size());
        assertEquals(1, leaderBoard.getHand().size());
        assertEquals(9, leaderBoard.calculateVP());

        leaderBoard.playLeaderHand(leaderCards.get(1));
        assertEquals(2, leaderBoard.getBoard().size());
        assertEquals(0, leaderBoard.getHand().size());
        assertEquals(15, leaderBoard.calculateVP());
    }
}