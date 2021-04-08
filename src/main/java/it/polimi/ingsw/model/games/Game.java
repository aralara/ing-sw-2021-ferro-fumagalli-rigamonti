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

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class Game {

    private List<PlayerBoard> playerBoards;
    private Market market;
    private List<DevelopmentDeck> developmentDecks;
    private FaithTrack faithTrack;


    public Game() {
    }

    public Game(String ... players) {
        initGame(players);
    }


    /**
     * Gets the playerBoards attribute
     * @return Returns playerBoards value
     */
    List<PlayerBoard> getPlayerBoards() {
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
     * Gets the faithTrack attribute
     * @return Returns faithTrack value
     */
    FaithTrack getFaithTrack() {
        return this.faithTrack;
    }

    /**
     * Sets the playerBoards attribute
     * @param playerBoards New attribute value
     */
    void setPlayerBoards (List<PlayerBoard> playerBoards) {
        this.playerBoards = playerBoards;
    }

    /**
     * Gets the number of players in a game
     * @return Returns the number of player boards
     */
    public int getPlayerNumber () {
        return this.playerBoards.size();
    }

    /**
     * Gets the nickname of the player at a specified position
     * @param position Position of the player
     * @return Returns a String containing the nickname of the player
     */
    public String getPlayerNameAt(int position){
        return playerBoards.get(position).getPlayer().getNickname();
    }

    /**
     * Initializes a game by calling initMarket, initDevelopment, initFaithTrack, initLeaders in this order
     * @param players Nicknames of the players
     */
    public void initGame(String ... players) {
        initPlayerBoards(players);
        initMarket();
        initDevelopment();
        initFaithTrack();
        initLeaders();
    }

    /**
     * Initializes the player boards from the players' nicknames
     * @param players Nicknames of the players
     */
    void initPlayerBoards(String ... players) {
        this.playerBoards = new ArrayList<>();
        for (String player : players)
            playerBoards.add(new PlayerBoard(this, player));
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
        faithTrack.loadTrack(FileNames.VATICAN_REPORT_FILE.value(), FileNames.FAITH_SPACE_FILE.value());
    }

    /**
     * Loads leader cards from their file utilizing LeaderCardFactory and sends four random cards to each player
     */
    void initLeaders() {
        int[] first4 = new int[]{0, 1, 2, 3};   //TODO: Si potrebbe migliorare
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
            addFaithAll(player, discarded.size());
    }

    /**
     * Method invoked to let the next player play his turn
     */
    public abstract void loadNextTurn();    //TODO: Necessario inserire dei controlli per il funzionamento dell'ultimo giro

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
     * Removes the first DevelopmentCard of a DevelopmentDeck given its color and its level
     * @param color Color of the card to remove
     * @param level Level of the card to remove, if left at 0 it accounts as the lowest level possible
     * @return Returns true if the card can be removed, false if there are no more cards
     */
    public boolean removeDevCard(CardColors color, int level) {
        Optional<DevelopmentDeck> deck;
        Stream<DevelopmentDeck> deckStream = developmentDecks.stream()
                .filter(d -> d.getDeckColor() == color)
                .filter(Predicate.not(DevelopmentDeck::isEmpty));
        if(level == 0)
            deck = deckStream.min(Comparator.comparingInt(DevelopmentDeck::getDeckLevel));
        else
            deck =  deckStream.filter(d -> d.getDeckLevel() == level).findFirst();
        if(deck.isEmpty())
            return false;
        deck.get().removeFirst();
        return true;
    }

    /**
     * Checks if a DevelopmentCard can be bought and placed on the board for the player
     * @param player Index of the player to check
     * @param card The development card to be added
     * @return Returns true if the card can be added, false otherwise
     */
    public boolean canBuyDevCard(int player, DevelopmentCard card) {
        return playerBoards.get(player).canBuyDevCard(card);
    }

    /**
     * If the resources contained in the requests match the actual available resources detained by the player
     * buys the card for said player and places it
     * @param player Index of the player to buy for
     * @param card The development card to buy
     * @param space The position where the card will be placed
     * @param requests List of requests containing resource quantity and location
     * @return Returns true if the card is bought, false otherwise
     */
    public boolean buyDevCard(int player, DevelopmentCard card, int space, List<RequestResources> requests) {
        if(playerBoards.get(player).buyDevCard(card, space, requests))
            return removeDevCard(card.getColor(), card.getLevel());
        return false;
    }

    /**
     * Checks if a list of productions can be activated for the player by checking if they own enough resources
     * @param player Index of the player to check
     * @param consumed The list of resources to be consumed
     * @return Returns true if the productions can be activated, false otherwise
     */
    public boolean canActivateProductions(int player, List<Resource> consumed) {
        return playerBoards.get(player).canActivateProductions(consumed);
    }

    /**
     * If the resources contained in the requests match the actual available resources detained by the player,
     * activates the productions for said player by adding the produced resources to their
     * @param player Index of the player to activate productions for
     * @param produced List of resources to produce
     * @param requests List of requests containing resource quantity and location for the spent resources
     * @return Returns true if the card is bought, false otherwise
     */
    public boolean activateProductions(int player, List<Resource> produced, List<RequestResources> requests) {
        return playerBoards.get(player).activateProductions(produced, requests);
    }

    /**
     * Discards a specified LeaderCard from the hand of the player calling discardLeaders and adds 1 faith
     * @param player Index of the player to discard cards
     * @param card LeaderCard to be discarded
     */
    public void discardExtraLeader(int player, LeaderCard card) {
        playerBoards.get(player).discardLeader(card);
        playerBoards.get(player).addFaith(1);
    }

    /**
     * Plays the selected LeaderCard if the player meets its resource requirements
     * @param player Index of the player to play the Leader card
     * @param card LeaderCard to be played
     * @return Returns true if the card is played, false otherwise
     */
    public boolean playLeaderCard(int player, LeaderCard card) {
        return playerBoards.get(player).playLeaderCard(card);
    }

    /**
     * Method invoked to check if a player has reached a Pope space and handle the eventual vatican report updating
     * player boards and the faith track
     */
    void checkFaith() {
        boolean vatReport = false;
        for(int i = 0; i < playerBoards.size() && !vatReport; i++)
            vatReport =  faithTrack.checkReportActivation(playerBoards.get(i).getFaithProgression());
        if(vatReport) {
            for (PlayerBoard pBoard : playerBoards)
                pBoard.handleReportActivation(faithTrack);
        }
    }

    /**
     * Method invoked to add faith to the faith counter to every board except for the specified one
     * @param playerEx Index of the player to not add faith to
     * @param quantity Amount of faith to be added
     */
    public abstract void addFaithAll(int playerEx, int quantity);

    /**
     * Method invoked to check if a player has finished the game by reaching the last Pope space or by buying the 7th
     * development card
     * @return Returns true if the game is finished, false otherwise
     */
    boolean checkEndGame() {
        boolean endGame = false;
        for(PlayerBoard pBoard : playerBoards)
            if(!endGame && faithTrack.isCompleted(pBoard.getFaithProgression()) && pBoard.getTotalDevelopmentCards() >= 7)
                endGame = true;
        return endGame;
    }

    /**
     * Method invoked to calculate the amount of VP earned by each player
     */
    public void calculateTotalVP() {
        for(PlayerBoard pBoard : playerBoards)
            pBoard.calculateVP(faithTrack);
    }

    /**
     * Method invoked to calculate the final position for each player
     */
    public void calculateFinalPositions() {
        int playerNumber = getPlayerNumber();
        int[] playersVP = new int[playerNumber];
        int[] playersPositions = new int[playerNumber];

        for(int i = 0; i < playerNumber; i++)
            playersVP[i] = playerBoards.get(i).getPlayerVP();

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

        for(int i = 0; i < playerNumber; i++)
            playerBoards.get(i).setPlayerFinalPosition(playersPositions[i]);
    }
}
