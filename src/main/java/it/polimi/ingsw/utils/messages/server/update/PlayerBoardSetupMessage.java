package it.polimi.ingsw.utils.messages.server.update;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.client.structures.*;
import it.polimi.ingsw.server.model.boards.*;
import it.polimi.ingsw.server.model.cards.card.LeaderCard;
import it.polimi.ingsw.server.model.cards.deck.Deck;
import it.polimi.ingsw.utils.messages.HiddenMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * Server setup message for all of the players' playerboards
 */
public class PlayerBoardSetupMessage implements HiddenMessage, ServerUpdateMessage {

    private final PlayerBoardView playerBoard;
    private final boolean turnPlayed;


    /**
     * Constructor for a PlayerBoardSetupMessage given a playerBoard
     * @param playerBoard PlayerBoard contained in the PlayerBoardSetupMessage
     */
    public PlayerBoardSetupMessage(PlayerBoard playerBoard) {
        this.playerBoard = new PlayerBoardView(playerBoard);
        this.turnPlayed = playerBoard.isTurnPlayed();
    }


    @Override
    public void hide() {
        List<LeaderCard> newList = new ArrayList<>();
        playerBoard.getLeaderBoard().getHand().getCards().forEach(c -> newList.add(new LeaderCard()));
        playerBoard.getLeaderBoard().setHand(new Deck(newList));
    }

    @Override
    public void doUpdate(ClientController client) {
        client.getPlayerBoards().add(playerBoard);

        if(client.getNickname().equals(playerBoard.getNickname()))
            client.setMainActionPlayed(turnPlayed);
    }
}
