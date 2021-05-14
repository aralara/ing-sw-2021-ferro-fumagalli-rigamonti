package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.client.MessageHandler;
import it.polimi.ingsw.client.structures.*;
import it.polimi.ingsw.exceptions.NotExistingNickname;
import it.polimi.ingsw.server.Server;
import it.polimi.ingsw.server.model.cards.card.CardColors;
import it.polimi.ingsw.server.model.cards.card.DevelopmentCard;
import it.polimi.ingsw.server.model.cards.card.LeaderCard;
import it.polimi.ingsw.server.model.cards.deck.Deck;
import it.polimi.ingsw.server.model.storage.*;
import it.polimi.ingsw.utils.messages.client.*;
import it.polimi.ingsw.utils.messages.server.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

public class CLI {

    private String nickname;
    private int numberOfPlayers;
    private int lorenzoFaith;                   //TODO: gestione lorenzo WIP
    private final List<PlayerBoardView> playerBoards;
    private final MarketView marketView;
    private final List<DevelopmentDeckView> developmentDecks;
    private final FaithTrackView faithTrackView;
    private List<Resource> resourcesToPut;      //TODO: valutare se serve memorizzare
    private DevelopmentCard cardToBuy;
    private int spaceToPlace;
    private List<Production> productionsToActivate;
    private final Scanner scanner;
    private boolean goBack, mainActionPlayed, endTurn;
    private final GraphicalCLI graphicalCLI;
    private final MessageHandler messageHandler;

    public CLI() {
        lorenzoFaith = -1;
        playerBoards = new ArrayList<>();
        marketView = new MarketView();
        developmentDecks = new ArrayList<>();
        faithTrackView = new FaithTrackView();
        scanner = new Scanner(System.in);
        goBack = false;
        mainActionPlayed = false;
        graphicalCLI = new GraphicalCLI();
        messageHandler = new MessageHandler();
    }

