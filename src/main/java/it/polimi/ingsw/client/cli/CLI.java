package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.client.structures.*;
import it.polimi.ingsw.utils.exceptions.NotExistingNicknameException;
import it.polimi.ingsw.server.Server;
import it.polimi.ingsw.server.model.boards.Player;
import it.polimi.ingsw.server.model.cards.card.*;
import it.polimi.ingsw.server.model.storage.*;
import it.polimi.ingsw.server.saves.GameSave;
import it.polimi.ingsw.server.saves.SaveInteractions;
import it.polimi.ingsw.utils.Constants;
import it.polimi.ingsw.utils.messages.client.*;
import it.polimi.ingsw.utils.messages.server.ack.ServerAckMessage;
import it.polimi.ingsw.utils.messages.server.action.ServerActionMessage;
import it.polimi.ingsw.utils.messages.server.update.ServerUpdateMessage;

import java.io.*;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

public class CLI extends ClientController {

    private boolean idle;
    private List<MenuOption> playerTurnMenu, opponentTurnMenu;
    private final GraphicalCLI graphicalCLI;

    /**
     * CLI constructor with no parameters
     */
    public CLI() {
        super();
        idle = false;
        graphicalCLI = new GraphicalCLI();
        initMenus();
    }


    @Override
    public void setup() {
        if(askMultiplayer()) {
            connect();
            new Thread(getMessageHandler()).start();
            askNickname();
        }
        else {
            setNickname(graphicalCLI.askNickname());
            try {
                localSetup();
                graphicalCLI.printlnString("Creating a local game");
            } catch (IOException e) {
                graphicalCLI.printlnString("Unable to create local game");
            }
        }
    }

    /**
     * Asks the user if they want to connect to the server
     * @return Returns true if the user wants to connect, false otherwise
     */
    public boolean askMultiplayer() {
        graphicalCLI.printString("Do you want to connect to the server? ");
        return graphicalCLI.isAnswerYes();
    }

    /**
     * Connects the client to the server by asking IP address and port for the socket
     */
    public void connect() {
        boolean success;
        do {
            graphicalCLI.printString("Insert the IP address of the server: ");
            String ip = graphicalCLI.getNextLine();
            graphicalCLI.printString("Insert the port of the server: ");
            int port = graphicalCLI.getNextInt();
            graphicalCLI.printlnString("Connecting...");
            if(port <= 0) port = Server.SOCKET_PORT;    //TODO: temporaneo per testare
            success = getMessageHandler().connect(ip, port);
            if (success)
                graphicalCLI.printlnString("Connected");
            else
                graphicalCLI.printlnString("Server unreachable");
        } while(!success);
    }

