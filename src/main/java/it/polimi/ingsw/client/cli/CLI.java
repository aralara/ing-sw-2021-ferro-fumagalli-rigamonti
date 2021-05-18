package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.client.AckMessageReader;
import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.client.UpdateMessageReader;
import it.polimi.ingsw.client.structures.*;
import it.polimi.ingsw.exceptions.NotExistingNicknameException;
import it.polimi.ingsw.server.Server;
import it.polimi.ingsw.server.model.boards.Player;
import it.polimi.ingsw.server.model.cards.card.*;
import it.polimi.ingsw.server.model.storage.*;
import it.polimi.ingsw.utils.messages.client.*;
import it.polimi.ingsw.utils.messages.server.action.ServerActionMessage;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

public class CLI extends ClientController {

    boolean waiting;
    private final GraphicalCLI graphicalCLI;


    public CLI() {
        super();
        waiting = false;
        graphicalCLI = new GraphicalCLI();
    }


    public void setup() {
        connect();
        new Thread(getMessageHandler()).start();
        askNickname();
    }

    @Override
    public void connect() { //TODO: inserimento porta
        boolean success;
        do {
            graphicalCLI.printString("Insert the IP address of server: ");
            String ip = graphicalCLI.getNextLine();
            graphicalCLI.printlnString("Connecting...");
            success = getMessageHandler().connect(ip, Server.SOCKET_PORT);
            if (success)
                graphicalCLI.printlnString("Connected");
            else
                graphicalCLI.printlnString("Server unreachable");
        } while(!success);
    }