    public String getNickname() {
        return nickname;
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public int getLorenzoFaith() {
        return lorenzoFaith;
    }

    public void setLorenzoFaith(int lorenzoFaith) {
        this.lorenzoFaith = lorenzoFaith;
    }

    public List<PlayerBoardView> getPlayerBoards() {
        return playerBoards;
    }

    public MarketView getMarketView() {
        return marketView;
    }

    public List<DevelopmentDeckView> getDevelopmentDecks() {
        return developmentDecks;
    }

    public FaithTrackView getFaithTrackView() {
        return faithTrackView;
    }

    public boolean isMainActionPlayed() {
        return mainActionPlayed;
    }

    public void setMainActionPlayed(boolean mainActionPlayed) {
        this.mainActionPlayed = mainActionPlayed;
    }

    public GraphicalCLI getGraphicalCLI() {
        return graphicalCLI;
    }

    public MessageHandler getPacketHandler() {
        return messageHandler;
    }

    public int getNextInt() {
        return scanner.nextInt();
    }

    public void setup() {
        //TODO: busy wait
        while(!connect());
        new Thread(messageHandler).start();
        graphicalCLI.printString("Insert your nickname\n");
        askNickname();
    }

    public void run() {
        Queue<ServerActionMessage> messageQueue = messageHandler.getQueue();
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        while(true) {
            try {
                if (br.ready())
                    turnMenu(true);
                else if (messageQueue.size() > 0)
                    messageQueue.remove().doAction(this);
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    private boolean connect() {
        graphicalCLI.printString("Insert the IP address of server\n");
        String ip = scanner.nextLine();

        return messageHandler.connect(ip, Server.SOCKET_PORT);
    }

    public void askNickname() {
        nickname = scanner.next();
        messageHandler.sendMessage(new ConnectionMessage(nickname));
    }

    public void createNewLobby() {
        int size;
        graphicalCLI.printString("There isn't any player waiting for a match!\n");
        do {
            graphicalCLI.printString("Insert the number of players that will play the game " +
                    "(value inserted must between 1 and 4)\n");
            size = scanner.nextInt();
        }while(size <= 0 || size >= 5);
        setNumberOfPlayers(size);
        messageHandler.sendMessage(new NewLobbyMessage(size));
    }

    public void setNumberOfPlayers(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
        if(numberOfPlayers == 1)
            lorenzoFaith = 0;
    }

    public PlayerBoardView playerBoardFromNickname(String nickname) throws NotExistingNickname {
        for(PlayerBoardView playerBoard : playerBoards)
            if(playerBoard.getNickname().equals(nickname))
                return playerBoard;
        throw new NotExistingNickname();
    }

    public ResourceType resourceTypeSelector(List<ResourceType> resourceTypes) {    //TODO: da spostare in graphical insieme allo scanner
        if(resourceTypes.size() > 0) {
            if(resourceTypes.size() == 1) {
                ResourceType res = resourceTypes.get(0);
                graphicalCLI.printString(res + " is the only resource type available\n");
                return res;
            }
            int index;
            graphicalCLI.printString("You can choose a resource type from the following: \n");
            graphicalCLI.printNumberedList(resourceTypes, rt -> graphicalCLI.printString(rt.name()));
            do {
                graphicalCLI.printString("Please choose a valid resource: ");
                index = scanner.nextInt()-1;
            } while(index < 0 || index>=4);
            return resourceTypes.get(index);
        }
        return null;
    }

    public List<Resource> resolveResourcesToEqualize(int wildcardQuantity) { //TODO: sarà chiamato una sola volta per equalizzare
        int index;
        List<Resource> resources = new ArrayList<>();
        for(int num=0; num<wildcardQuantity; num++){
            graphicalCLI.printString("You can choose a resource from the following: \n"); //TODO: in ordine come nel file... va bene?
            for(int i=0;i<4;i++)
                System.out.println((i+1) + ": " + ResourceType.values()[i].toString());
            index = -1;
            while(index<0 || index>=4){
                graphicalCLI.printString("Please choose a valid resource: ");
                index = scanner.nextInt()-1;
            }

            if(resources.size()>0 && resources.get(0).getResourceType()==ResourceType.values()[index]){
                resources.get(0).setQuantity(resources.get(0).getQuantity()+1);
            } else {
                resources.add(new Resource(ResourceType.values()[index], 1));
            }
        }
        return resources;
    }

    private void refresh() {
        graphicalCLI.printMarket(marketView);
        graphicalCLI.printString("\nThe development decks:\n");
        printDevelopmentDeckTop();
        try {
            PlayerBoardView playerBoard = playerBoardFromNickname(nickname);
            graphicalCLI.printFaithBoard(playerBoard,faithTrackView);
            //TODO: developmentBoard
            graphicalCLI.printWarehouse(playerBoard.getWarehouse());
            graphicalCLI.printExtraShelfLeader(playerBoard.getWarehouse());
            graphicalCLI.printStrongbox(playerBoard.getStrongbox());
        }catch(NotExistingNickname e){
            e.printStackTrace();
        }
    }

    public void storeTempResources(List<Resource> resourcesToMemorize) {    //TODO: da rimuovere
        resourcesToPut = new ArrayList<>(resourcesToMemorize);
    }

    public void tryToPlaceShelves() { //TODO: decidere visibility (anche x altri try)
        graphicalCLI.printString("The selected configuration is invalid\n");
        if(askGoBack())
            turnMenu(true);
        else {
            graphicalCLI.printString("Please, try again to place on the shelves:\n");
            chooseShelvesManagement(resourcesToPut);
        }
    }

    private void tryAgainToBuyCard() {
        graphicalCLI.printString("The selected configuration is invalid\n");
        //TODO: da fare
        if(askGoBack())
            turnMenu(true);
        else { }
    }

    private void tryAgainToActivateProduction() {
        graphicalCLI.printString("The selected configuration is invalid\n");
        //TODO: da fare
        if(askGoBack())
            turnMenu(true);
        else { }
    }

    public void chooseShelvesManagement(List<Resource> resources){ //TODO: x controllare se si hanno o meno i leader, valutare se serve
        try {
            PlayerBoardView player = playerBoardFromNickname(nickname);
            graphicalCLI.printWarehouseConfiguration(player.getWarehouse());

            if(!isLeaderShelfActive())
                placeResourcesOnShelves(resources);
            else {
                graphicalCLI.printExtraShelfLeader(player.getWarehouse());
                placeResourcesOnShelves(resources, true);
            }
        }catch (NotExistingNickname e){
            e.printStackTrace();
        }
    }

    private boolean isLeaderShelfActive() {
        List<Shelf> shelves = new ArrayList<>();
        try{
            shelves = playerBoardFromNickname(nickname).getWarehouse().getShelves();
        }catch (NotExistingNickname e){
            e.printStackTrace();
        }
        return shelves.stream().anyMatch(Shelf::IsLeader);
    }

    private void placeResourcesOnShelves(List<Resource> resources){
        //TODO: aggiungere gestione leader
        WarehouseView warehouse;
        List<Shelf> shelves = new ArrayList<>();
        List<Resource> toPlace;
        List<Resource> toDiscard = new ArrayList<>();
        Shelf selectedShelf;
        Resource resourceToPlace;
        int level;

        try{
            toPlace = getResourcesOneByOne(resources);
            for(int i=0; i<toPlace.size(); i++){ //to remove faith resource
                if(toPlace.get(i).getResourceType().equals(ResourceType.FAITH)){
                    toDiscard.add(toPlace.get(i));
                    toPlace.remove(i);
                    break;
                }
            }

            graphicalCLI.printString("Resources to place: ");
            graphicalCLI.printGraphicalResources(toPlace);

            if(checkFreeSlotInWarehouse()){ //it's possible to place resources
                boolean rearranged = rearrangeWarehouse();

                warehouse = playerBoardFromNickname(nickname).getWarehouse();
                shelves = getShelvesWarehouseCopy(warehouse.getShelves());

                while (!toPlace.isEmpty()){
                    if(rearranged) { //x non stampare più volte se non si riorganizza warehouse
                        graphicalCLI.printWarehouseConfiguration(new WarehouseView(shelves));
                        graphicalCLI.printString("Resources to place: ");
                        graphicalCLI.printGraphicalResources(toPlace);
                    }

                    resourceToPlace = toPlace.get(0);

                    level=askWhichShelf(resourceToPlace, shelves.size(), rearranged);
                    rearranged = true; //x non stampare più volte se non si riorganizza warehouse

                    if(level>0) {
                        selectedShelf = shelves.get(level - 1);

                        if (selectedShelf.getResourceType().equals(ResourceType.WILDCARD)) { //empty shelf
                            emptyShelfManagement(shelves, toPlace, selectedShelf, resourceToPlace);
                        }
                        else if (selectedShelf.getResourceType().equals(resourceToPlace.getResourceType())) { //shelf with the same resource type
                            sameResTypeShelfManagement(shelves, toPlace, selectedShelf, resourceToPlace);
                        }
                        else { //shelf with different resource type
                            differentResTypeShelfManagement(shelves, toPlace, selectedShelf, resourceToPlace);
                        }
                    }
                    else if(level==0){
                        toDiscard.add(new Resource(resourceToPlace.getResourceType(),resourceToPlace.getQuantity()));
                        graphicalCLI.printString("Resource discarded\n");
                        toPlace.remove(0);
                    }
                    else if(level<0) {
                        restoreConfiguration(warehouse, shelves, resources, toPlace, toDiscard);
                    }

                    if(toPlace.isEmpty() && !isDiscardedResCorrect(resources, toDiscard)){
                        graphicalCLI.printString("You're trying to discard resources already stored in the" +
                                " warehouse!\nThe warehouse will be restored and you'll be asked to place all the" +
                                " resources again\n");
                        restoreConfiguration(warehouse, shelves, resources, toPlace, toDiscard);
                    }
                }
                graphicalCLI.printWarehouseConfiguration(new WarehouseView(shelves));
            }
            else{
                graphicalCLI.printString("There are no available slots, all the resources will be discarded\n");
                toDiscard.addAll(toPlace);
            }

            messageHandler.sendMessage(new ShelvesConfigurationMessage(shelves, toDiscard));
        }catch (NotExistingNickname e){
            e.printStackTrace();
        }
    }

    private boolean checkFreeSlotInWarehouse() {
        try {
            if(playerBoardFromNickname(nickname).getWarehouse().getShelves().size()<3)
                return true;
            for (Shelf shelf : playerBoardFromNickname(nickname).getWarehouse().getShelves()) { //TODO: da controllare
                if (shelf.getResourceType().equals(ResourceType.WILDCARD) ||
                        shelf.getLevel() > shelf.getResources().getQuantity())
                    return true;
            }
        } catch (NotExistingNickname e){
            e.printStackTrace();
        }
        return false;
    }

    private boolean rearrangeWarehouse(){
        graphicalCLI.printString("Do you want to rearrange the warehouse? ");
        boolean rearranged = isAnswerYes();
        if(rearranged){
            //TODO: da fare
        }
        return rearranged;
    }

    private List<Shelf> getShelvesWarehouseCopy(List<Shelf> warehouse) {
        List<Shelf> shelves = new ArrayList<>();
        for(Shelf shelf : warehouse)
            shelves.add(new Shelf(shelf.getResourceType(), shelf.getResources(),
                    shelf.getLevel(), shelf.IsLeader())); //TODO: controllare se va anche con leaderShelf
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

    private int askWhichShelf(Resource resource, int numberOfShelves, boolean rearranged) {
        int level;
        if(rearranged) {
            graphicalCLI.printString("Do you want to restore warehouse to its original configuration? ");
            if (isAnswerYes())
                return -1;
        }
        graphicalCLI.printString("Where do you want to place " + resource.getResourceType()
                + "? (0 to discard it) ");
        level = scanner.nextInt();
        while (level<0 || level>numberOfShelves){
            graphicalCLI.printString("Choose a valid shelf: ");
            level = scanner.nextInt();
        }
        return level;
    }

    private void emptyShelfManagement(List<Shelf> shelves, List<Resource> toPlace, Shelf selectedShelf, Resource resourceToPlace) {
        if(isResourceTypeUnique(shelves,resourceToPlace.getResourceType())) { //there are no shelves with the same resource type
            placeResource(selectedShelf, resourceToPlace);
            toPlace.remove(0);
        }
        else {//there are shelves with the same resource type
            graphicalCLI.printString("There's already another shelf with the same resource type\n");
            if(isShelfRearrangeable(shelves, resourceToPlace)) { //it's possible to rearrange shelves
                graphicalCLI.printString("If you want to place it here anyway, insert YES and " +
                        "then you'll place again the removed ones from the other shelf: ");
                if(isAnswerYes()) {
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

    private void sameResTypeShelfManagement(List<Shelf> shelves, List<Resource> toPlace, Shelf selectedShelf, Resource resourceToPlace){
        if (selectedShelf.getResources().getQuantity() <= selectedShelf.getLevel() - 1) { //shelf not completely full
            placeResource(selectedShelf, resourceToPlace);
            toPlace.remove(0);
        }
        else { //shelf completely full
            graphicalCLI.printString("The selected shelf is already full\n");
            if(isShelfRearrangeable(shelves, resourceToPlace)){
                graphicalCLI.printString("If you want to remove the resources from this" +
                        " shelf to place them again on another one, insert YES: ");
                if(isAnswerYes()){
                    for(int i=0; i<selectedShelf.getResources().getQuantity(); i++){
                        toPlace.add(1, new Resource(selectedShelf.getResourceType(), 1));
                    }
                }
            }
        }
    }

    private void differentResTypeShelfManagement(List<Shelf> shelves, List<Resource> toPlace, Shelf selectedShelf, Resource resourceToPlace) {
        graphicalCLI.printString("This shelf contains a different resource type\nIf you want" +
                " to place it here anyway, insert YES and then you'll place again the removed ones: ");
        if(isAnswerYes()) {
            if (isResourceTypeUnique(shelves, resourceToPlace.getResourceType())) { //there are no shelves with the same resource type
                for (int i = 0; i < selectedShelf.getResources().getQuantity(); i++) {
                    toPlace.add(1, new Resource(selectedShelf.getResourceType(), 1));
                }
                placeResource(selectedShelf, resourceToPlace);
                toPlace.remove(0);
            }
            else {//there are shelves with the same resource type
                graphicalCLI.printString("There's already another shelf with the same resource type\n");
                if (isShelfRearrangeable(shelves, resourceToPlace)) { //it's possible to rearrange shelves
                    graphicalCLI.printString("If you want to place it here anyway, insert YES and " +
                            "then you'll place again the removed ones: ");
                    if (isAnswerYes()) {
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
        return shelves.stream().noneMatch(shelf -> shelf.getResourceType().equals(resourceType));
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
            if(shelf.getResourceType().equals(resourceType))
                return shelf;
        }
        return null; //TODO: brutto?
    }

    private boolean isShelfRearrangeable(List<Shelf> shelves, Resource resource){ //TODO: nome da cambiare?
        Shelf shelfWithResources = getShelfWithSameResource(shelves, resource.getResourceType());
        return shelfWithResources.getResources().getQuantity()+resource.getQuantity() <= 3;
    }

    private void resetShelf(Shelf shelf) {
        shelf.setResourceType(ResourceType.WILDCARD);
        shelf.getResources().setResourceType(ResourceType.WILDCARD);
        shelf.getResources().setQuantity(0);
    }

    private void restoreConfiguration(WarehouseView warehouse, List<Shelf> shelves, List<Resource> resources,
                                      List<Resource> toPlace, List<Resource> toDiscard){
        shelves.clear();
        shelves.addAll(getShelvesWarehouseCopy(warehouse.getShelves()));
        toPlace.clear();
        toPlace.addAll(getResourcesOneByOne(resources));
        toDiscard.clear();
        graphicalCLI.printString("Warehouse restored\n");
    }

    private boolean isDiscardedResCorrect(List<Resource> resources, List<Resource> toDiscard){ //TODO: da controllare
        if(toDiscard.size()>resources.size())
            return false;
        List<Resource> resourcesCopy = getResourcesOneByOne(resources);
        int found;
        for (Resource resource : toDiscard){
            found = -1;
            for(int i=0;i<resourcesCopy.size();i++){
                if(resource.getResourceType().equals(resourcesCopy.get(i).getResourceType())){
                    found=i;
                    break;
                }
            }
            if(found==-1)
                return false;
            resourcesCopy.remove(found);
        }
        return true;
    }

    private void placeResourcesOnShelves(List<Resource> resources, boolean leaderShelfActive){
        //TODO: gestire così il parametro va bene?
        //TODO: da completare, è un casino :)
        //sendShelvesConfiguration();
    }

    public void selectMarket() {
        graphicalCLI.printMarket(marketView);
        if(askGoBack())
            return;
        graphicalCLI.printString("Where do you want to place the marble?\n" +
                "Choose R (row) or C (column) followed by a number: \n");

        boolean valid;
        do {
            int row = -1, column = -1;
            valid = true;

            String choice = scanner.next();

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
                messageHandler.sendMessage(new SelectMarketMessage(row, column));
            else
                graphicalCLI.printString("Your choice is invalid, please try again\n");
        } while(!valid);
        mainActionPlayed = true;
    }

    private void selectDevDecks() { //TODO: dividere in selezione carta (aspettando ack) e selezione spazio?

        printDevelopmentDeckTop();
        if(askGoBack())
            return;
        graphicalCLI.printString("Which card do you want to buy?\nChoose B (blue), G (green), P (purple) or Y (yellow)" +
                " followed by a number corresponding to its level: ");

        DevelopmentCard developmentCard = chooseCardFromDecks();
        int space = chooseDevCardSpace(developmentCard.getLevel());
        //TODO: aggiungere controlli anche su risorse da spendere?

        cardToBuy = new DevelopmentCard(developmentCard.getID(),developmentCard.getVP(),developmentCard.getColor(),
                developmentCard.getLevel(),developmentCard.getProduction(),developmentCard.getCost());
        messageHandler.sendMessage(new BuyDevelopmentCardMessage(developmentCard, space));
        mainActionPlayed = true;
    }

    private DevelopmentCard chooseCardFromDecks() {
        boolean valid;
        String choice;
        int level;
        List<DevelopmentCard> activeCards = getActiveCardsInSpaces(nickname);
        //TODO: va bene? non verrà mai utilizzato. Oppure costruttore vuoto?
        DevelopmentCard developmentCard = new DevelopmentCard(-1,0,null,-1,null,null);
        do {
            valid = false;
            choice = scanner.next();

            if(choice.matches("[BGPYbgpy][1-3]")) {
                String color = choice.substring(0, 1).toUpperCase();
                level = Integer.parseInt(choice.substring(1, 2));
                switch (color) {
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
                for (DevelopmentDeckView developmentDeck : developmentDecks) {
                    if (developmentDeck.getDeckColor().equals(CardColors.valueOf(color)) &&
                            developmentDeck.getDeckLevel() == level) {
                        if(!developmentDeck.getDeck().isEmpty()) {
                            developmentCard = (DevelopmentCard) developmentDeck.getDeck().get(0);
                            valid = true;
                        }
                        else
                            graphicalCLI.printString("There's no more cards available from this deck, " +
                                    "please try again");
                        break;
                    }
                }
            }
            else
                graphicalCLI.printString("Invalid choice, please try again");
        } while (!valid);
        return developmentCard;
    }

    private int chooseDevCardSpace(int cardLevel) {
        int space = -1;
        boolean valid;
        try{
            List<Deck> spaces = playerBoardFromNickname(nickname).getDevelopmentBoard().getSpaces();
            graphicalCLI.printString("Which space do you want to put the card on? ");
            space = scanner.nextInt()-1;
            do{
                valid=true;
                while (space<0 || space>=3){
                    graphicalCLI.printString("Your choice is invalid, please try again ");
                    space = scanner.nextInt()-1;
                }
                if((cardLevel==1 && spaces.get(space).size()>0) || cardLevel >1 && (spaces.get(space).size()==0 ||
                        ((DevelopmentCard)spaces.get(space).get(0)).getLevel()>=cardLevel-1)){
                    graphicalCLI.printString("The selected slot has been already filled, please try again ");
                    space = scanner.nextInt()-1;
                    valid = false;
                }
            } while (!valid);

        }catch (NotExistingNickname e){
            e.printStackTrace();
        }
        spaceToPlace = space; //TODO: controllare: mi salvo lo space perchè serve da madnare al server per vedere se puo comprare (insieme alle request resources)
        return space;
    }

    public List<LeaderCard> chooseLeaderCard(){
        try {
            List<LeaderCard> hand = (List<LeaderCard>)(List<?>)playerBoardFromNickname(nickname).getLeaderBoard().getHand().getCards();
            graphicalCLI.printNumberedList(hand, graphicalCLI::printLeaderCard);
            graphicalCLI.printString("Choose a leader card by selecting the corresponding number: ");

            int index = scanner.nextInt() - 1;
            List<LeaderCard> temp = new ArrayList<>();
            temp.add(hand.get(index));
            return temp;
        }catch(NotExistingNickname e){
            e.printStackTrace();
        }
        return null; //TODO: fa schifo tornare null
    }


    private List<DevelopmentCard> getActiveCardsInSpaces(String nickname) { //TODO: da testare quando funzionerà tutto e si sarà comprata qualche carta
        List<DevelopmentCard> activeSpaces = new ArrayList<>();
        try{
             List<Deck> playerSpaces = playerBoardFromNickname(nickname).getDevelopmentBoard().getSpaces();
             for(Deck deck : playerSpaces){
                 if(deck.size()>0)
                    activeSpaces.add((DevelopmentCard) deck.get(0));
             }
        }catch (NotExistingNickname e){
            e.printStackTrace();
        }
        return activeSpaces;
    }

    public void selectProductions() {
        try {
            DevelopmentBoardView developmentBoard = playerBoardFromNickname(nickname).getDevelopmentBoard();
            List<Production> productions = new ArrayList<>();
            //TODO: gestire leader e basic production
            developmentBoard.getSpaces()
                    .forEach(d -> d.getCards()
                            .forEach(c -> productions.add(((DevelopmentCard) c).getProduction()))
                    );
            graphicalCLI.printString("Available productions:\n");
            graphicalCLI.printNumberedList(productions, graphicalCLI::printProduction);
            boolean endChoice = false;
            if(productions.size() > 0) {
                do {
                    int index;
                    boolean validIndex = true;
                    do {
                        graphicalCLI.printString("Choose a production you want to activate by entering its number: ");
                        index = scanner.nextInt() - 1;
                        if (index < 0 || index >= productions.size())
                            validIndex = false;
                    } while (!validIndex);

                    productionsToActivate.add(productions.get(index));

                    graphicalCLI.printString("Do you want to activate another production? ");
                    if (!isAnswerYes())
                        endChoice = true;
                } while (!endChoice);
                resolveProductionWildcards();
                messageHandler.sendMessage(new ActivateProductionsMessage(productionsToActivate));
            }
            else
                graphicalCLI.printString("No productions:\n");
        } catch(NotExistingNickname e) {
            e.printStackTrace();
        }
    }

    public void resolveProductionWildcards() {
        List<Production> resolvedProductions = new ArrayList<>();
        for(Production production : productionsToActivate) {
            List<Resource> consumedResolved =  production.getConsumed().stream()
                    .filter((r -> r.getResourceType() != ResourceType.WILDCARD)).collect(Collectors.toList());
            List<Resource> producedResolved =  production.getProduced().stream()
                    .filter((r -> r.getResourceType() != ResourceType.WILDCARD)).collect(Collectors.toList());
            List<Resource> consumedWildcards = production.getConsumed().stream()
                    .filter((r -> r.getResourceType() == ResourceType.WILDCARD)).collect(Collectors.toList());
            List<Resource> producedWildcards = production.getProduced().stream()
                    .filter((r -> r.getResourceType() == ResourceType.WILDCARD)).collect(Collectors.toList());

            if(consumedWildcards.size() > 0 || producedWildcards.size() > 0) {
                graphicalCLI.printString("Resolve wildcards for the following production:\n");
                graphicalCLI.printProduction(production);
                if(consumedWildcards.size() > 0) {
                    graphicalCLI.printString("Choose for consumed wildcards:\n");
                    for (Resource wildcard : consumedWildcards) {
                        ResourceType chosenType = resourceTypeSelector(Arrays.asList(ResourceType.values().clone()));
                        consumedResolved.add(new Resource(chosenType, wildcard.getQuantity()));
                    }
                }
                if(producedWildcards.size() > 0) {
                    graphicalCLI.printString("Choose for produced wildcards:\n");
                    for (Resource wildcard : producedWildcards) {
                        ResourceType chosenType = resourceTypeSelector(Arrays.asList(ResourceType.values().clone()));
                        producedResolved.add(new Resource(chosenType, wildcard.getQuantity()));
                    }
                }
            }
            resolvedProductions.add(new Production(consumedResolved, producedResolved));
        }
        productionsToActivate = resolvedProductions;
    }

    public void turnMenu(boolean isPlayerTurn) { //TODO: gestire per far fare comunque altre azioni
        int action;
        goBack = false;
        endTurn = false;
        refresh();
        if(isPlayerTurn) {
            graphicalCLI.printActions();
            if (goBack) {
                graphicalCLI.printString("Choose another action to do on your turn: ");
                goBack = false;
            }
            do {
                action = scanner.nextInt();
            } while (action < 1 || action > 6);

            switch (action) {
                case 1:
                    if (!mainActionPlayed)
                        selectMarket();
                    else {
                        graphicalCLI.printString("You can't play this action on your turn anymore\n");
                        goBack = true;
                    }
                    break;
                case 2:
                    if (!mainActionPlayed){
                        selectDevDecks();
                        List<RequestResources> temp = chooseStorages(cardToBuy.getCost());  //TODO: Ste: invio al server le risorse, va testato
                        messageHandler.sendMessage(new RequestResourcesDevMessage(cardToBuy,spaceToPlace,temp)); //TODO: manca il metodo che riceve ack, quindi si blocca
                    }
                    else {
                        graphicalCLI.printString("You can't play this action on your turn anymore\n");
                        goBack = true;
                    }
                    break;
                case 3:
                    if (!mainActionPlayed)
                        selectProductions();
                    else {
                        graphicalCLI.printString("You can't play this action on your turn anymore\n");
                        goBack = true;
                    }

                    break;
                case 4:
                    messageHandler.sendMessage(new LeaderCardPlayMessage(chooseLeaderCard()));
                    break;
                case 5: messageHandler.sendMessage(new LeaderCardDiscardMessage(chooseLeaderCard()));
                    break;
                case 6:
                    if (mainActionPlayed)
                        endTurn = true;
                    break;
                default: //boh, default non lo farò mai :)
                    break;
            }
        }
    }

    private boolean askGoBack() {
        graphicalCLI.printString("Do you want to go back and choose another action?\nIf you want to, insert YES: ");
        if(isAnswerYes()){
            goBack = true;
            return true;
        }
        return false;
    }

    public List<RequestResources> chooseStorages(List<Resource> resources) {

        try {
            PlayerBoardView playerBoard = playerBoardFromNickname(nickname);
            graphicalCLI.printWarehouse(playerBoard.getWarehouse());
            graphicalCLI.printExtraShelfLeader(playerBoardFromNickname(nickname).getWarehouse());
            graphicalCLI.printStrongbox(playerBoardFromNickname(nickname).getStrongbox());
        }catch(NotExistingNickname e){
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
                choice = scanner.nextInt();
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
  
    private boolean isAnswerYes() {
        String command = scanner.next();
        return  command.equalsIgnoreCase("YES") || command.equalsIgnoreCase("Y");
    }

    private void printDevelopmentDeckTop() {
        List<DevelopmentCard> developmentCards =  new ArrayList<>();
        for(DevelopmentDeckView temp : developmentDecks){
            if(!temp.getDeck().isEmpty())
                developmentCards.add((DevelopmentCard) temp.getDeck().getCards().get(0));
        }
        graphicalCLI.printNumberedList(developmentCards, graphicalCLI::printDevelopmentCard);
    }
}
