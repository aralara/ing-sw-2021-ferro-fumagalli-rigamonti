package it.polimi.ingsw.utils.messages.server.update;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.utils.exceptions.NotExistingNicknameException;
import it.polimi.ingsw.server.model.cards.deck.Deck;

import java.util.List;

/**
 * Server update message for the player's development spaces
 */
public class PlayerDevelopmentBSpacesMessage implements ServerUpdateMessage {

    private final List<Deck> spaces;
    private final String nickname;


    public PlayerDevelopmentBSpacesMessage(List<Deck> spaces, String nickname) {
        this.spaces = spaces;
        this.nickname = nickname;
    }


    @Override
    public void doUpdate(ClientController client) {
        try {
            client.playerBoardFromNickname(nickname).getDevelopmentBoard().setSpaces(spaces);
        } catch(NotExistingNicknameException e){
            e.printStackTrace();
        }
    }
}
