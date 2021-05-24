package it.polimi.ingsw.utils.messages.server.update;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.exceptions.NotExistingNicknameException;
import it.polimi.ingsw.server.model.cards.card.LeaderCard;
import it.polimi.ingsw.server.model.cards.deck.Deck;
import it.polimi.ingsw.utils.messages.HiddenMessage;

import java.util.ArrayList;
import java.util.List;

public class PlayerLeaderBHandMessage implements HiddenMessage, ServerUpdateMessage {

    private Deck hand;
    private final String nickname;


    public PlayerLeaderBHandMessage(Deck hand, String nickname) {
        this.hand = hand;
        this.nickname = nickname;
    }


    @Override
    public void hide() {
        List<LeaderCard> newList = new ArrayList<>();
        hand.getCards().forEach(c -> newList.add(new LeaderCard()));
        hand = new Deck(newList);
    }

    @Override
    public void doUpdate(ClientController client) {
        try {
            client.playerBoardFromNickname(nickname).getLeaderBoard().setHand(hand);
        } catch(NotExistingNicknameException e){
            e.printStackTrace();
        }
    }
}
