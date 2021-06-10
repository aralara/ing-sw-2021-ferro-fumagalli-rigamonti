package it.polimi.ingsw.server.model.games;

import it.polimi.ingsw.utils.exceptions.InvalidColumnException;
import it.polimi.ingsw.utils.exceptions.InvalidRowException;
import it.polimi.ingsw.server.model.FileNames;
import it.polimi.ingsw.server.model.boards.PlayerBoard;
import it.polimi.ingsw.server.model.cards.card.*;
import it.polimi.ingsw.server.model.cards.deck.*;
import it.polimi.ingsw.server.model.faith.FaithTrack;
import it.polimi.ingsw.server.model.market.Market;
import it.polimi.ingsw.server.model.storage.*;
import it.polimi.ingsw.server.view.VirtualView;
import it.polimi.ingsw.utils.Constants;
import it.polimi.ingsw.utils.listeners.*;
import it.polimi.ingsw.utils.listeners.server.*;

import java.io.*;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class Game implements Serializable {

    private List<PlayerBoard> playerBoards;
    private Market market;
    private List<DevelopmentDeck> developmentDecks;
    private FaithTrack faithTrack;
    private boolean finished;


    /**
     * Default constructor for Game
     */
    public Game() { }


    /**
     * Initializes a game by calling initPlayerBoards, randomizeStartingPLayers, initMarket, initDevelopment,
     * initFaithTrack, initLeaders in this order
     * @param players List containing nicknames of the players
     */
    public void initGame(List<String> players) {
        initPlayerBoards(players);
        randomizeStartingPlayer();
        initMarket();
        initDevelopment();
        initFaithTrack();
        initLeaders();
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
     * Loads development cards from their file utilizing CardFactory and splits them into 12 development decks
     */
    private void initDevelopment() {
        this.developmentDecks = new ArrayList<>();
        Deck devCardDeck =
                new Deck(CardFactory.getInstance().loadDevelopmentCardsFromFile(FileNames.DEV_CARD_FILE.value()));
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
     * Loads leader cards from their file utilizing CardFactory and sends 4 random cards to each player
     */
    private void initLeaders() {
        int[] first4 = new int[]{0, 1, 2, 3};   //TODO: Si potrebbe migliorare
        Deck leadCardDeck =
                new Deck(CardFactory.getInstance().loadLeaderCardsFromFile(FileNames.LEADER_CARD_FILE.value()));
        leadCardDeck.shuffle();
        for(PlayerBoard pBoard : playerBoards) {
            // Extracts and assigns 4 cards to the the PlayerBoard
            List<LeaderCard> leadCardsPlayer = leadCardDeck.extract(first4).stream().map(s -> (LeaderCard)s)
                    .collect(Collectors.toList());
            pBoard.getLeaderBoard().setLeaderHand(leadCardsPlayer);
        }
    }

    /**
     * Discards leader cards from the hand of a player
     * @param player Index of the player discarding cards
     * @param leaderCards List of leader cards to discard
     * @param setup If the setup flag is false, for each leader discarded 1 faith is added to their owner
     * @return Returns true if the cards can be discarded, false otherwise
     */
    public boolean discardLeaders(int player, List<LeaderCard> leaderCards, boolean setup) {
        PlayerBoard playerBoard = playerBoards.get(player);
        List<Card> playerHand = playerBoard.getLeaderBoard().getHand().getCards();
        if((setup && playerHand.size() != Constants.INITIAL_LEADER_CARDS.value()) ||                    //Checks if the discardLeaders is called during setup
                (setup && leaderCards.size() != 2) ||                                                   //Checks (assuming setup) if the player discarded 2 cards
                playerHand.stream().allMatch(c -> leaderCards.stream().anyMatch(lc -> lc.equals(c))) || //Checks if the discarded cards are in the player's hand
                !leaderCards.stream().allMatch(new HashSet<>()::add)                                    //Checks if the player wants to discard the same card
        )
            return false;
        for(LeaderCard leaderCard : leaderCards) {
            playerBoard.getLeaderBoard().discardLeaderHand(leaderCard);
            if(!setup) {
                playerBoard.getFaithBoard().addFaith(1);
                checkFaith();
            }
        }
        return true;
    }

    /**
     * Gets a map that contains a list of resources (wildcards and fatih) for each player that needs to be equalized,
     * if the game is single player, it will return an empty map
     * @return Returns a map of lists of resources using the nickname of the player as a key
     */
    public Map<String, List<Resource>> getResourcesToEqualize() {    //TODO: hardcoded resources
        Map<String, List<Resource>> equalizeRes = new HashMap<>();
        List<List<Resource>> resources = new ArrayList<>();
        //Resources for the 1st player
        resources.add(new ArrayList<>());
        //Resources for the 2nd player
        resources.add(Collections.singletonList(new Resource(ResourceType.WILDCARD, 1)));
        //Resources for the 3rd player
        resources.add(Arrays.asList(
                new Resource(ResourceType.WILDCARD, 1),
                new Resource(ResourceType.FAITH, 1)));
        //Resources for the 4th player
        resources.add(Arrays.asList(
                new Resource(ResourceType.WILDCARD, 2),
                new Resource(ResourceType.FAITH, 1)));
        for(int i = 0; i < getPlayerNumber(); i++)
            equalizeRes.put(getPlayerBoards().get(i).getPlayer().getNickname(), resources.get(i));
        return equalizeRes;
    }

    /**
     * Adds resources organized in shelves to the specified player's warehouse
     * @param player Index of the player to add resources to
     * @param shelves List of shelves containing resources to add
     * @param extra List of resources to be discarded and faith to be added
     * @return Returns true if the resources are added correctly, false otherwise
     */
    public boolean addResourcesToWarehouse(int player, List<Shelf> shelves, List<Resource> extra) { //TODO: controllare che il giocatore possa aggiungerle dato lo stato corrente (dopo aver preso dal market o dopo aver equalizzato)
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
     * @return Returns 2 if it's the last round, 3 if the game is ended, 1 otherwise
     */
    public abstract int loadNextTurn();

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
            } catch (InvalidRowException | InvalidColumnException e) {
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
     * @param space The space where card will be added
     * @return Returns true if the card can be added, false otherwise
     */
    public boolean canBuyDevCard(int player, DevelopmentCard card, int space) {
        PlayerBoard playerBoard = playerBoards.get(player);
        List<Resource> cost = Storage.calculateDiscount(card.getCost(), playerBoard.getAbilityDiscounts());
        return playerBoard.getDevelopmentBoard().checkDevCardAddable(card, space) &&
                Storage.checkContainedResources(playerBoards.get(player).createResourceStock(),
                        Storage.mergeResourceList(cost));
    }

    /**
     * If the resources contained in the requests match the actual available resources detained by the player
     * buys the card for said player and places it onto their development board
     * @param player Index of the player to buy for
     * @param card The development card to buy
     * @param space The position where the card will be placed
     * @param requests List of requests containing resource quantity and location
     * @return Returns true if the card is bought, false otherwise
     */
    public boolean buyDevCard(int player, DevelopmentCard card, int space, List<RequestResources> requests) {
        PlayerBoard playerBoard = playerBoards.get(player);
        List<Resource> totalRequests = new ArrayList<>();
        List<Resource> cost = Storage.calculateDiscount(card.getCost(), playerBoard.getAbilityDiscounts());
        requests.forEach(rr -> totalRequests.addAll(rr.getList()));
        if(!playerBoard.isTurnPlayed()) {
            if (canBuyDevCard(player, card, space) &&                                                       //Basic checks
                    Storage.checkContainedResources(Storage.mergeResourceList(cost), totalRequests) &&      //Checks if the resources in the requests can buy the card
                    developmentDecks.stream()
                            .anyMatch(dd -> !dd.getDeck().isEmpty() && dd.getDeck().get(0).equals(card)) && //Checks if the selected card can be taken from the decks
                    playerBoard.buyDevCard(card, space, requests)                                           //Buys development card if the player has enough resources
            ) {
                playerBoard.setTurnPlayed(true);
                return removeDevCard(card.getColor(), card.getLevel());
            }
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
        return Storage.checkContainedResources(playerBoards.get(player).createResourceStock(),
                Storage.mergeResourceList(consumed));
    }

    /**
     * If the resources contained in the requests match the actual available resources detained by the player,
     * activates the productions for said player by adding the produced resources to their
     * @param player Index of the player to activate productions for
     * @param productions List of productions to activate
     * @param requests List of requests containing resource quantity and location for the spent resources
     * @return Returns true if the productions can be activated, false otherwise
     */
    public boolean activateProductions(int player, List<Production> productions, List<RequestResources> requests) {
        PlayerBoard playerBoard = playerBoards.get(player);
        List<Resource> totalRequests = new ArrayList<>();
        List<Resource> consumed = new ArrayList<>(), produced = new ArrayList<>();
        productions.stream().map(Production::getConsumed).forEach(l -> l.forEach(r -> consumed.add(r.makeClone())));
        productions.stream().map(Production::getProduced).forEach(l -> l.forEach(r -> produced.add(r.makeClone())));
        if(!playerBoard.isTurnPlayed()) {
            if(canActivateProductions(player, consumed) &&                                                  //Basic checks
                    Storage.checkContainedResources(Storage.mergeResourceList(consumed), totalRequests) &&  //Checks if the resources in the requests can activate the production
                    //TODO: controllo se le produzioni possono essere state create da questa playerboard
                    playerBoard.canTakeFromStorages(requests)                                               //Activates productions if the player has enough resources
            ) {
                playerBoard.getFaithBoard().takeFaithFromResources(produced);
                checkFaith();
                playerBoard.setTurnPlayed(true);
            }
            return playerBoards.get(player).activateProductions(produced, requests);
        }
        return false;
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
     * Method invoked to check if a player has reached a Pope space and handle the eventual vatican report, updating
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
        //Checks if a players has reached the end of the FaithTrack or has 7 development cards on his board
        for(PlayerBoard pBoard : playerBoards)
            if(!endGame &&
                    (faithTrack.isCompleted(pBoard.getFaithBoard().getFaith()) ||
                    pBoard.getDevelopmentBoard().numberOfCards() >= 7))
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
    private void randomizeStartingPlayer() {
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

        for(int i = 0; i < playerNumber; i++) {
            playersVP[i] = playerBoards.get(i).getPlayer().getTotalVP();
            playersPositions[i] = 0;
        }

        //Iterates over the VP array multiple times, each time considering only the scores that are lower or equal than the
        // current maximum score, and selecting the highest one as the next iteration's maximum score
        int currentMax = Integer.MAX_VALUE;
        for(int i = 0; i < playerNumber; i++) {
            int tempMax = -1;
            for (int j = 0; j < playerNumber; j++) {
                if (playersVP[j] < currentMax) {
                    playersPositions[j]++;
                    if(playersVP[j] >= tempMax)
                        tempMax = playersVP[j];
                }
            }
            currentMax = tempMax;
        }

        for(int i = 0; i < playerNumber; i++)
            playerBoards.get(i).getPlayer().setFinalPosition(playersPositions[i] + 1);
    }

    /**
     * Adds listeners to the Game's components
     * @param virtualViews VirtualViews that intend to listen to the Game
     */
    public void addListeners(List<VirtualView> virtualViews) {
        for(VirtualView view : virtualViews){
            //Development decks
            for(DevelopmentDeck dDeck : developmentDecks)
                dDeck.addListener(Listeners.GAME_DEV_DECK.value(), new DevelopmentDeckChangeListener(view));
            //Market
            market.addListener(Listeners.GAME_MARKET.value(), new MarketChangeListener(view));
            for(PlayerBoard pBoard : playerBoards){
                String nickname = pBoard.getPlayer().getNickname();
                //PlayerBoards
                pBoard.setPlayerNickname(nickname);
                pBoard.addListener(Listeners.BOARD_ABILITY_PROD.value(),
                        new AbilityProductionsChangeListener(view));
                pBoard.addListener(Listeners.BOARD_ABILITY_MARB.value(),
                        new AbilityMarblesChangeListener(view));
                pBoard.addListener(Listeners.BOARD_ABILITY_DISC.value(),
                        new AbilityDiscountsChangeListener(view));
                //DevelopmentBoards
                pBoard.getDevelopmentBoard().setPlayerNickname(nickname);
                pBoard.getDevelopmentBoard().addListener(Listeners.BOARD_DEV_SPACES.value(),
                        new DevelopmentBSpacesChangeListener(view));
                //FaithBoards
                pBoard.getFaithBoard().setPlayerNickname(nickname);
                pBoard.getFaithBoard().addListener(Listeners.BOARD_FAITH_FAITH.value(),
                        new FaithBFaithListener(view));
                pBoard.getFaithBoard().addListener(Listeners.BOARD_FAITH_POPE.value(),
                        new FaithBPopeChangeListener(view));
                //LeaderBoards
                pBoard.getLeaderBoard().setPlayerNickname(nickname);
                pBoard.getLeaderBoard().addListener(Listeners.BOARD_LEADER_BOARD.value(),
                        new LeaderBBoardChangeListener(view));
                pBoard.getLeaderBoard().addListener(Listeners.BOARD_LEADER_HAND.value(),
                        new LeaderBHandChangeListener(view));
                //Strongboxes
                pBoard.getStrongbox().setPlayerNickname(nickname);
                pBoard.getStrongbox().addListener(Listeners.BOARD_STRONGBOX.value(),
                        new StrongboxChangeListener(view));
                //Warehouses
                pBoard.getWarehouse().setPlayerNickname(nickname);
                pBoard.getWarehouse().addListener(Listeners.BOARD_WAREHOUSE.value(),
                        new WarehouseChangeListener(view));
            }
        }
    }

    /**
     * Gets the nickname of the currently playing player
     * @return Returns a string containing the nickname
     */
    public abstract String getPlayingNickname();

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
     * Gets the finished attribute
     * @return Returns finished value
     */
    public boolean isFinished() {
        return finished;
    }

    /**
     * Sets the finished attribute
     * @param finished Attribute to be set
     */
    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    /**
     * Gets the number of players in a game
     * @return Returns the number of player boards
     */
    public int getPlayerNumber() {
        return this.playerBoards.size();
    }
}
