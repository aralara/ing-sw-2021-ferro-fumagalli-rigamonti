package it.polimi.ingsw.utils.messages.server;

import it.polimi.ingsw.server.model.cards.deck.Deck;
import it.polimi.ingsw.utils.messages.Message;

public class PlayerLeaderBHandMessage implements Message { //TODO: guarda dubbio drive

    private Deck hand;
    private String nickname;


    public PlayerLeaderBHandMessage(Deck hand, String nickname) {
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
