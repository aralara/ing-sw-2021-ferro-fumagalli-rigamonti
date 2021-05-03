package it.polimi.ingsw.utils.messages.server;

import it.polimi.ingsw.server.model.cards.deck.Deck;
import it.polimi.ingsw.utils.messages.Message;

public class PlayerLeaderHandMessage implements Message {

    private Deck hand;
    private String nickname;


    public PlayerLeaderHandMessage(Deck hand, String nickname) {
        this.hand = hand;
        this.nickname = nickname;
    }


    public Deck getHand() {
        return hand;
    }

    public String getNickname(){
        return nickname;
    }
}