    @Override
    public void run() {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        LinkedBlockingQueue<ServerActionMessage> actionQueue = getMessageHandler().getActionQueue();
        LinkedBlockingQueue<ServerAckMessage> responseQueue = getMessageHandler().getResponseQueue();
        LinkedBlockingQueue<ServerUpdateMessage> updateQueue = getMessageHandler().getUpdateQueue();
        List<ClientMessage> confirmationList = getMessageHandler().getConfirmationList();

        new Thread(getUpdateMessageReader()).start();

        boolean displayMenu = true;

        setAlive(true);

        while(isAlive()) {
            if (confirmationList.size() != 0 && updateQueue.isEmpty()) {
                try {
                    ServerAckMessage ack = responseQueue.take();
                    confirmationList.remove(confirmationList.stream().filter(ack::compareTo).findFirst().orElseThrow());
                    ack.activateResponse(this);
                    displayMenu = true;
                } catch(InterruptedException e) {
                    e.printStackTrace();
                }
            }
            else {
                if (actionQueue.size() > 0 && updateQueue.isEmpty()) {
                    actionQueue.poll().doAction(this);
                    displayMenu = true;
                }
                else if(idle) {
                    if (displayMenu) {
                        graphicalCLI.printlnString("Press ENTER to display action menu");
                        displayMenu = false;
                    }
                    try {
                        if (br.ready()) {
                            while(br.ready())   //Clears the buffer
                                br.readLine();
                            turnMenu();
                            displayMenu = true;
                        }
                    } catch(Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    public void ackNotification(String message, boolean visual) {
        graphicalCLI.printlnString(message);
    }

    @Override
    public void askNickname() {
        setNickname(graphicalCLI.askNickname());
        getMessageHandler().sendClientMessage(new ConnectionMessage(getNickname()));
    }

    @Override
    public void askNewLobby(int lobbySize, int waitingPlayers) {
        if (lobbySize == waitingPlayers) {
            graphicalCLI.printlnString("There aren't any players waiting for a match!");
            int size = graphicalCLI.integerSelector(1, Constants.MAX_LOBBY_SIZE.value(),
                    "Insert the number of desired players for the game", false);
            setNumberOfPlayers(size);
            getMessageHandler().sendClientMessage(new NewLobbyMessage(size));
        }
        else {
            graphicalCLI.printlnString("There is already a " + lobbySize + " player lobby waiting for "
                    + (lobbySize - waitingPlayers) + " more player(s)");
            setNumberOfPlayers(lobbySize);
        }
    }

    @Override
    public void displaySaves(List<GameSave> saves) {
        ClientMessage messageToSend = new SaveInteractionMessage(null, SaveInteractions.NO_ACTION);
        if(saves.size() > 0) {
            graphicalCLI.printString("Do you want to load a save? ");
            if (graphicalCLI.isAnswerYes()) {
                GameSave save = graphicalCLI.objectOptionSelector(saves,
                        s -> graphicalCLI.printlnString(s.getFileName()),
                        () -> graphicalCLI.printlnString("Choose a save file to load or delete: "),
                        null,
                        () -> graphicalCLI.printString("Found only one save: "),
                        false, -1, -1, -1).get(0);
                String option = graphicalCLI.objectOptionSelector(List.of("Load", "Delete"),
                        graphicalCLI::printlnString,
                        () -> graphicalCLI.printlnString("Do you want to load it or delete it?"),
                        null,
                        null,
                        false, -1, -1, -1).get(0);
                if (option.equals("Delete"))
                    messageToSend = new SaveInteractionMessage(save, SaveInteractions.DELETE_SAVE);
                else
                    messageToSend = new SaveInteractionMessage(save, SaveInteractions.OPEN_SAVE);
            }
        }
        getMessageHandler().sendClientMessage(messageToSend);
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

            graphicalCLI.printMarket(getMarket());
            graphicalCLI.printDevelopmentDeckTop(getDevelopmentDecks().getDecks());

            graphicalCLI.objectOptionSelector(leaderHand,
                    graphicalCLI::printLeaderCard,
                    () -> graphicalCLI.printlnString("\nYou have to discard 2 leader cards from your hand:"),
                    () -> graphicalCLI.printString("Choose 2 cards from your hand: "),
                    null,
                    true, 2, 2, 1
            ).forEach(c -> {
                selected.add(c);
                leaderHand.remove(c);
            });

            getMessageHandler().sendClientMessage(new LeaderCardDiscardMessage(selected, true));
        }catch (NotExistingNicknameException e){
            e.printStackTrace();
        }
    }

    @Override
    public void askResourceEqualize(List<Resource> resources) {
        List<Resource> newResources = new ArrayList<>();
        graphicalCLI.printlnString("");
        for(Resource resource : resources) {
            if (resource.getResourceType() == ResourceType.FAITH)
                newResources.add(new Resource(ResourceType.FAITH, resource.getQuantity()));
            else if (resource.getResourceType() == ResourceType.WILDCARD)
                graphicalCLI.objectOptionSelector(ResourceType.getRealValues(),
                        rt -> graphicalCLI.printlnString(rt.toString()),
                        () -> graphicalCLI.printlnString("You can add " + resource.getQuantity() +
                                " resource(s) to your warehouse since you are not the first player"),
                        () -> graphicalCLI.printString("Choose " + resource.getQuantity() +
                                " resource(s) to add: "),
                        null,
                        true, resource.getQuantity(), resource.getQuantity(), -1
                ).forEach(rt -> newResources.add(new Resource(rt, 1)));
        }
        if(newResources.size() > 0) {
            graphicalCLI.printlnString("Now place the resources on the shelves:");
            placeResourcesOnShelves(newResources);
        }
        idle = true;
    }

    @Override
    public void notifyStartTurn(String nickname) {
        if (nickname.equals(getNickname())) {
            graphicalCLI.printlnString("\nNOW IT'S YOUR TURN!\n");
            setPlayerTurn(true);
        }
        else {
            graphicalCLI.printlnString("\nNOW IT'S " + nickname.toUpperCase() + "'S TURN!\n");
            setPlayerTurn(false);
        }
        turnMenu();
        idle = true;
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
                graphicalCLI.printString("\nPlease choose a valid resource type for the wildcard:");
                index = graphicalCLI.getNextInt() - 1;
            } while(0 > index || index > availableAbilities.size());

            plainResources.add(new Resource(availableAbilities.get(index), 1));

            availableAbilities.remove(index);
            marblesLeft--;
        }
        placeResourcesOnShelves(plainResources);
        idle = true;
    }

    @Override
    public void notifyLorenzoCard(LorenzoCard lorenzoCard) {
        graphicalCLI.printlnString("\nNOW IT'S LORENZO'S TURN\n");
        graphicalCLI.printlnString("Lorenzo pulls a card from his deck");
        graphicalCLI.printLorenzoCard(lorenzoCard);
    }

    @Override
    public void notifyLastRound() {
        graphicalCLI.printlnString("Last round before the game ends!");
        idle = true;
    }

    @Override
    public void notifyEndGame(List<Player> players) {
        graphicalCLI.printlnString("THE GAME HAS ENDED!");
        graphicalCLI.printlnString("Scoreboard:");
        // Lorenzo doesn't have a VP score, therefore the ranking board won't display it
        for(Player player : players.stream().sorted(Comparator.comparingInt(Player::getFinalPosition))
                .collect(Collectors.toList()))
            graphicalCLI.printlnString(player.getFinalPosition() + ": " + player.getNickname() +
                    (player.getTotalVP() >= 0 ? (" with " + player.getTotalVP() + " VP") : "")
            );
        graphicalCLI.printlnString("Thank you for having played Master of Renaissance!");
    }

    /**
     * Visualizes and makes the player choose between the options from their current menu
     */
    public void turnMenu() {
        List<MenuOption> turnMenu = opponentTurnMenu;   //Defaults as the opponent turn
        Runnable choiceText = () -> graphicalCLI.printString("\nChoose an action to do: ");
        if(isPlayerTurn()) {    // If it's the player's turn sets the corresponding menu options
            turnMenu = new ArrayList<>(playerTurnMenu);
            if(isMainActionPlayed())    // If the player has already played their main action, he can save the game
                turnMenu.add(
                        new MenuOption("Save game", () -> getMessageHandler().sendClientMessage(new SaveMessage()))
                );
            choiceText = () -> graphicalCLI.printString("\nChoose an action to do on your turn: ");
        }
        graphicalCLI.objectOptionSelector(turnMenu,     // Makes the player choose between their actions and runs it
                m -> graphicalCLI.printlnString(m.getTitle()),
                () -> graphicalCLI.printlnString("MENU:\n"),
                choiceText,
                null,
                false, -1, -1, -1
        ).get(0).getAction().run();
    }

    public void selectMarket() {
        graphicalCLI.printMarket(getMarket());
        if(idle = graphicalCLI.askGoBack())
            return;

        graphicalCLI.printString("Where do you want to place the marble?\n" +
                "Choose R (row) or C (column) followed by a number: ");
        boolean valid;
        do {
            int row = -1, column = -1;
            valid = true;

            String choice = graphicalCLI.getNextLine();
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
                getMessageHandler().sendClientMessage(new SelectMarketMessage(row, column));
            else
                graphicalCLI.printString("Invalid choice, please try again: ");
        } while(!valid);
        setMainActionPlayed(true);
    }

    @Override
    public void selectDevDecks() {
        int space;
        try {
            graphicalCLI.printWarehouseConfiguration(getLocalPlayerBoard().getWarehouse(), false);
            graphicalCLI.printStrongbox(getLocalPlayerBoard().getStrongbox());
            graphicalCLI.printDevelopmentDeckTop(getDevelopmentDecks().getDecks());
            if (idle = graphicalCLI.askGoBack())
                return;

            DevelopmentCard developmentCard = chooseCardFromDecks();

            space = graphicalCLI.integerSelector(0, Constants.BASE_DEVELOPMENT_SPACES.value() - 1,
                    "Which space do you want to put the card on?", true);

            getMessageHandler().sendClientMessage(new CanBuyDevelopmentCardMessage(developmentCard, space));
        } catch(NotExistingNicknameException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void selectProductions() {
        try {
            graphicalCLI.printWarehouseConfiguration(getLocalPlayerBoard().getWarehouse(),false);
            graphicalCLI.printStrongbox(getLocalPlayerBoard().getStrongbox());

            List<Production> productions = new ArrayList<>();
            List<Production> productionsToActivate;

            productions.add(getLocalPlayerBoard().getBasicProduction());
            getLocalPlayerBoard().getDevelopmentBoard().getSpaces().stream().filter(d -> !d.isEmpty())
                    .forEach(d -> productions.add(((DevelopmentCard) d.get(0)).getProduction()));
            productions.addAll(getLocalPlayerBoard().getActiveAbilityProductions());

            graphicalCLI.printlnString("Available productions:");
            graphicalCLI.printNumberedList(productions, graphicalCLI::printProduction);
            if(idle = graphicalCLI.askGoBack())
                return;

            if(productions.size() > 0) {
                productionsToActivate = graphicalCLI.objectOptionSelector(productions,
                        graphicalCLI::printProduction,
                        () -> graphicalCLI.printlnString("Available productions:"),
                        () -> graphicalCLI.printString("Choose a production you want to activate " +
                                "by entering its number: "),
                        () -> graphicalCLI.printlnString("Found only one production to activate"),
                        true, 1, productions.size(), 1);
                productionsToActivate = resolveProductionWildcards(productionsToActivate);
                if(productionsToActivate.size() > 0)
                    getMessageHandler().sendClientMessage(new CanActivateProductionsMessage(productionsToActivate));
            }
            else
                graphicalCLI.printlnString("No productions");
        } catch(NotExistingNicknameException e) {
            e.printStackTrace();
        }
        setMainActionPlayed(true);
    }

    @Override
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
            shelves = warehouse.getShelvesClone();
            graphicalCLI.printWarehouseConfiguration(warehouse, true);
            if(!toPlace.isEmpty()) {
                graphicalCLI.printString("Resources to place: ");
                graphicalCLI.printGraphicalResources(toPlace);
                if(getLocalPlayerBoard().getWarehouse().getShelves().size()<3)
                    freeSlots = true;
                else {
                    for (Shelf shelf : getLocalPlayerBoard().getWarehouse().getShelves()) {
                        if (shelf.getResourceType().equals(ResourceType.WILDCARD) ||
                                shelf.getLevel() > shelf.getResources().getQuantity()) {
                            freeSlots = true;
                            break;
                        }
                    }
                }
                if (freeSlots) { //it's possible to place resources
                    if (!shelves.stream().allMatch(Shelf::isEmpty)) { //before starting move resources
                        graphicalCLI.printString("Do you want to rearrange the warehouse? ");
                        rearranged = graphicalCLI.isAnswerYes();
                        if(rearranged){
                            shelves = warehouse.getShelvesClone();
                        }
                    }
                    while (!toPlace.isEmpty()) {
                        if (rearranged) {
                            graphicalCLI.printWarehouseConfiguration(new WarehouseView(shelves),true);
                            graphicalCLI.printString("Resources to place: ");
                            graphicalCLI.printGraphicalResources(toPlace);
                        }
                        resourceToPlace = toPlace.get(0);
                        level = graphicalCLI.askWhichShelf(resourceToPlace, shelves.size(), true);
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
                        } else {
                            restoreConfiguration(warehouse, shelves, resources, toPlace, toDiscard, true);
                            rearranged = false;
                            graphicalCLI.printWarehouseConfiguration(new WarehouseView(shelves),true);
                            graphicalCLI.printString("Resources to place: ");
                            graphicalCLI.printGraphicalResources(toPlace);
                        }
                        if (toPlace.isEmpty() && !Storage.isDiscardedResCorrect(resources, toDiscard)) {
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
            getMessageHandler().sendClientMessage(new ShelvesConfigurationMessage(shelves, resources, toDiscard));
        }catch (NotExistingNicknameException e){
            e.printStackTrace();
        }
        idle = true;
    }

    @Override
    public void chooseDevelopmentStorages(DevelopmentCard cardToBuy, int spaceToPlace, List<Resource> cost) {
        Storage.aggregateResources(cost);
        List<Resource> realCost = cost;
        try {
            realCost = Storage.calculateDiscount(cost, getLocalPlayerBoard().getActiveAbilityDiscounts());
        } catch(NotExistingNicknameException e) {
            e.printStackTrace();
        }
        getMessageHandler().sendClientMessage(
                new RequestResourcesDevMessage(cardToBuy, spaceToPlace, chooseStorages(realCost)));
    }

    @Override
    public void chooseProductionStorages(List<Production> productionsToActivate, List<Resource> consumed) {
        Storage.aggregateResources(consumed);
        getMessageHandler().sendClientMessage(
                new RequestResourcesProdMessage(productionsToActivate, chooseStorages(consumed)));
    }

    /**
     * Chooses a storage for each resource to take in order to make a request
     * @param resources Resources to be taken
     * @return Returns a list of RequestResources
     */
    private List<RequestResources> chooseStorages(List<Resource> resources) {
        try {
            PlayerBoardView playerBoard = getLocalPlayerBoard();
            graphicalCLI.printWarehouseConfiguration(playerBoard.getWarehouse(), false);
            graphicalCLI.printStrongbox(getLocalPlayerBoard().getStrongbox());
        } catch(NotExistingNicknameException e) {
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
        for(Resource res : allResources) {
            graphicalCLI.printString("Resource: " );
            graphicalCLI.printResource(res);
            graphicalCLI.printString(" - Storage number: ");
            do {
                if(!first) graphicalCLI.printString("Invalid storage number, try again: ");
                choice = graphicalCLI.getNextInt();
                first = choice >= 0 && choice <= 3;
            } while(choice<0 || choice >3);

            if(choice == 1) {
                whResources.add(res);
            } else if (choice == 2) {
                whLeaderResources.add(res);
            } else if(choice == 3) {
                strongboxResources.add(res);
            }
        }
        if(whResources.size() > 0)
            requestResources.add(new RequestResources(whResources, StorageType.WAREHOUSE));
        if(whLeaderResources.size() > 0)
            requestResources.add(new RequestResources(whLeaderResources, StorageType.LEADER));
        if(strongboxResources.size() > 0)
            requestResources.add(new RequestResources(strongboxResources, StorageType.STRONGBOX));
        idle = true;
        return requestResources;
    }

    /**
     * Chooses a leader card to play or discard
     * @return The chosen leader card
     */
    @SuppressWarnings("unchecked")
    private List<LeaderCard> chooseLeaderCard() { //TODO: mettere nella graphicalCLI
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
        return null;
    }

    /**
     * Shows the opponents' boards
     */
    private void showOpponents(){
        if(getNumberOfPlayers()>1) {
            for (PlayerBoardView playerBoardView : getPlayerBoards()) {
                if (!playerBoardView.getNickname().equals(getNickname())) {
                    graphicalCLI.printPlayer(playerBoardView, getFaithTrack());
                }
            }
        } else graphicalCLI.printLorenzo(getLorenzoFaith(), getFaithTrack());
    }

    /**
     * Shows the player's board
     */
    private void showBoard(){
        try{
            graphicalCLI.printPlayer(getLocalPlayerBoard(), getFaithTrack());
        } catch (NotExistingNicknameException e){
            e.printStackTrace();
        }
    }

    /**
     * Moves all the Faith resources from a source list to destination list
     * @param dest List where the resources will be added to
     * @param src List from which the resources will be taken from
     */
    public void moveFaith(List<Resource> dest, List<Resource> src) {        //TODO: refactor con streams
        for(int i = 0; i < src.size(); i++)                                 //TODO: da spostare (magari nello storage) in maniera che tutti possano usarlo
            if(src.get(i).getResourceType().equals(ResourceType.FAITH)) {   //TODO: invertire l'ordine dei paramteri, prima src, poi dest
                dest.add(src.get(i));
                src.remove(i);
                break;
            }
    }

    /**
     * Lets the user rearrange the warehouse
     * @return Returns the new list of shelves
     */
    private List<Shelf> rearrangeWarehouse() {
        List<Shelf> shelves = new ArrayList<>();
        try {
            WarehouseView warehouse = getLocalPlayerBoard().getWarehouse();
            shelves = warehouse.getShelvesClone();
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
            temporaryWarehouseShelves = warehouse.getShelvesClone();
            List<Resource> toPlace = getResourcesOneByOne(resources);
            boolean firstTurn = true;
            while (!toPlace.isEmpty()){
                resourceToPlace = toPlace.get(0);
                graphicalCLI.printWarehouseConfiguration(new WarehouseView(shelves), true);
                graphicalCLI.printString("Resources to place: ");
                graphicalCLI.printGraphicalResources(toPlace);
                level=graphicalCLI.askWhichShelf(resourceToPlace, shelves.size(), false);

                if(firstTurn)
                    firstTurn = false;

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
        } catch (NotExistingNicknameException e){
            e.printStackTrace();
        }
        return shelves;
    }

    /**
     * Split a list of resources in more lists with a size of 1
     * @param resources List of resources to be split
     * @return Returns the list of resources
     */
    private List<Resource> getResourcesOneByOne(List<Resource> resources) {     //TODO: da spostare (magari nello storage) in maniera che tutti possano usarlo
        List<Resource> resourcesOneByOne = new ArrayList<>();
        for(Resource resource : resources){
            for(int i=0; i<resource.getQuantity(); i++){
                resourcesOneByOne.add(new Resource(resource.getResourceType(),1));
            }
        }
        return resourcesOneByOne;
    }

    /**
     * Manages the specified resources placement on the empty shelf given by parameter
     * @param shelves List of shelves to check
     * @param toPlace List of resources to place
     * @param selectedShelf Shelf where the resources will be placed
     * @param resourceToPlace Resources' type
     */
    private void emptyShelfManagement(List<Shelf> shelves, List<Resource> toPlace,
                                      Shelf selectedShelf, Resource resourceToPlace) {
        if(Shelf.isResourceTypeUnique(shelves,resourceToPlace.getResourceType())) { //there are no shelves with the same resource type
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
                    if(otherShelf != null) {
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
    }

    /**
     * Manages the resources placement on the shelf with the same resource type of the resources
     * @param shelves List of shelves to check
     * @param toPlace List of resources to place
     * @param selectedShelf Shelf where the resources will be placed
     * @param resourceToPlace Resources' type
     */
    private void sameResTypeShelfManagement(List<Shelf> shelves, List<Resource> toPlace,
                                            Shelf selectedShelf, Resource resourceToPlace) {
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

    /**
     * Manages the resources placement on the shelf with different resource type from the resources
     * @param shelves List of shelves to check
     * @param toPlace List of resources to place
     * @param selectedShelf Shelf where the resources will be placed
     * @param resourceToPlace Resources' type
     */
    private void differentResTypeShelfManagement(List<Shelf> shelves, List<Resource> toPlace,
                                                 Shelf selectedShelf, Resource resourceToPlace) {
        graphicalCLI.printString("This shelf contains a different resource type\nIf you want" +
                " to place it here anyway, insert YES and then you'll place again the removed ones: ");
        if(graphicalCLI.isAnswerYes()) {
            if (Shelf.isResourceTypeUnique(shelves, resourceToPlace.getResourceType())) { //there are no shelves with the same resource type
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
                        if(otherShelf != null) {
                            for (int i = 0; i < otherShelf.getResources().getQuantity(); i++) {
                                toPlace.add(1,
                                        new Resource(otherShelf.getResources().getResourceType(), 1));
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
    }

    /**
     * Place a resource on a shelf
     * @param shelf Shelf where the resource will be placed
     * @param resource Resource to be placed
     */
    private void placeResource(Shelf shelf, Resource resource) {                //TODO: da spostare (magari nello shelf) in maniera che tutti possano usarlo
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

    /**
     * Gets a shelf from a list of shelves with a specific resourceType
     * @param shelves List of shelves to be checked
     * @param resourceType ResourceType to be found
     * @return Returns a shelf with the specific resourceType
     */
    private Shelf getShelfWithSameResource(List<Shelf> shelves, ResourceType resourceType) {     //TODO: da spostare (magari nello shelf) in maniera che tutti possano usarlo
        for(Shelf shelf : shelves){
            if(!shelf.isLeader() && shelf.getResourceType().equals(resourceType))
                return shelf;
        }
        return null;
    }

    /**
     * Checks if the configuration of the resources given by parameter is rearrangeable among the warehouse's shelves
     * @param shelves List of shelves to check
     * @param resource Resources to check
     * @return Returns true if it's rearrangeable, false otherwise
     */
    private boolean isShelfRearrangeable(List<Shelf> shelves, Resource resource) {
        Shelf shelfWithResources = getShelfWithSameResource(shelves, resource.getResourceType());
        int totalLeaderShelves = 2 * (int)(shelves.stream().filter(shelf -> shelf.isLeader() && shelf.getResourceType()
                    .equals(resource.getResourceType())).count());
        return shelfWithResources != null &&
                shelfWithResources.getResources().getQuantity() + resource.getQuantity() <= 3 + totalLeaderShelves;
    }

    /**
     * Reset a shelf
     * @param shelf Shelf to be reset
     */
    private void resetShelf(Shelf shelf) {          //TODO: da spostare (magari nello shelf) in maniera che tutti possano usarlo
        if(!shelf.isLeader()) {
            shelf.setResourceType(ResourceType.WILDCARD);
            shelf.getResources().setResourceType(ResourceType.WILDCARD);
        }
        shelf.getResources().setQuantity(0);
    }

    /**
     * Restores resources in the warehouse and resets attributes
     * @param warehouse Warehouse where to restore the resources from
     * @param shelves List of shelves to restore
     * @param resources List where to restore the "resources to place" from
     * @param toPlace List of resources to place
     * @param toDiscard List of resources to discard
     * @param canDiscard True if there are resources that can be discarded, false otherwise
     */
    private void restoreConfiguration(WarehouseView warehouse, List<Shelf> shelves, List<Resource> resources,
                                      List<Resource> toPlace, List<Resource> toDiscard, boolean canDiscard) {
        shelves.clear();
        shelves.addAll(warehouse.getShelvesClone());
        toPlace.clear();
        toPlace.addAll(getResourcesOneByOne(resources));
        if(canDiscard) {
            toDiscard.clear();
            moveFaith(toDiscard, toPlace);
        }
        graphicalCLI.printlnString("Warehouse restored");
    }

    /**
     * Chooses a development card from the development decks
     * @return Returns the chosen development card
     */
    private DevelopmentCard chooseCardFromDecks() {
        boolean valid;
        String choice;
        int level;
        DevelopmentCard developmentCard = new DevelopmentCard();
        graphicalCLI.printString("Which card do you want to buy?\nChoose B (blue), G (green), P (purple) " +
                "or Y (yellow) followed by a number corresponding to its level: ");
        do {
            valid = false;
            choice = graphicalCLI.getNextLine();

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
                for(DevelopmentDeckView developmentDeck : getDevelopmentDecks().getDecks()) {
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

    /**
     * Resolves wildcards for a list of productions
     * @param productions List of production to be solved
     * @return Returns the list of productions with their wildcards solved
     */
    public List<Production> resolveProductionWildcards(List<Production> productions) {
        List<Production> resolvedProductions = new ArrayList<>();
        for(Production production : productions) {
            List<Resource> consumedResolved = production.getConsumed().stream()
                    .filter((r -> r.getResourceType() != ResourceType.WILDCARD)).collect(Collectors.toList());
            List<Resource> producedResolved = production.getProduced().stream()
                    .filter((r -> r.getResourceType() != ResourceType.WILDCARD)).collect(Collectors.toList());
            List<Resource> consumedWildcards = production.getConsumed().stream()
                    .filter((r -> r.getResourceType() == ResourceType.WILDCARD)).collect(Collectors.toList());
            List<Resource> producedWildcards = production.getProduced().stream()
                    .filter((r -> r.getResourceType() == ResourceType.WILDCARD)).collect(Collectors.toList());

            consumedWildcards = getResourcesOneByOne(consumedWildcards);
            producedWildcards = getResourcesOneByOne(producedWildcards);

            if(consumedWildcards.size() > 0 || producedWildcards.size() > 0) {
                graphicalCLI.printString(GraphicalCLI.YELLOW_BRIGHT);
                graphicalCLI.printlnString("\nResolve wildcards for the following production:\n");
                graphicalCLI.printString(GraphicalCLI.RESET);
                graphicalCLI.printProduction(production);
                if(consumedWildcards.size() > 0) {
                    graphicalCLI.printString(GraphicalCLI.YELLOW_BRIGHT);
                    graphicalCLI.printlnString("\nChoose resource types for the consumed wildcards:\n");
                    graphicalCLI.printString(GraphicalCLI.RESET);   //TODO: sistemare duplicazione
                    final List<Resource> finalConsumedWildcards = consumedWildcards;
                    graphicalCLI.objectOptionSelector(ResourceType.getRealValues(),
                            rt -> graphicalCLI.printlnString(rt.toString()),
                            () -> graphicalCLI.printlnString("You have " + finalConsumedWildcards.size() +
                                    " wildcard(s) to resolve"),
                            () -> graphicalCLI.printString("Choose " + finalConsumedWildcards.size() +
                                    " resource type(s) for the wildcard(s): "),
                            null,
                            true, consumedWildcards.size(), consumedWildcards.size(), -1
                    ).forEach(rt -> consumedResolved.add(new Resource(rt, 1)));
                }
                if(producedWildcards.size() > 0) {
                    graphicalCLI.printString(GraphicalCLI.YELLOW_BRIGHT);
                    graphicalCLI.printlnString("\nChoose for produced wildcards:\n");
                    graphicalCLI.printString(GraphicalCLI.RESET);
                    final List<Resource> finalProducedWildcards = producedWildcards;
                    graphicalCLI.objectOptionSelector(ResourceType.getRealValues(),
                            rt -> graphicalCLI.printlnString(rt.toString()),
                            () -> graphicalCLI.printlnString("You have " + finalProducedWildcards.size() +
                                    " wildcard(s) to resolve"),
                            () -> graphicalCLI.printString("Choose " + finalProducedWildcards.size() +
                                    " resource type(s) for the wildcard(s): "),
                            null,
                            true, consumedWildcards.size(), consumedWildcards.size(), -1
                    ).forEach(rt -> producedResolved.add(new Resource(rt, 1)));
                }
            }
            Storage.aggregateResources(consumedResolved);
            Storage.aggregateResources(producedResolved);
            resolvedProductions.add(new Production(consumedResolved, producedResolved));
        }
        return resolvedProductions;
    }

    /**
     * Initializes the menu options for the "player" and "opponents" possible turns
     */
    private void initMenus() {
        playerTurnMenu = new ArrayList<>();
        opponentTurnMenu = new ArrayList<>();
        List<MenuOption> shared = Arrays.asList(
                new MenuOption("View market and development decks", () -> {
                    graphicalCLI.printMarket(getMarket());
                    graphicalCLI.printlnString("");
                    graphicalCLI.printDevelopmentDeckTop(getDevelopmentDecks().getDecks());
                    graphicalCLI.printlnString("");
                }),
                new MenuOption("View your board", this::showBoard),
                new MenuOption("View opponents' boards", this::showOpponents),
                new MenuOption("Rearrange Warehouse", () -> {
                    try {
                        if(!getLocalPlayerBoard().getWarehouse().isEmpty()) {
                            List<Shelf> shelves = rearrangeWarehouse();
                            getMessageHandler().sendClientMessage(new ShelvesConfigurationMessage(shelves));
                        } else graphicalCLI.printlnString("You have no resources to rearrange");
                    } catch (NotExistingNicknameException e) {
                        e.printStackTrace();
                    }
                })
        );
        playerTurnMenu.addAll( Arrays.asList(
                new MenuOption("Get resources from market", () -> {
                    if (!isMainActionPlayed()) selectMarket(); else graphicalCLI.printlnString("You can't play this action on your turn anymore");
                }),
                new MenuOption("Buy a development card", () -> {
                    if (!isMainActionPlayed()) selectDevDecks(); else graphicalCLI.printlnString("You can't play this action on your turn anymore");
                }),
                new MenuOption("Activate your productions", () -> {
                    if (!isMainActionPlayed()) selectProductions(); else graphicalCLI.printlnString("You can't play this action on your turn anymore");
                }),
                new MenuOption("Activate a leader card", () -> {
                    List<LeaderCard> leaderCards = chooseLeaderCard();
                    if(leaderCards != null && leaderCards.size() > 0) getMessageHandler().sendClientMessage(new LeaderCardPlayMessage(leaderCards));
                }),
                new MenuOption("Discard a leader card", () -> {
                    List<LeaderCard> leaderCards = chooseLeaderCard();
                    if(leaderCards != null && leaderCards.size() > 0) getMessageHandler().sendClientMessage(new LeaderCardDiscardMessage(leaderCards));
                })
        ));
        playerTurnMenu.addAll(shared);
        playerTurnMenu.addAll( Arrays.asList(
                new MenuOption("End turn", () -> {
                    if (isMainActionPlayed()) {
                        setPlayerTurn(false);
                        getMessageHandler().sendClientMessage(new EndTurnMessage());
                    }
                    else graphicalCLI.printlnString("You haven't played any main action yet!");
                })
        ));
        opponentTurnMenu.add(
                new MenuOption("Wait for my turn", () -> graphicalCLI.printlnString("Waiting for your turn..."))
        );
        opponentTurnMenu.addAll(shared);
    }
}
