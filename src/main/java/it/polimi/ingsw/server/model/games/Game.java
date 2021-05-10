package it.polimi.ingsw.server.model.games;

import it.polimi.ingsw.exceptions.InvalidColumnException;
import it.polimi.ingsw.exceptions.InvalidRowException;
import it.polimi.ingsw.server.model.FileNames;
import it.polimi.ingsw.server.model.boards.PlayerBoard;
import it.polimi.ingsw.server.model.cards.card.*;
import it.polimi.ingsw.server.model.cards.deck.*;
import it.polimi.ingsw.server.model.cards.factory.*;
import it.polimi.ingsw.server.model.faith.FaithTrack;
import it.polimi.ingsw.server.model.market.Market;
import it.polimi.ingsw.server.model.storage.*;
import it.polimi.ingsw.server.view.VirtualView;
import it.polimi.ingsw.utils.listeners.*;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class Game {

    private List<PlayerBoard> playerBoards;
    private Market market;
    private List<DevelopmentDeck> developmentDecks;
    private FaithTrack faithTrack;
    boolean finished;


    public Game() {
    }


    /**
     * Gets the playerBoards attribute
     * @return Returns playerBoards value
     */
    public List<PlayerBoard> getPlayerBoards() {
        return playerBoards;
    }

    /**
     * Gets the market attribute
     * @return Returns market value
     */
    public Market getMarket() {
        return this.market;
    }

    /**
     * Gets the developmentDecks attribute
     * @return Returns developmentDecks value
     */
    public List<DevelopmentDeck> getDevelopmentDecks() {
        return this.developmentDecks;
    }

    /**
     * Gets the faithTrack attribute
     * @return Returns faithTrack value
     */
    public FaithTrack getFaithTrack() {
        return this.faithTrack;
    }


    /**
     * Gets the number of players in a game
     * @return Returns the number of player boards
     */
    public int getPlayerNumber () {
        return this.playerBoards.size();
    }

    /**
     * Gets the index of the player with a specified nickname
     * @param nickname Nickname of the player
     * @return Returns the position of the player
     */
    public int getPlayerIndexOf(String nickname) {
        int index = -1;
        for(int i = 0; i < getPlayerNumber() && index == -1; i++) {
            if (playerBoards.get(i).getPlayer().getNickname().equals(nickname))
                index = i;
        }
        return index;
    }

    /**
     * Initializes a game by calling initMarket, initDevelopment, initFaithTrack, initLeaders in this order and adds
     * listeners to the virtual views
     * @param views Virtual views of the players
     */
    public void initGame(List<VirtualView> views) {
        initPlayerBoards(views.stream().map(VirtualView::getNickname).collect(Collectors.toList()));
        randomizeStartingPlayer();
        initMarket();
        initDevelopment();
        initFaithTrack();
        initLeaders();
        addListeners(views);
        finished = false;
    }

    /**
     * Initializes the player boards from the players' nicknames
     * @param players Nicknames of the players
     */
    private void initPlayerBoards(List<String> players) {
        this.playerBoards = new ArrayList<>();
        for (String player : players)
            playerBoards.add(new PlayerBoard(player));
    }

    /**
     * Initializes the market
     */
    private void initMarket() {
        this.market = new Market();
        market.loadMarket(FileNames.MARKET_FILE.value());
    }

    /**
     * Loads development cards from their file utilizing DevelopmentCardFactory and split them into 12 DevelopmentDeck
     */
    private void initDevelopment() {
        this.developmentDecks = new ArrayList<>();
        DevelopmentCardFactory devCardFactory = new DevelopmentCardFactory();
        Deck devCardDeck = new Deck(devCardFactory.loadCardFromFile(FileNames.DEV_CARD_FILE.value()));
        while(!devCardDeck.isEmpty())
            developmentDecks.add(new DevelopmentDeck(devCardDeck));
    }

    /**
     * Loads faith track information from his file
     */
    private void initFaithTrack() {
        this.faithTrack = new FaithTrack();
        faithTrack.loadTrack(FileNames.VATICAN_REPORT_FILE.value(), FileNames.FAITH_SPACE_FILE.value());
    }

    /**
     * Loads leader cards from their file utilizing LeaderCardFactory and sends four random cards to each player
     */
    private void initLeaders() {
        int[] first4 = new int[]{0, 1, 2, 3};   //TODO: Si potrebbe migliorare
        LeaderCardFactory leadCardFactory = new LeaderCardFactory();
        Deck leadCardDeck = new Deck(leadCardFactory.loadCardFromFile(FileNames.LEADER_CARD_FILE.value()));
        leadCardDeck.shuffle();
        for(PlayerBoard pBoard : playerBoards){
            List<LeaderCard> leadCardsPlayer = leadCardDeck.extract(first4).stream().map(s -> (LeaderCard)s)
                    .collect(Collectors.toList());
            pBoard.getLeaderBoard().setLeaderHand(leadCardsPlayer);
        }
    }

    /**
     * Discards leader cards from the hand of a player
     * @param player Index of the player discarding cards
     * @param leaderCards List of leader cards to discard
     */
    public void discardLeader(int player, List<LeaderCard> leaderCards) {
        for(LeaderCard leaderCard : leaderCards)
            playerBoards.get(player).getLeaderBoard().discardLeaderHand(leaderCard);
    }

    /**
     * Adds resources organized in shelves to the specified player's warehouse
     * @param player Index of the player to add resources to
     * @param shelves List of shelves containing resources to add
     * @param extra List of resources to be discarded and faith to be added
     * @return Returns true if the resources are added correctly, false otherwise
     */
    public boolean addResourcesToWarehouse(int player, List<Shelf> shelves, List<Resource> extra) {
        PlayerBoard playerboard = playerBoards.get(player);
        boolean success = playerboard.getWarehouse().changeConfiguration(shelves);
        if(extra.size() > 0 && success) {
            playerboard.getFaithBoard().takeFaithFromResources(extra);
            addFaithAll(player, Storage.getTotalQuantity(extra));
        }
        checkFaith();
        return success;
    }

    /**
     * Method invoked to let the next player play his turn
     */
    public abstract void loadNextTurn();

    /**
     * Method invoked to take resources from the market
     * @param player Index of the player
     * @param row Row chosen by the player, if the player chose a column it is -1
     * @param column Column chosen by the player, if the player chose a row it is -1
     * @return Returns a list of resources corresponding to the marbles contained in the market
     */
    public List<Resource> getFromMarket(int player, int row, int column) {
        PlayerBoard playerBoard = playerBoards.get(player);
        if(!playerBoard.isTurnPlayed()) {
            playerBoard.setTurnPlayed(true);
            try{
                return market.chooseCoordinates(row, column);
            }
            catch (InvalidRowException | InvalidColumnException e){
                e.printStackTrace();
            }
        }
        return null;
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
        return playerBoards.get(player).getDevelopmentBoard().checkDevCardAddable(card);
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
        PlayerBoard playerBoard = playerBoards.get(player);
        if(!playerBoard.isTurnPlayed())
            if(playerBoard.buyDevCard(card, space, requests)) {
                playerBoard.setTurnPlayed(true);
                return removeDevCard(card.getColor(), card.getLevel());
            }
        return false;
    }

    /**
     * Checks if a list of productions can be activated for the player by checking if they own enough resources
     * @param player Index of the player to check
     * @param consumed The list of resources to be consumed
     * @return Returns true if the productions can be activated, false otherwise
     */
    public boolean canActivateProductions(int player, List<Resource> consumed) {
        return Storage.checkContainedResources(playerBoards.get(player).createResourceStock(),consumed);
    }

    /**
     * If the resources contained in the requests match the actual available resources detained by the player,
     * activates the productions for said player by adding the produced resources to their
     * @param player Index of the player to activate productions for
     * @param produced List of resources to produce
     * @param requests List of requests containing resource quantity and location for the spent resources
     * @return Returns true if the productions can be activated, false otherwise
     */
    public boolean activateProductions(int player, List<Resource> produced, List<RequestResources> requests) {
        PlayerBoard playerBoard = playerBoards.get(player);
        if(!playerBoard.isTurnPlayed()) {
            if(playerBoard.canTakeFromStorages(requests)) {
                playerBoard.getFaithBoard().takeFaithFromResources(produced);
                checkFaith();
                playerBoard.setTurnPlayed(true);
            }
            return playerBoards.get(player).activateProductions(produced, requests);
        }
        return false;   //TODO: eccezione
    }

    /**
     * Discards a specified LeaderCard from the hand of the player calling discardLeaders and adds 1 faith
     * @param player Index of the player to discard cards
     * @param card LeaderCard to be discarded
     */
    public void discardExtraLeader(int player, LeaderCard card) {
        playerBoards.get(player).getLeaderBoard().discardLeaderHand(card);
        playerBoards.get(player).getFaithBoard().addFaith(1);
        checkFaith();
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
            vatReport =  faithTrack.checkReportActivation(playerBoards.get(i).getFaithBoard().getFaith());
        if(vatReport) {
            for (PlayerBoard pBoard : playerBoards)
                pBoard.getFaithBoard().handleReportActivation(faithTrack);
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
            if(!endGame &&
                    faithTrack.isCompleted(pBoard.getFaithBoard().getFaith()) &&
                    pBoard.getDevelopmentBoard().numberOfCards() >= 7)
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
     * Shuffles the players and appoints the first one as the starting player
     */
    private void randomizeStartingPlayer(){
        Collections.shuffle(getPlayerBoards());
        getPlayerBoards().get(0).firstPlayer();
    }

    /**
     * Method invoked to calculate the final position for each player
     */
    public void calculateFinalPositions() {
        int playerNumber = getPlayerNumber();
        int[] playersVP = new int[playerNumber];
        int[] playersPositions = new int[playerNumber];

        for(int i = 0; i < playerNumber; i++)
            playersVP[i] = playerBoards.get(i).getPlayer().getTotalVP();

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
            playerBoards.get(i).getPlayer().setFinalPosition(playersPositions[i]);
    }

    /**
     * Adds listeners to the Game's components
     * @param virtualViews VirtualViews that intend to listen to the Game
     */
    public void addListeners(List<VirtualView> virtualViews) {
        for(VirtualView view : virtualViews){
            for(DevelopmentDeck dDeck : developmentDecks)
                dDeck.addListener(Listeners.GAME_DEV_DECK.value(), new DevelopmentDeckChangeListener(view));
            market.addListener(Listeners.GAME_MARKET.value(), new MarketChangeListener(view));
            for(PlayerBoard pBoard : playerBoards){
                String nickname = pBoard.getPlayer().getNickname();
                pBoard.getDevelopmentBoard().setPlayerNickname(nickname);
                pBoard.getDevelopmentBoard().addListener(Listeners.BOARD_DEV_SPACES.value(),
                        new DevelopmentBSpacesChangeListener(view));
                pBoard.getFaithBoard().setPlayerNickname(nickname);
                pBoard.getFaithBoard().addListener(Listeners.BOARD_FAITH_FAITH.value(),
                        new FaithBFaithListener(view));
                pBoard.getFaithBoard().addListener(Listeners.BOARD_FAITH_POPE.value(),
                        new FaithBPopeChangeListener(view));
                pBoard.getLeaderBoard().setPlayerNickname(nickname);
                pBoard.getLeaderBoard().addListener(Listeners.BOARD_LEADER_BOARD.value(),
                        new LeaderBBoardChangeListener(view));
                pBoard.getLeaderBoard().addListener(Listeners.BOARD_LEADER_HAND.value(),
                        new LeaderBHandChangeListener(view));
                pBoard.getStrongbox().setPlayerNickname(nickname);
                pBoard.getStrongbox().addListener(Listeners.BOARD_STRONGBOX.value(),
                        new StrongboxChangeListener(view));
                pBoard.getWarehouse().setPlayerNickname(nickname);
                pBoard.getWarehouse().addListener(Listeners.BOARD_WAREHOUSE.value(),
                        new WarehouseChangeListener(view));
            }
        }
    }
}
