package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.exceptions.NotExistingNicknameException;
import it.polimi.ingsw.server.model.cards.card.LeaderCard;
import it.polimi.ingsw.utils.messages.client.LeaderCardDiscardMessage;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.List;

public class FirstPhaseController extends GenericController {

    private List<Integer> leaderIndexesToDiscard;

    @FXML private ImageView leader1_imageView, leader2_imageView, leader3_imageView, leader4_imageView;
    @FXML private Button discard1_button, discard2_button, discard3_button, discard4_button;

    /**
     * Adds to the discarded leaders list the card given by parameter and disable its button
     * When the list contains 2 cards, a message is sent to the server
     * @param button Button to disable
     * @param index Index of the card to discard
     */
    private void discard(Button button, int index){
        button.setDisable(true);
        if(leaderIndexesToDiscard == null)
            leaderIndexesToDiscard = new ArrayList<>();
        leaderIndexesToDiscard.add(index-1);
        if(leaderIndexesToDiscard.size()==2) {
            try {
                List<LeaderCard> leadersToDiscard = new ArrayList<>();
                for(int leaderIndex : leaderIndexesToDiscard)
                    leadersToDiscard.add((LeaderCard) getGUI().getLocalPlayerBoard().
                            getLeaderBoard().getHand().get(leaderIndex));
                getGUI().getMessageHandler().sendClientMessage(
                        new LeaderCardDiscardMessage(leadersToDiscard, true));
                getGUIApplication().closePopUpStage();
                getGUI().callAskResourceToEqualize();
            } catch (NotExistingNicknameException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Adds to the discarded list the card in the first position
     */
    public void discard1() {
        discard(discard1_button, 1);
    }

    /**
     * Adds to the discarded list the card in the second position
     */
    public void discard2() {
        discard(discard2_button, 2);
    }

    /**
     * Adds to the discarded list the card in the third position
     */
    public void discard3() {
        discard(discard3_button, 3);
    }

    /**
     * Adds to the discarded list the card in the fourth position
     */
    public void discard4() {
        discard(discard4_button, 4);
    }

    /**
     * Sets the leaders' imageViews by the IDs given by parameter
     * @param idLeaders List of leaders' IDs
     */
    public void setLeaders(List<Integer> idLeaders){
        leader1_imageView.setImage(new Image(getClass().getResourceAsStream("/imgs/leaderCards/"
                + idLeaders.get(0)+".png")));
        leader2_imageView.setImage(new Image(getClass().getResourceAsStream("/imgs/leaderCards/"
                + idLeaders.get(1)+".png")));
        leader3_imageView.setImage(new Image(getClass().getResourceAsStream("/imgs/leaderCards/"
                + idLeaders.get(2)+".png")));
        leader4_imageView.setImage(new Image(getClass().getResourceAsStream("/imgs/leaderCards/"
                + idLeaders.get(3)+".png")));
    }
}