    @Override
    public void run() {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));   //TODO: da sistemare
        LinkedBlockingQueue<ServerActionMessage> actionQueue = getMessageHandler().getActionQueue();

        new Thread(new UpdateMessageReader(this, getMessageHandler().getUpdateQueue())).start();
        new Thread(new AckMessageReader(this, getMessageHandler().getAckQueue())).start();

        boolean displayMenu = true;

        while(true) {   //TODO: temp
            try {
                if(waiting) {
                    if (displayMenu) {
                        graphicalCLI.printlnString("Press ENTER to display action menu");
                        displayMenu = false;
                    }
                    if (br.ready()) {
                        turnMenu();
                        displayMenu = true;
                    }
                }
                if (actionQueue.size() > 0) {
                    actionQueue.poll().doAction(this);
                    displayMenu = true;
                }
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void askNickname() {
        graphicalCLI.printString("Insert your nickname: ");
        setNickname(graphicalCLI.getNext());
        getMessageHandler().sendMessage(new ConnectionMessage(getNickname()));
    }

    @Override
    public void askNewLobby(int lobbySize, int waitingPlayers) {
        if (lobbySize == waitingPlayers){
            int size;
            graphicalCLI.printlnString("There isn't any player waiting for a match!");
            do {
                graphicalCLI.printString("Insert the number of players that will play the game " +
                        "(value inserted must between 1 and 4): ");
                size = graphicalCLI.getNextInt();
            }while(size <= 0 || size >= 5);
            setNumberOfPlayers(size);
            getMessageHandler().sendMessage(new NewLobbyMessage(size));
        }
        else {
            graphicalCLI.printlnString("There is already a " + lobbySize + " player lobby waiting for "
                    + (lobbySize - waitingPlayers) + " more player(s)");
            setNumberOfPlayers(lobbySize);
        }
    }

    @Override
    public void notifyNewPlayer(String nickname) {
        if(getNickname().equals(nickname))
            graphicalCLI.printlnString("You have been added to the game!");
        else
            graphicalCLI.printlnString("The player " + nickname + " has joined the game!");

    }

    @Override
    @SuppressWarnings("unchecked")
    public void askLeaderDiscard() {
        try {
            PlayerBoardView playerBoard = getLocalPlayerBoard();
            List<LeaderCard> leaderHand = new ArrayList<>(
                    (List<LeaderCard>)(List<? extends Card>)playerBoard.getLeaderBoard().getHand().getCards());
            List<LeaderCard> selected = new ArrayList<>();

            graphicalCLI.printlnString("\nYou have to discard 2 leader cards from your hand:");
            for(int i = 0; i < 2; i++) {
                LeaderCard selection = graphicalCLI.objectOptionSelector(leaderHand, graphicalCLI::printLeaderCard);
                selected.add(selection);
                leaderHand.remove(selection);
            }

            getMessageHandler().sendMessage(new LeaderCardDiscardMessage(selected));
        }catch (NotExistingNicknameException e){
            e.printStackTrace();
        }
    }

    @Override
    public void askResourceEqualize(List<Resource> resources) { //TODO: prima della scelta delle risorse mostrare i dev decks e il market per lasciare una scelta sensata (dovremo cambiare ordine pacchetti, mandando prima il setup delle cose comuni)
        List<Resource> newResources = new ArrayList<>();
        graphicalCLI.printlnString("");
        for(Resource resource : resources) {
            if (resource.getResourceType() == ResourceType.FAITH) {
                newResources.add(new Resource(ResourceType.FAITH, resource.getQuantity()));
            }
            else if (resource.getResourceType() == ResourceType.WILDCARD) {
                for (int num = 0; num < resource.getQuantity(); num++) {
                    ResourceType resType = graphicalCLI.objectOptionSelector(
                            ResourceType.getRealValues(),
                            rt -> graphicalCLI.printlnString(rt.toString()));
                    newResources.add(new Resource(resType, 1));
                }
            }
        }
        if(newResources.size() > 0) {
            setResourcesToPut(new ArrayList<>(newResources));
            graphicalCLI.printlnString("Now place the resources on the shelves:");
            placeResourcesOnShelves(newResources);
        }
    }

    @Override
    public void notifyStartTurn(String nickname) {
        if (nickname.equals(getNickname())) {
            graphicalCLI.printlnString("\nNOW IT'S YOUR TURN!\n");
            setMainActionPlayed(false);
            setPlayerTurn(true);
        }
        else {
            graphicalCLI.printlnString("\nNOW IT'S " + nickname.toUpperCase() + "'S TURN!\n");
            setPlayerTurn(false);
        }
        turnMenu();
        waiting = true;
    }

    @Override
    public void addMarketResources(List<Resource> resources, List<ResourceType> availableAbilities) {
        List<Resource> plainResources = resources.stream()
                .filter(r -> r.getResourceType() != ResourceType.WILDCARD).collect(Collectors.toList());
        int     index,
                marblesLeft = (int) resources.stream()
                        .filter(r -> r.getResourceType() == ResourceType.WILDCARD).count();

        while (marblesLeft > 0 && availableAbilities.size() > 0) {

            graphicalCLI.printlnString("You can choose a resource from the following types ( "
                    + marblesLeft + " wildcards left ):");

            graphicalCLI.printNumberedList(availableAbilities, rt -> graphicalCLI.printString(rt.name()));

            do {
                graphicalCLI.printString("Please choose a valid resource type for the wildcard:");
                index = graphicalCLI.getNextInt() - 1;
            } while(0 > index || index > availableAbilities.size());

            plainResources.add(new Resource(availableAbilities.get(index), 1));

            availableAbilities.remove(index);
            marblesLeft--;
        }
        placeResourcesOnShelves(plainResources);
        waiting = true;
    }

    @Override
    public void notifyLorenzoCard(LorenzoCard lorenzoCard) {
        graphicalCLI.printlnString("\nNOW IT'S LORENZO'S TURN\n");
        graphicalCLI.printlnString("Lorenzo pulls a card from his deck");
        graphicalCLI.printLorenzoCard(lorenzoCard);
        waiting = false;
    }

    @Override
    public void notifyLastRound() {
        graphicalCLI.printlnString("Last round before the game ends!");
    }

    @Override
    public void notifyEndGame(List<Player> players) {
        graphicalCLI.printlnString("THE GAME HAS ENDED!");
        graphicalCLI.printlnString("Scoreboard:");
        for(Player player : players.stream()
                .sorted(Comparator.comparingInt(Player::getFinalPosition)).collect(Collectors.toList()))
            graphicalCLI.printlnString(player.getFinalPosition()+1 + ": " + player.getNickname() + " with " +
                    + player.getTotalVP() + " VP");
    }

    public void turnMenu() {
        int action;
        List<LeaderCard> leaderCards;
        graphicalCLI.printActions(isPlayerTurn());
        action = graphicalCLI.getNextInt();
        while (action < 1 || action > (isPlayerTurn() ? 10 : 4)){
            graphicalCLI.printString("Invalid choice, please try again: ");
            action = graphicalCLI.getNextInt();
        }
        if(!isPlayerTurn())
            action+=5;
        switch (action) {
            case 1:
                if (!isMainActionPlayed())
                    selectMarket();
                else {
                    graphicalCLI.printlnString("You can't play this action on your turn anymore");
                }
                break;
            case 2:
                if (!isMainActionPlayed()) {
                    selectDevDecks();
                    if (isMainActionPlayed()) { //TODO: da spostare in un doAction (togliendo if e controllando ack)
                        List<RequestResources> requestResources = chooseStorages(getCardToBuy().getCost());
                        getMessageHandler().sendMessage(new RequestResourcesDevMessage(getCardToBuy(), getSpaceToPlace(), requestResources));
                    }
                }
                else {
                    graphicalCLI.printlnString("You can't play this action on your turn anymore");
                }
                break;
            case 3:
                if (!isMainActionPlayed()) {
                    selectProductions();
                    if (isMainActionPlayed()) { //TODO: da spostare in un doAction (togliendo if e controllando ack)
                        List<Resource> resources = new ArrayList<>();
                        for(Production p : getProductionsToActivate()){
                            resources.addAll(p.getConsumed());
                        }
                        List<RequestResources> requestResources = chooseStorages(resources);
                        getMessageHandler().sendMessage(new RequestResourcesProdMessage(getProductionsToActivate(), requestResources));
                    }
                }
                else {
                    graphicalCLI.printlnString("You can't play this action on your turn anymore");
                }
                break;
            case 4:
                leaderCards = chooseLeaderCard();
                if(leaderCards.size() > 0) {
                    getMessageHandler().sendMessage(new LeaderCardPlayMessage(leaderCards));
                }
                break;
            case 5:
                leaderCards = chooseLeaderCard();
                if(leaderCards.size() > 0) {
                    getMessageHandler().sendMessage(new LeaderCardDiscardMessage(leaderCards));
                }
                break;
            case 6:
                graphicalCLI.printMarket(getMarket()); graphicalCLI.printlnString("");
                graphicalCLI.printDevelopmentDeckTop(getDevelopmentDecks()); graphicalCLI.printlnString("");
                break;
            case 7:
                showBoard();
                break;
            case 8:
                showOpponents();
                break;
            case 9:
                if(isWarehouseEmpty()){
                    graphicalCLI.printlnString("You have no resources to rearrange");
                } else {
                    getMessageHandler().sendMessage(new ShelvesConfigurationMessage(
                            rearrangeWarehouse(), new ArrayList<>()));
                }
                //TODO: messaggio per confermare config?
                break;
            case 10:
                if (isMainActionPlayed()) {
                    setProductionsToActivate(new ArrayList<>());
                    setPlayerTurn(false);
                    getMessageHandler().sendMessage(new EndTurnMessage(getNickname()));
                    waiting = false;
                }
                else graphicalCLI.printlnString("You haven't played any main action yet!");
                break;
            default:
                break;
        }
    }

    private void selectMarket() {
        graphicalCLI.printMarket(getMarket());
        if(graphicalCLI.askGoBack())
            return;
        graphicalCLI.printlnString("Where do you want to place the marble?\n" +
                "Choose R (row) or C (column) followed by a number: ");
        boolean valid;
        do {
            int row = -1, column = -1;
            valid = true;

            String choice = graphicalCLI.getNext();
            if(choice.matches("[RCrc][0-4]")) {
                String rowCol = choice.substring(0, 1).toUpperCase();
                int number = Integer.parseInt(choice.substring(1, 2));

                if (rowCol.equals("R") && 0 < number && number <= 3)
                    row = number - 1;
                else if (rowCol.equals("C") && 0 < number && number <= 4)
                    column = number - 1;
                else
                    valid = false;
            }
            else
                valid = false;

            if(valid)
                getMessageHandler().sendMessage(new SelectMarketMessage(row, column));
            else
                graphicalCLI.printString("Invalid choice, please try again: ");
        } while(!valid);
        setMainActionPlayed(true);
    }

    private void selectDevDecks() {
        int space;

        graphicalCLI.printDevelopmentDeckTop(getDevelopmentDecks());
        if(graphicalCLI.askGoBack())
            return;

        DevelopmentCard developmentCard = chooseCardFromDecks();

        graphicalCLI.printString("Which space do you want to put the card on? ");
        space = graphicalCLI.getNextInt() - 1;
        while (space < 0 || space >= 3){
            graphicalCLI.printString("Invalid choice, please try again: ");
            space = graphicalCLI.getNextInt() - 1;
        }
        setSpaceToPlace(space);

        setCardToBuy(new DevelopmentCard(developmentCard.getID(),developmentCard.getVP(),developmentCard.getColor(),
                developmentCard.getLevel(),developmentCard.getProduction(),developmentCard.getCost()));
        getMessageHandler().sendMessage(new CanBuyDevelopmentCardMessage(getCardToBuy(), getSpaceToPlace()));
        setMainActionPlayed(true); //TODO: mettere a false nel nack se la carta selezionata non può essere acquistata
    }

    private void selectProductions() {
        try {
            List<Production> productions = new ArrayList<>();
            setProductionsToActivate(new ArrayList<>());

            productions.add(getLocalPlayerBoard().getBasicProduction());
            getLocalPlayerBoard().getDevelopmentBoard().getSpaces().stream().filter(d -> !d.isEmpty())
                    .forEach(d -> productions.add(((DevelopmentCard) d.get(0)).getProduction()));
            productions.addAll(getLocalPlayerBoard().getActiveAbilityProductions());

            graphicalCLI.printlnString("Available productions:");
            graphicalCLI.printNumberedList(productions, graphicalCLI::printProduction);
            boolean endChoice = false;
            if(productions.size() > 0) {
                do {
                    graphicalCLI.printString("Choose a production you want to activate by entering its number: ");
                    int index = graphicalCLI.getNextInt() - 1;
                    while (index < 0 || index >= productions.size()){
                        graphicalCLI.printString("Invalid choice, please try again: ");
                        index = graphicalCLI.getNextInt() - 1;
                    }

                    getProductionsToActivate().add(productions.remove(index));

                    if(productions.size() <= 0)
                        endChoice = true;
                    graphicalCLI.printString("Do you want to activate another production? ");
                    if (!endChoice && !graphicalCLI.isAnswerYes())
                        endChoice = true;
                } while (!endChoice);
                resolveProductionWildcards();
                if(getProductionsToActivate().size() > 0)
                    getMessageHandler().sendMessage(new CanActivateProductionsMessage(getProductionsToActivate()));
            }
            else
                graphicalCLI.printlnString("No productions");
        } catch(NotExistingNicknameException e) {
            e.printStackTrace();
        }
        setMainActionPlayed(getProductionsToActivate().size() > 0);
    }

    @SuppressWarnings("unchecked")
    private List<LeaderCard> chooseLeaderCard(){ //TODO: mettere nella graphicalCLI
        try {
            List<LeaderCard> hand = (List<LeaderCard>)(List<? extends Card>)
                    getLocalPlayerBoard().getLeaderBoard().getHand().getCards();
            List<LeaderCard> chosenCard = new ArrayList<>();
            if(hand.size() > 0){
                graphicalCLI.printNumberedList(hand, graphicalCLI::printLeaderCard);
                graphicalCLI.printString("Choose a leader card by selecting the corresponding number: ");

                int index = graphicalCLI.getNextInt() - 1;
                while(index<0 || index>=hand.size()){
                    graphicalCLI.printString("Invalid choice, please try again: ");
                    index = graphicalCLI.getNextInt() - 1;
                }
                chosenCard.add(hand.get(index));
            }
            else{
                graphicalCLI.printlnString("You don't have any leader card in your hand!");
            }
            return chosenCard;
        }catch(NotExistingNicknameException e){
            e.printStackTrace();
        }
        return null; //TODO: fa schifo tornare null
    }

    private List<Shelf> rearrangeWarehouse(){
        try {
            WarehouseView warehouse = getLocalPlayerBoard().getWarehouse();

            List<Shelf> shelves = getShelvesWarehouseCopy(warehouse.getShelves());
            List<Shelf> temporaryWarehouseShelves;
            List<Resource> resources = new ArrayList<>();
            Shelf selectedShelf;
            Resource resourceToPlace;
            int level;

            for (Shelf shelf : shelves) {
                resources.add(new Resource(shelf.getResourceType(), shelf.getResources().getQuantity()));
                shelf.getResources().setQuantity(0);
                if (!shelf.isLeader()) {
                    shelf.getResources().setResourceType(ResourceType.WILDCARD);
                    shelf.setResourceType(ResourceType.WILDCARD);
                }
            }
            temporaryWarehouseShelves = getShelvesWarehouseCopy(shelves);

            List<Resource> toPlace = getResourcesOneByOne(resources);
            boolean firstTurn = true;
            while (!toPlace.isEmpty()){
                resourceToPlace = toPlace.get(0);
                graphicalCLI.printWarehouseConfiguration(new WarehouseView(shelves), true);
                graphicalCLI.printString("Resources to place: ");
                graphicalCLI.printGraphicalResources(toPlace);

                level=graphicalCLI.askWhichShelf(resourceToPlace, shelves.size(), !firstTurn, false);
                if(firstTurn) firstTurn = !firstTurn;

                if(level>0) {
                    selectedShelf = shelves.get(level - 1);
                    if (selectedShelf.getResourceType().equals(ResourceType.WILDCARD)) //empty shelf
                        emptyShelfManagement(shelves, toPlace, selectedShelf, resourceToPlace);
                    else if (selectedShelf.getResourceType().equals(resourceToPlace.getResourceType())) //shelf with the same resource type
                        sameResTypeShelfManagement(shelves, toPlace, selectedShelf, resourceToPlace);
                    else if(!selectedShelf.isLeader()) //shelf with different resource type
                        differentResTypeShelfManagement(shelves, toPlace, selectedShelf, resourceToPlace);
                    else graphicalCLI.printlnString("You can't place this resource here");
                }
                else if(level<0) {
                    restoreConfiguration(new WarehouseView(temporaryWarehouseShelves), shelves, resources, toPlace,
                            null, false);
                }
            }
            return shelves;
        } catch (NotExistingNicknameException e){
            e.printStackTrace();
        }
        return null; //TODO: :(
    }

    private void showOpponents(){
        if(getNumberOfPlayers()>1) {
            for (PlayerBoardView playerBoardView : getPlayerBoards()) {
                if (!playerBoardView.getNickname().equals(getNickname())) {
                    graphicalCLI.printPlayer(playerBoardView, getFaithTrack());
                }
            }
        } else graphicalCLI.printLorenzo(getLorenzoFaith(), getFaithTrack());
    }

    private void showBoard(){
        try{
            graphicalCLI.printPlayer(getLocalPlayerBoard(), getFaithTrack());
        } catch (NotExistingNicknameException e){
            e.printStackTrace();
        }
    }

    public void tryToPlaceShelves() { //TODO: implementare con ack (x altri ack)
        graphicalCLI.printlnString("The selected configuration is invalid\n" +
                "Please, try again to place on the shelves:");
        placeResourcesOnShelves(getResourcesToPut());
    }

    private void tryAgainToBuyCard() {
        graphicalCLI.printlnString("The selected configuration is invalid");
        if(graphicalCLI.askGoBack())
            turnMenu();
        else { }
    }

    private void tryAgainToActivateProduction() {
        graphicalCLI.printlnString("The selected configuration is invalid");
        if(graphicalCLI.askGoBack())
            turnMenu();
        else { }
    }

    public void placeResourcesOnShelves(List<Resource> resources){
        List<Shelf> shelves;
        List<Resource> toDiscard = new ArrayList<>();
        Shelf selectedShelf;
        Resource resourceToPlace;
        boolean rearranged = false, freeSlots = false;
        int level;

        try{
            List<Resource> toPlace = getResourcesOneByOne(resources);
            moveFaith(toDiscard, toPlace);

            WarehouseView warehouse = getLocalPlayerBoard().getWarehouse();
            shelves = getShelvesWarehouseCopy(warehouse.getShelves());
            graphicalCLI.printWarehouseConfiguration(warehouse, true);
            if(!toPlace.isEmpty()) {
                graphicalCLI.printString("Resources to place: ");
                graphicalCLI.printGraphicalResources(toPlace);

                if(getLocalPlayerBoard().getWarehouse().getShelves().size()<3)
                    freeSlots = true;
                else {
                    for (Shelf shelf : getLocalPlayerBoard().getWarehouse().getShelves()) {
                        if (shelf.getResourceType().equals(ResourceType.WILDCARD) ||
                                shelf.getLevel() > shelf.getResources().getQuantity())
                            freeSlots = true;
                    }
                }

                if (freeSlots) { //it's possible to place resources
                    if (!isShelvesEmpty(shelves)) { //before starting move resources
                        graphicalCLI.printString("Do you want to rearrange the warehouse? ");
                        rearranged = graphicalCLI.isAnswerYes();
                        if(rearranged){
                            shelves = getShelvesWarehouseCopy(rearrangeWarehouse());
                        }
                    }

                    while (!toPlace.isEmpty()) {
                        if (rearranged) {
                            graphicalCLI.printWarehouseConfiguration(new WarehouseView(shelves),true);
                            graphicalCLI.printString("Resources to place: ");
                            graphicalCLI.printGraphicalResources(toPlace);
                        }
                        resourceToPlace = toPlace.get(0);
                        level = graphicalCLI.askWhichShelf(resourceToPlace, shelves.size(), rearranged, true);
                        rearranged = true;

                        if (level > 0) {
                            selectedShelf = shelves.get(level - 1);
                            if (selectedShelf.getResourceType().equals(ResourceType.WILDCARD)) //empty shelf
                                emptyShelfManagement(shelves, toPlace, selectedShelf, resourceToPlace);
                            else if (selectedShelf.getResourceType().equals(resourceToPlace.getResourceType())) //shelf with the same resource type
                                sameResTypeShelfManagement(shelves, toPlace, selectedShelf, resourceToPlace);
                            else if (!selectedShelf.isLeader()) //shelf with different resource type
                                differentResTypeShelfManagement(shelves, toPlace, selectedShelf, resourceToPlace);
                            else graphicalCLI.printlnString("You can't place this resource here");
                        } else if (level == 0) {
                            toDiscard.add(new Resource(resourceToPlace.getResourceType(), resourceToPlace.getQuantity()));
                            graphicalCLI.printlnString("Resource discarded");
                            toPlace.remove(0);
                        } else if (level < 0) {
                            restoreConfiguration(warehouse, shelves, resources, toPlace, toDiscard, true);
                            rearranged = false;
                            graphicalCLI.printWarehouseConfiguration(new WarehouseView(shelves),true);
                            graphicalCLI.printString("Resources to place: ");
                            graphicalCLI.printGraphicalResources(toPlace);
                        }

                        if (toPlace.isEmpty() && !isDiscardedResCorrect(resources, toDiscard)) {
                            graphicalCLI.printlnString("You're trying to discard resources already stored in the" +
                                    " warehouse!\nThe warehouse will be restored and you'll be asked to place all the" +
                                    " resources again...");
                            restoreConfiguration(warehouse, shelves, resources, toPlace, toDiscard, true);
                            rearranged = false;
                            graphicalCLI.printWarehouseConfiguration(new WarehouseView(shelves),true);
                            graphicalCLI.printString("Resources to place: ");
                            graphicalCLI.printGraphicalResources(toPlace);
                        }
                    }
                } else {
                    graphicalCLI.printlnString("There are no available slots, all the resources will be discarded");
                    toDiscard.addAll(toPlace);
                }
            } else graphicalCLI.printlnString("There are no resources to place");
            getMessageHandler().sendMessage(new ShelvesConfigurationMessage(shelves, toDiscard));
        }catch (NotExistingNicknameException e){
            e.printStackTrace();
        }
    }

    public void moveFaith(List<Resource> dest, List<Resource> src){ //TODO: refactor con streams
        for(int i=0; i<src.size(); i++)
            if(src.get(i).getResourceType().equals(ResourceType.FAITH)){
                dest.add(src.get(i));
                src.remove(i);
                break;
            }
    }

    private boolean checkFreeSlotInWarehouse() {    //TODO: accorpare
        try {
            if(getLocalPlayerBoard().getWarehouse().getShelves().size()<3)
                return true;
            for (Shelf shelf : getLocalPlayerBoard().getWarehouse().getShelves()) {
                if (shelf.getResourceType().equals(ResourceType.WILDCARD) ||
                        shelf.getLevel() > shelf.getResources().getQuantity())
                    return true;
            }
        } catch (NotExistingNicknameException e){
            e.printStackTrace();
        }
        return false;
    }

    private boolean isShelvesEmpty(List<Shelf> shelves){    //TODO: mettere metodi specifici nelle loro classi
        for (Shelf shelf : shelves)
            if(shelf.getResources().getQuantity()>0)
                return false;
        return true;
    }

    private boolean isWarehouseEmpty(){
        try {
            return isShelvesEmpty(getLocalPlayerBoard().getWarehouse().getShelves());
        } catch (NotExistingNicknameException e){
            e.printStackTrace();
        }
        return false;
    }

    private List<Shelf> getShelvesWarehouseCopy(List<Shelf> warehouse) {
        List<Shelf> shelves = new ArrayList<>();
        for(Shelf shelf : warehouse)
            shelves.add(new Shelf(shelf.getResourceType(), shelf.getResources(),
                    shelf.getLevel(), shelf.isLeader()));
        return shelves;
    }

    private List<Resource> getResourcesOneByOne(List<Resource> resources){
        List<Resource> resourcesOneByOne = new ArrayList<>();
        for(Resource resource : resources){
            for(int i=0; i<resource.getQuantity(); i++){
                resourcesOneByOne.add(new Resource(resource.getResourceType(),1));
            }
        }
        return resourcesOneByOne;
    }

    private void emptyShelfManagement(List<Shelf> shelves, List<Resource> toPlace,
                                      Shelf selectedShelf, Resource resourceToPlace) {
        if(isResourceTypeUnique(shelves,resourceToPlace.getResourceType())) { //there are no shelves with the same resource type
            placeResource(selectedShelf, resourceToPlace);
            toPlace.remove(0);
        }
        else {//there are shelves with the same resource type
            graphicalCLI.printlnString("There's already another shelf with the same resource type");
            if(isShelfRearrangeable(shelves, resourceToPlace)) { //it's possible to rearrange shelves
                graphicalCLI.printString("If you want to place it here anyway, insert YES and " +
                        "then you'll place again the removed ones from the other shelf: ");
                if(graphicalCLI.isAnswerYes()) {
                    Shelf otherShelf = getShelfWithSameResource(shelves, resourceToPlace.getResourceType());
                    for (int i = 0; i < otherShelf.getResources().getQuantity(); i++) {
                        toPlace.add(1, new Resource(otherShelf.getResources().getResourceType(), 1));
                    }
                    resetShelf(otherShelf);
                    placeResource(selectedShelf, resourceToPlace);
                    toPlace.remove(0);
                }
            }
        }
    }

    private void sameResTypeShelfManagement(List<Shelf> shelves, List<Resource> toPlace,
                                            Shelf selectedShelf, Resource resourceToPlace){
        if (selectedShelf.getResources().getQuantity() <= selectedShelf.getLevel() - 1) { //shelf not completely full
            placeResource(selectedShelf, resourceToPlace);
            toPlace.remove(0);
        }
        else { //shelf completely full
            graphicalCLI.printlnString("The selected shelf is already full");
            if(isShelfRearrangeable(shelves, resourceToPlace)){
                graphicalCLI.printString("If you want to remove the resources from this" +
                        " shelf to place them again on another one, insert YES: ");
                if(graphicalCLI.isAnswerYes()){
                    for(int i=0; i<selectedShelf.getResources().getQuantity(); i++){
                        toPlace.add(1, new Resource(selectedShelf.getResourceType(), 1));
                    }
                    resetShelf(selectedShelf);
                }
            }
        }
    }

    private void differentResTypeShelfManagement(List<Shelf> shelves, List<Resource> toPlace,
                                                 Shelf selectedShelf, Resource resourceToPlace) {
        graphicalCLI.printString("This shelf contains a different resource type\nIf you want" +
                " to place it here anyway, insert YES and then you'll place again the removed ones: ");
        if(graphicalCLI.isAnswerYes()) {
            if (isResourceTypeUnique(shelves, resourceToPlace.getResourceType())) { //there are no shelves with the same resource type
                for (int i = 0; i < selectedShelf.getResources().getQuantity(); i++) {
                    toPlace.add(1, new Resource(selectedShelf.getResourceType(), 1));
                }
                placeResource(selectedShelf, resourceToPlace);
                toPlace.remove(0);
            }
            else {//there are shelves with the same resource type
                graphicalCLI.printlnString("There's already another shelf with the same resource type");
                if (isShelfRearrangeable(shelves, resourceToPlace)) { //it's possible to rearrange shelves
                    graphicalCLI.printString("If you want to place it here anyway, insert YES and " +
                            "then you'll place again the removed ones: ");
                    if (graphicalCLI.isAnswerYes()) {
                        Shelf otherShelf = getShelfWithSameResource(shelves, resourceToPlace.getResourceType());
                        for (int i = 0; i < otherShelf.getResources().getQuantity(); i++) {
                            toPlace.add(1, new Resource(otherShelf.getResources().getResourceType(), 1));
                        }
                        resetShelf(otherShelf);
                        for (int i = 0; i < selectedShelf.getResources().getQuantity(); i++) {
                            toPlace.add(1, new Resource(selectedShelf.getResourceType(), 1));
                        }
                        placeResource(selectedShelf, resourceToPlace);
                        toPlace.remove(0);
                    }
                }
            }
        }
    }

    private boolean isResourceTypeUnique(List<Shelf> shelves, ResourceType resourceType) {
        return shelves.stream().noneMatch(shelf -> !shelf.isLeader() && shelf.getResourceType().equals(resourceType));
    }

    private void placeResource(Shelf shelf, Resource resource){
        if(shelf.getResourceType().equals(resource.getResourceType())){ //shelf with the same resource type
            shelf.getResources().setQuantity(shelf.getResources().getQuantity() +
                    resource.getQuantity());
        }
        else { //empty shelf or with a different resource type
            shelf.setResourceType(resource.getResourceType());
            shelf.getResources().setResourceType(resource.getResourceType());
            shelf.getResources().setQuantity(resource.getQuantity());
        }
    }

    private Shelf getShelfWithSameResource(List<Shelf> shelves, ResourceType resourceType){
        for(Shelf shelf : shelves){
            if(!shelf.isLeader() && shelf.getResourceType().equals(resourceType))
                return shelf;
        }
        return null; //TODO: brutto?
    }

    private boolean isShelfRearrangeable(List<Shelf> shelves, Resource resource){
        Shelf shelfWithResources = getShelfWithSameResource(shelves, resource.getResourceType());
        int totalLeaderShelves = 2*(int)(shelves.stream().filter(shelf -> shelf.isLeader() && shelf.getResourceType()
                    .equals(resource.getResourceType())).count());
        return shelfWithResources.getResources().getQuantity()+resource.getQuantity() <= 3 + totalLeaderShelves;
    }

    private void resetShelf(Shelf shelf) {
        if(!shelf.isLeader()) {
            shelf.setResourceType(ResourceType.WILDCARD);
            shelf.getResources().setResourceType(ResourceType.WILDCARD);
        }
        shelf.getResources().setQuantity(0);
    }

    private void restoreConfiguration(WarehouseView warehouse, List<Shelf> shelves, List<Resource> resources,
                                      List<Resource> toPlace, List<Resource> toDiscard, boolean canDiscard){
        shelves.clear();
        shelves.addAll(getShelvesWarehouseCopy(warehouse.getShelves()));
        toPlace.clear();
        toPlace.addAll(getResourcesOneByOne(resources));
        if(canDiscard) {
            toDiscard.clear();
            moveFaith(toDiscard, toPlace);
        }
        graphicalCLI.printlnString("Warehouse restored");
    }

    /**
     *
     * @param resources resources to be placed
     * @param toDiscard resources to be discarded
     * @return
     */
    private boolean isDiscardedResCorrect(List<Resource> resources, List<Resource> toDiscard){
        Storage.aggregateResources(resources);
        Storage.aggregateResources(toDiscard);
        return Storage.checkContainedResources(resources,toDiscard);
    }

    private DevelopmentCard chooseCardFromDecks() {
        boolean valid;
        String choice;
        int level;
        DevelopmentCard developmentCard = new DevelopmentCard();
        graphicalCLI.printString("Which card do you want to buy?\nChoose B (blue), G (green), P (purple) " +
                "or Y (yellow) followed by a number corresponding to its level: ");
        do {
            valid = false;
            choice = graphicalCLI.getNext();

            if(choice.matches("[BGPYbgpy][1-3]")) {
                String color = choice.substring(0, 1).toUpperCase();
                level = Integer.parseInt(choice.substring(1, 2));
                switch (color) {    //TODO: soluzione carina B)
                    case "B":
                        color = CardColors.BLUE.name();
                        break;
                    case "G":
                        color = CardColors.GREEN.name();
                        break;
                    case "P":
                        color = CardColors.PURPLE.name();
                        break;
                    case "Y":
                        color = CardColors.YELLOW.name();
                        break;
                }
                for (DevelopmentDeckView developmentDeck : getDevelopmentDecks()) {
                    if (developmentDeck.getDeckColor().equals(CardColors.valueOf(color)) &&
                            developmentDeck.getDeckLevel() == level) {
                        if(!developmentDeck.getDeck().isEmpty()) {
                            developmentCard = (DevelopmentCard) developmentDeck.getDeck().get(0);
                            valid = true;
                        }
                        else
                            graphicalCLI.printString("There's no more cards available from this deck, " +
                                    "please try again ");
                        break;
                    }
                }
            }
            else
                graphicalCLI.printString("Invalid choice, please try again: ");
        } while (!valid);
        return developmentCard;
    }

    public void resolveProductionWildcards() {
        List<Production> resolvedProductions = new ArrayList<>();
        for(Production production : getProductionsToActivate()) {
            List<Resource> consumedResolved =  production.getConsumed().stream()
                    .filter((r -> r.getResourceType() != ResourceType.WILDCARD)).collect(Collectors.toList());
            List<Resource> producedResolved =  production.getProduced().stream()
                    .filter((r -> r.getResourceType() != ResourceType.WILDCARD)).collect(Collectors.toList());
            List<Resource> consumedWildcards = production.getConsumed().stream()
                    .filter((r -> r.getResourceType() == ResourceType.WILDCARD)).collect(Collectors.toList());
            List<Resource> producedWildcards = production.getProduced().stream()
                    .filter((r -> r.getResourceType() == ResourceType.WILDCARD)).collect(Collectors.toList());

            consumedWildcards = getResourcesOneByOne(consumedWildcards);
            producedWildcards = getResourcesOneByOne(producedWildcards);

            if(consumedWildcards.size() > 0 || producedWildcards.size() > 0) {
                graphicalCLI.printlnString("Resolve wildcards for the following production:");
                graphicalCLI.printProduction(production);
                if(consumedWildcards.size() > 0) {
                    graphicalCLI.printlnString("Choose for consumed wildcards:");
                    for (Resource wildcard : consumedWildcards) {
                        ResourceType chosenType = graphicalCLI.objectOptionSelector(
                                ResourceType.getRealValues(),
                                rt -> graphicalCLI.printlnString(rt.toString()));
                        consumedResolved.add(new Resource(chosenType, wildcard.getQuantity()));
                    }
                }
                if(producedWildcards.size() > 0) {
                    graphicalCLI.printlnString("Choose for produced wildcards:");
                    for (Resource wildcard : producedWildcards) {
                        ResourceType chosenType = graphicalCLI.objectOptionSelector(
                                ResourceType.getRealValues(),
                                rt -> graphicalCLI.printlnString(rt.toString()));
                        producedResolved.add(new Resource(chosenType, wildcard.getQuantity()));
                    }
                }
            }
            resolvedProductions.add(new Production(consumedResolved, producedResolved));
        }
        setProductionsToActivate(resolvedProductions);
    }

    public List<RequestResources> chooseStorages(List<Resource> resources) {
        Storage.aggregateResources(resources);
        try {
            PlayerBoardView playerBoard = getLocalPlayerBoard();
            graphicalCLI.printWarehouseConfiguration(playerBoard.getWarehouse(), false);
            graphicalCLI.printStrongbox(getLocalPlayerBoard().getStrongbox());
        }catch(NotExistingNicknameException e){
            e.printStackTrace();
        }

        graphicalCLI.printString("You have to take these resources: ");
        graphicalCLI.printResources(resources);

        int choice;
        boolean first = true;
        List<Resource> whResources = new ArrayList<>();
        List<Resource> whLeaderResources = new ArrayList<>();
        List<Resource> strongboxResources = new ArrayList<>();
        List<RequestResources> requestResources = new ArrayList<>();
        List<Resource> allResources = getResourcesOneByOne(resources);
        graphicalCLI.printChooseStorage();
        for(Resource res :allResources){
            graphicalCLI.printString("Resource: " );
            graphicalCLI.printResource(res);
            graphicalCLI.printString(" - Storage number: ");
            do{
                if(!first) graphicalCLI.printString("Invalid storage number, try again: ");
                choice = graphicalCLI.getNextInt();
                first = choice >= 0 && choice <= 3;
            }while(choice<0 || choice >3);

            if(choice == 1){
                whResources.add(res);
            }else if (choice == 2){
                whLeaderResources.add(res);
            }else if(choice == 3){
                strongboxResources.add(res);
            }
        }
        requestResources.add(new RequestResources(whResources,StorageType.WAREHOUSE));
        requestResources.add(new RequestResources(whLeaderResources,StorageType.LEADER));
        requestResources.add(new RequestResources(strongboxResources,StorageType.STRONGBOX));

        return requestResources;
    }

    public GraphicalCLI getGraphicalCLI() {
        return graphicalCLI;
    }
}
