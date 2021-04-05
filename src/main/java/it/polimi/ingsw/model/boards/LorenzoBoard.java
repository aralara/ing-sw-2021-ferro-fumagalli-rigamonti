package it.polimi.ingsw.model.boards;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import it.polimi.ingsw.model.cards.card.CardColors;
import it.polimi.ingsw.model.cards.card.LorenzoDev;
import it.polimi.ingsw.model.cards.card.LorenzoFaith;
import it.polimi.ingsw.model.cards.deck.Deck;
import it.polimi.ingsw.model.cards.deck.DevelopmentDeck;
import it.polimi.ingsw.model.games.Game;
import it.polimi.ingsw.model.cards.card.LorenzoCard;
import it.polimi.ingsw.model.games.SingleGame;
import it.polimi.ingsw.model.market.Marble;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

public class LorenzoBoard {

    private Game game;
    private Deck lorenzoDeck;
    private int faith;


    public LorenzoBoard() {
        game = null;
        lorenzoDeck = new Deck();
        faith = 0;
    }


    /**
     * Gets the faith attribute
     * @return Returns faith value
     */
    public int getFaith() {
        return faith;
    }

    /**
     * Adds a set amount of faith to Lorenzo
     * @param faith Faith quantity to be added
     */
    public void addFaith(int faith) {
        this.faith += faith;
    }

    /**
     * Removes a number of development cards from a development deck of a given color
     * @param color Color of the cards to be removed
     * @param quantity Quantity of cards to be removed
     */
    public void takeDevCard(CardColors color, int quantity) {
        /*//getDevelopmentDecks() ritorna lista devDeck
        boolean removed = false;
        int index = 0;
        List<DevelopmentDeck> temp = new ArrayList<>();

        List<DevelopmentDeck> prova = new ArrayList<>(); //sostituire

        //creates a DevelopmentDeck list of the specified color sorted by level
        temp = prova.stream().filter(card -> card.getDeckColor() == color)
                .sorted(Comparator.comparing(DevelopmentDeck::getDeckLevel)).collect(Collectors.toList());

        //searches the first not empty deck
        for(int i=0; i<temp.size() && !removed; i++){
            if(!temp.get(i).isEmpty()){
                index = i;
                removed = true;
            }
        }

        //removes quantity of cards from the found deck
        if(removed){
            for(int i=0; i<quantity; i++)
                prova.get(index).removeFirst();
        }*/
    }

    /**
     * Initializes lorenzoDeck by loading cards from the specified files and randomizes the order by calling refreshDeck
     * @param fileNameDev File containing the LorenzoDev cards
     * @param fileNameFaith File containing the LorenzoFaith cards
     */
    public void initLorenzoDeck(String fileNameDev, String fileNameFaith) {
        initLorenzoDev(fileNameDev);
        initLorenzoFaith(fileNameFaith);
        refreshDeck();
    }

    /**
     * Loads LorenzoDev cards from the specified files
     * @param fileName File containing the LorenzoDev cards
     */
    private void initLorenzoDev(String fileName){
        /*Gson gson = new Gson();

        try {
            LorenzoDev[] jsonLorenzoDevs;
            JsonReader reader = new JsonReader(new FileReader(fileName));
            jsonLorenzoDevs = gson.fromJson(reader, LorenzoDev[].class);

            for (LorenzoDev lorenzoDev : jsonLorenzoDevs) {
                lorenzoDeck.add(new LorenzoDev());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }*/
    }

    /**
     * Loads LorenzoFaith cards from the specified files
     * @param fileName File containing the LorenzoFaith cards
     */
    private void initLorenzoFaith(String fileName){
        /*Gson gson = new Gson();

        try {
            LorenzoFaith[] jsonLorenzoFaith;
            JsonReader reader = new JsonReader(new FileReader(fileName));
            jsonLorenzoFaith = gson.fromJson(reader, LorenzoDev[].class);

            for (LorenzoFaith lorenzoFaith : jsonLorenzoFaith) {
                lorenzoDeck.add(new LorenzoFaith());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }*/
    }

    /**
     * Picks the Lorenzo card at the top of the deck and moves the picked card to the bottom of the deck
     * @return Returns the selected Lorenzo card
     */
    public LorenzoCard pickLorenzoCard() {
        LorenzoCard extracted = (LorenzoCard) (lorenzoDeck.extract(new int[] {0})).get(0);
        lorenzoDeck.add(extracted);
        return extracted;
    }

    /**
     *  Randomizes the order of the Lorenzo cards in the deck
     */
    public void refreshDeck() {
        lorenzoDeck.shuffle();
    }
}
