package it.polimi.ingsw.utils.messages.server;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.exceptions.NotExistingNickname;
import it.polimi.ingsw.server.model.cards.deck.Deck;

import java.util.List;

public class PlayerDevelopmentBSpacesMessage implements ServerUpdateMessage {

    private final List<Deck> spaces;
    private final String nickname;


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

    @Override
    public void doUpdate(ClientController client) {
        try {
            client.playerBoardFromNickname(nickname).getDevelopmentBoard().setSpaces(spaces);
        } catch(NotExistingNickname e){
            e.printStackTrace();
        }
    }
}
