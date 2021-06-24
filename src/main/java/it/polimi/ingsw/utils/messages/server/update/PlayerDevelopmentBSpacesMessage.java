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


    /**
     * Constructor for a PlayerDevelopmentBSpacesMessage given the spaces and a nickname
     * @param spaces List of decks representing the spaces on a player's development board
     * @param nickname Nickname of the player
     */
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
