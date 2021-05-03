package it.polimi.ingsw.utils.messages.server;

import it.polimi.ingsw.server.model.cards.deck.Deck;
import it.polimi.ingsw.utils.messages.Message;

import java.util.List;

public class PlayerDevelopmentBSpacesMessage implements Message {

    private List<Deck> spaces;
    private String nickname;


    public PlayerDevelopmentBSpacesMessage(List<Deck> spaces, String nickname) {
        this.spaces = spaces;
        this.nickname = nickname;
    }


    public List<Deck> getSpaces() {
        return spaces;
    }

    public String getNickname(){
        return nickname;
    }
}
