package it.polimi.ingsw.model.games;

import it.polimi.ingsw.model.FileNames;
import it.polimi.ingsw.model.boards.PlayerBoard;
import it.polimi.ingsw.model.cards.card.*;
import it.polimi.ingsw.model.cards.deck.Deck;
import it.polimi.ingsw.model.cards.deck.DevelopmentDeck;
import it.polimi.ingsw.model.cards.factory.DevelopmentCardFactory;
import it.polimi.ingsw.model.cards.factory.LeaderCardFactory;
import it.polimi.ingsw.model.faith.FaithTrack;
import it.polimi.ingsw.model.market.Market;
import it.polimi.ingsw.model.storage.RequestResources;
import it.polimi.ingsw.model.storage.Resource;
import it.polimi.ingsw.model.storage.Shelf;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public abstract class Game {

    private List<PlayerBoard> playerBoards;
    private Market market;
    private List<DevelopmentDeck> developmentDecks;
    private FaithTrack faithTrack;
    private int playerNumber, currentPlayer;


    public Game() {
        initGame();
    }


    /**
     * Gets the playerBoards attribute
     * @return Returns playerBoards value
     */
    List<PlayerBoard> getplayerBoards() {
        return playerBoards;
    }

    /**
     * Gets the developmentDecks attribute
     * @return Returns developmentDecks value
     */
    List<DevelopmentDeck> getDevelopmentDecks() {
        return this.developmentDecks;
    }

    /**
     * Sets the playerBoards attribute
     * @param playerBoards New attribute value
     */
    void setPlayerBoards (List<PlayerBoard> playerBoards) {
        this.playerBoards = playerBoards;
    }

    /**
     * Initializes a game by calling initMarket, initDevelopment, initFaithTrack, initLeaders in this order
     */
    public void initGame() {
        initMarket();
        initDevelopment();
        initFaithTrack();
        initLeaders();
    }

    /**
     * Initializes the market
     */
    void initMarket() {
        this.market = new Market();
        market.loadMarket(FileNames.MARKET_FILE.value());
    }

    /**
     * Loads development cards from their file utilizing DevelopmentCardFactory and split them into 12 DevelopmentDeck
     */
    void initDevelopment() {
        this.developmentDecks = new ArrayList<>();
        DevelopmentCardFactory devCardFactory = new DevelopmentCardFactory();
        Deck devCardDeck = new Deck(devCardFactory.loadCardFromFile(FileNames.DEV_CARD_FILE.value()));
        while(!devCardDeck.isEmpty())
            developmentDecks.add(new DevelopmentDeck(devCardDeck));
    }

    /**
     * Loads faith track information from his file
     */
    void initFaithTrack() {
        this.faithTrack = new FaithTrack();
        faithTrack.loadTrack();
    }

    /**
     * Loads leader cards from their file utilizing LeaderCardFactory and sends four random cards to each player
     */
    void initLeaders() {
        int[] first4 = new int[]{ 0, 1, 2, 3};
        LeaderCardFactory leadCardFactory = new LeaderCardFactory();
        Deck leadCardDeck = new Deck(leadCardFactory.loadCardFromFile(FileNames.LEADER_CARD_FILE.value()));
        leadCardDeck.shuffle();
        for(PlayerBoard pBoard : playerBoards){
            List<LeaderCard> leadCardsPlayer = leadCardDeck.extract(first4).stream().map(s -> (LeaderCard)s)
                    .collect(Collectors.toList());
            pBoard.addLeaderCards(leadCardsPlayer);
        }
    }

    /**
     * Discards leader cards from the hand of a player
     * @param player Index of the player discarding cards
     * @param cards List of leader cards to discard
     */
    public void discardLeaders(int player, List<LeaderCard> cards) {
        for(LeaderCard card : cards)
            playerBoards.get(player).discardLeader(card);
    }

    /**
     * Adds resources organized in shelves to the specified player's warehouse
     * @param player Index of the player to add resources to
     * @param shelves List of shelves containing resources to add
     * @param discarded List of resources to be discarded
     */
    public void addResourcesToWarehouse(int player, List<Shelf> shelves, List<Resource> discarded) {
        boolean success = playerBoards.get(player).getWarehouse().changeConfiguration(shelves);
        if(discarded.size() > 0 && success)
            addFaithAll(discarded.size());
    }

    /**
     * Method invoked to let the next player play his turn
     */
    public abstract void loadNextTurn();

    /**
     * Method invoked to take resources from the market
     * @param row Row chosen by the player, if the player chose a column it is -1
     * @param column Column chosen by the player, if the player chose a row it is -1
     * @return Returns a list of resources corresponding to the marbles contained in the market
     */
    public List<Resource> getFromMarket(int row, int column) {
        return market.chooseCoordinates(row, column);
    }

    /**
     * Checks if a DevelopmentCard can be bought and placed on the board for the current player
     * @param card The development card to be added
     * @return Returns true if the card can be added, false otherwise
     */
    public boolean canBuyDevCard(DevelopmentCard card) {
        return playerBoards.get(currentPlayer).canBuyDevCard(card);
    }

    /**
     * If the resources contained in the requests match the actual available resources detained by the current player,
     * buys the card for said player and places it
     * @param card The development card to buy
     * @param space The position where the card will be placed
     * @param requests List of requests containing resource quantity and location
     * @return Returns true if the card is bought, false otherwise
     */
    public boolean buyDevCard(DevelopmentCard card, int space, List<RequestResources> requests) {
        return playerBoards.get(currentPlayer).buyDevCard(card, space, requests);
    }

    /**
     * Checks if a list of productions can be activated for the current player by checking if they own enough resources
     * @param consumed The list of resources to be consumed
     * @return Returns true if the productions can be activated, false otherwise
     */
    public boolean canActivateProductions(List<Resource> consumed) {
        return playerBoards.get(currentPlayer).canActivateProductions(consumed);
    }

    /**
     * If the resources contained in the requests match the actual available resources detained by the current player,
     * activates the productions for said player by adding the produced resources to their Strongbox
     * @param produced List of resources to produce
     * @param requests List of requests containing resource quantity and location for the spent resources
     * @return Returns true if the card is bought, false otherwise
     */
    public boolean activateProductions(List<Resource> produced, List<RequestResources> requests) {
        return playerBoards.get(currentPlayer).activateProductions(produced, requests);
    }

    /**
     * Discards a specified LeaderCard from the hand of the current player calling discardLeaders and adds 1 faith
     * @param card LeaderCard to be discarded
     */
    public void discardExtraLeader(LeaderCard card) {
        playerBoards.get(currentPlayer).discardLeader(card);
        playerBoards.get(currentPlayer).addFaith(1);
    }

    /**
     * Plays the selected LeaderCard if the current player meets its resource requirements
     * @param card LeaderCard to be played
     * @return Returns true if the card is played, false otherwise
     */
    public boolean playLeaderCard(LeaderCard card) {
        return playerBoards.get(currentPlayer).playLeaderCard(card);
    }

    /**
     * Method invoked to check if a player has reached a Pope space and handle the eventual vatican report updating
     * player boards and the faith track
     */
    void checkFaith() {
        boolean vatReport = false;
        for(int i = 0; i < playerBoards.size() && !vatReport; i++){
            vatReport =  faithTrack.checkReportActivation(playerBoards.get(i).getFaithProgression());
        }
        if(vatReport) {
            for (PlayerBoard pBoard : playerBoards) {
                boolean playerInVatReport = faithTrack.checkPlayerReportPosition(pBoard.getFaithProgression());
                //TODO: Gestire update PopeProgression PlayerBoard
            }
        }
    }

    /**
     * Method invoked to add faith to the faith counter to every board except for the active one
     * @param quantity Amount of faith to be added
     */
    public abstract void addFaithAll(int quantity);

    /**
     * Method invoked to check if a player has finished the game by reaching the last Pope space or by buying the 7th
     * development card
     * @return Returns true if the game is finished, false otherwise
     */
    boolean checkEndGame() {
        boolean endGame = false;
        //TODO: Aggiungere un metodo che controlli nella FaithBoard se il giocatore ha raggiunto l'ultimo PopeSpace
        for(DevelopmentDeck dDeck : developmentDecks)
            if(dDeck.getDeckLevel() == 3 && dDeck.isEmpty())
                endGame = true;
        return endGame;
    }

    /**
     * Method invoked to calculate the amount of VP earned by each player
     * @return Returns an array containing the results, parallel to the list of player boards
     */
    public int[] calculateTotalVP() {
        int[] playersVP = new int[playerNumber];
        for(int i = 0; i < playerNumber; i++) {
            PlayerBoard playerboard = playerBoards.get(i);
            playersVP[i] = faithTrack.calculateVP(playerboard.getFaithProgression(), playerboard.getPopeProgression()) +
                    playerboard.calculateVP();
        }
        return playersVP;
    }

    /**
     * Method invoked to calculate the final position for each player
     * @return Returns an array containing the positions, parallel to the list of player boards
     */
    public int[] calculateFinalPositions() {
        int[] playersVP = calculateTotalVP();
        int[] playersPositions = new int[playerNumber];
        int currentMax = Integer.MAX_VALUE;
        for(int i = 0; i < playerNumber; i++)
            playersPositions[i] = 0;
        for(int i = 0; i < playerNumber; i++) {
            int tempMax = -1;
            for (int j = 0; j < playerNumber; j++) {
                if (playersVP[j] < currentMax) {
                    playersPositions[j]++;
                    if(playersVP[j] >= tempMax)
                        currentMax = playersVP[j];
                }
            }
        }
        return playersPositions;
    }
}
