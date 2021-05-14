package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.client.ClientController;
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

public class CLI extends ClientController {
    private final Scanner scanner;
    private final GraphicalCLI graphicalCLI;
    private final MessageHandler messageHandler;

    private boolean goBack;


    public CLI() {
        super();
        scanner = new Scanner(System.in);
        graphicalCLI = new GraphicalCLI();
        messageHandler = new MessageHandler(this);
        goBack = false;
    }


    public GraphicalCLI getGraphicalCLI() {
        return graphicalCLI;
    }

    public MessageHandler getPacketHandler() {
        return messageHandler;
    }

    public int getNextInt() { //TODO: bruttino?
        int value;
        String input = scanner.next();
        while (!(input.matches("^[0-9]*$") && input.length() > 0)){
            graphicalCLI.printString("Invalid input! Try again: ");
            input = scanner.next();
        }
        value = Integer.parseInt(input);
        return value;
    }

    public void setup() {
        //TODO: busy wait
        while(!connect());
        new Thread(messageHandler).start();
        graphicalCLI.printString("Insert your nickname: ");
        askNickname();
    }

    public void run() {
        Queue<ServerActionMessage> messageQueue = messageHandler.getQueue();
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        while(true) {
            try {
                if (br.ready()) //TODO: temporaneo
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
        setNickname(scanner.next());
        messageHandler.sendMessage(new ConnectionMessage(getNickname()));
    }

    public void createNewLobby() {
        int size;
        graphicalCLI.printString("There isn't any player waiting for a match!\n");
        do {
            graphicalCLI.printString("Insert the number of players that will play the game " +
                    "(value inserted must between 1 and 4): ");
            size = getNextInt();
        }while(size <= 0 || size >= 5);
        setNumberOfPlayers(size);
        messageHandler.sendMessage(new NewLobbyMessage(size));
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
                index = getNextInt()-1;
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
                index = getNextInt()-1;
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
        graphicalCLI.printMarket(getMarketView()); graphicalCLI.printString("\n");
        printDevelopmentDeckTop(); graphicalCLI.printString("\n");
        try {
            PlayerBoardView playerBoard = getLocalPlayerBoard();
            graphicalCLI.printFaithBoard(playerBoard, getFaithTrackView()); graphicalCLI.printString("\n");
            graphicalCLI.printDevelopmentBoard(); graphicalCLI.printString("\n");
            graphicalCLI.printWarehouse(playerBoard.getWarehouse());
            graphicalCLI.printExtraShelfLeader(playerBoard.getWarehouse()); graphicalCLI.printString("\n");
            graphicalCLI.printStrongbox(playerBoard.getStrongbox()); graphicalCLI.printString("\n");
        }catch(NotExistingNickname e){
            e.printStackTrace();
        }
    }

    public void storeTempResources(List<Resource> resourcesToMemorize) {    //TODO: da rimuovere
        setResourcesToPut(new ArrayList<>(resourcesToMemorize));
    }

    public void tryToPlaceShelves() { //TODO: decidere visibility (anche x altri try)
        graphicalCLI.printString("The selected configuration is invalid\n" +
                "Please, try again to place on the shelves:\n");
        placeResourcesOnShelves(getResourcesToPut());
    }

    private void tryAgainToBuyCard() {
        graphicalCLI.printString("The selected configuration is invalid\n");
        //TODO: da fare
        if(askGoBack()) //TODO: serve o obbligato?
            turnMenu(true);
        else { }
    }

    private void tryAgainToActivateProduction() {
        graphicalCLI.printString("The selected configuration is invalid\n");
        //TODO: da fare
        if(askGoBack()) //TODO: serve o obbligato?
            turnMenu(true);
        else { }
    }

    private boolean isLeaderShelfActive() {//TODO: serve ancora?
        List<Shelf> shelves = new ArrayList<>();
        try{
            shelves = getLocalPlayerBoard().getWarehouse().getShelves();
        }catch (NotExistingNickname e){
            e.printStackTrace();
        }
        return shelves.stream().anyMatch(Shelf::isLeader);
    }

    public void placeResourcesOnShelves(List<Resource> resources){
        List<Shelf> shelves;
        List<Resource> toDiscard = new ArrayList<>();
        Shelf selectedShelf;
        Resource resourceToPlace;
        boolean rearranged = false;
        int level;

        try{
            List<Resource> toPlace = getResourcesOneByOne(resources);
            moveFaith(toDiscard, toPlace);

            WarehouseView warehouse = getLocalPlayerBoard().getWarehouse();
            shelves = getShelvesWarehouseCopy(warehouse.getShelves());
            graphicalCLI.printWarehouseConfiguration(warehouse);
            if(!toPlace.isEmpty()) {
                graphicalCLI.printString("Resources to place: ");
                graphicalCLI.printGraphicalResources(toPlace);

                if (checkFreeSlotInWarehouse()) { //it's possible to place resources
                    if (!isShelvesEmpty(shelves)) { //before starting move resources
                        graphicalCLI.printString("Do you want to rearrange the warehouse? ");
                        rearranged = isAnswerYes();
                        if(rearranged){
                            shelves = getShelvesWarehouseCopy(rearrangeWarehouse());
                        }
                    }

                    while (!toPlace.isEmpty()) {
                        if (rearranged) {
                            graphicalCLI.printWarehouseConfiguration(new WarehouseView(shelves));
                            graphicalCLI.printString("Resources to place: ");
                            graphicalCLI.printGraphicalResources(toPlace);
                        }
                        resourceToPlace = toPlace.get(0);
                        level = askWhichShelf(resourceToPlace, shelves.size(), rearranged, true);
                        rearranged = true;

                        if (level > 0) {
                            selectedShelf = shelves.get(level - 1);
                            if (selectedShelf.getResourceType().equals(ResourceType.WILDCARD)) //empty shelf
                                emptyShelfManagement(shelves, toPlace, selectedShelf, resourceToPlace);
                            else if (selectedShelf.getResourceType().equals(resourceToPlace.getResourceType())) //shelf with the same resource type
                                sameResTypeShelfManagement(shelves, toPlace, selectedShelf, resourceToPlace);
                            else if (!selectedShelf.isLeader()) //shelf with different resource type
                                differentResTypeShelfManagement(shelves, toPlace, selectedShelf, resourceToPlace);
                            else graphicalCLI.printString("You can't place this resource here\n");
                        } else if (level == 0) {
                            toDiscard.add(new Resource(resourceToPlace.getResourceType(), resourceToPlace.getQuantity()));
                            graphicalCLI.printString("Resource discarded\n");
                            toPlace.remove(0);
                        } else if (level < 0) {
                            restoreConfiguration(warehouse, shelves, resources, toPlace, toDiscard, true);
                            rearranged = false;
                            graphicalCLI.printWarehouseConfiguration(new WarehouseView(shelves));
                            graphicalCLI.printString("Resources to place: ");
                            graphicalCLI.printGraphicalResources(toPlace);
                        }

                        if (toPlace.isEmpty() && !isDiscardedResCorrect(resources, toDiscard)) {
                            graphicalCLI.printString("You're trying to discard resources already stored in the" +
                                    " warehouse!\nThe warehouse will be restored and you'll be asked to place all the" +
                                    " resources again...\n");
                            restoreConfiguration(warehouse, shelves, resources, toPlace, toDiscard, true);
                            rearranged = false;
                            graphicalCLI.printWarehouseConfiguration(new WarehouseView(shelves));
                            graphicalCLI.printString("Resources to place: ");
                            graphicalCLI.printGraphicalResources(toPlace);
                        }
                    }
                } else {
                    graphicalCLI.printString("There are no available slots, all the resources will be discarded\n");
                    toDiscard.addAll(toPlace);
                }
            } else graphicalCLI.printString("There are no resources to place\n");
            messageHandler.sendMessage(new ShelvesConfigurationMessage(shelves, toDiscard));
        }catch (NotExistingNickname e){
            e.printStackTrace();
        }
    }

    public void moveFaith(List<Resource> dest, List<Resource> src){
        for(int i=0; i<src.size(); i++)
            if(src.get(i).getResourceType().equals(ResourceType.FAITH)){
                dest.add(src.get(i));
                src.remove(i);
                break;
            }
    }

    private boolean checkFreeSlotInWarehouse() {
        try {
            if(getLocalPlayerBoard().getWarehouse().getShelves().size()<3)
                return true;
            for (Shelf shelf : getLocalPlayerBoard().getWarehouse().getShelves()) {
                if (shelf.getResourceType().equals(ResourceType.WILDCARD) ||
                        shelf.getLevel() > shelf.getResources().getQuantity())
                    return true;
            }
        } catch (NotExistingNickname e){
            e.printStackTrace();
        }
        return false;
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

            for (int i=0; i<shelves.size(); i++) {
                resources.add(new Resource(shelves.get(i).getResourceType(),shelves.get(i).getResources().getQuantity()));
                shelves.get(i).getResources().setQuantity(0);
                if(!shelves.get(i).isLeader()){
                    shelves.get(i).getResources().setResourceType(ResourceType.WILDCARD);
                    shelves.get(i).setResourceType(ResourceType.WILDCARD);
                }
            }
            temporaryWarehouseShelves = getShelvesWarehouseCopy(shelves);

            List<Resource> toPlace = getResourcesOneByOne(resources);
            boolean firstTurn = true;
            while (!toPlace.isEmpty()){
                resourceToPlace = toPlace.get(0);
                graphicalCLI.printWarehouseConfiguration(new WarehouseView(shelves));
                graphicalCLI.printString("Resources to place: ");
                graphicalCLI.printGraphicalResources(toPlace);

                level=askWhichShelf(resourceToPlace, shelves.size(), !firstTurn, false);
                if(firstTurn) firstTurn = !firstTurn;

                if(level>0) {
                    selectedShelf = shelves.get(level - 1);
                    if (selectedShelf.getResourceType().equals(ResourceType.WILDCARD)) //empty shelf
                        emptyShelfManagement(shelves, toPlace, selectedShelf, resourceToPlace);
                    else if (selectedShelf.getResourceType().equals(resourceToPlace.getResourceType())) //shelf with the same resource type
                        sameResTypeShelfManagement(shelves, toPlace, selectedShelf, resourceToPlace);
                    else if(!selectedShelf.isLeader()) //shelf with different resource type
                        differentResTypeShelfManagement(shelves, toPlace, selectedShelf, resourceToPlace);
                    else graphicalCLI.printString("You can't place this resource here\n");
                }
                else if(level<0) {
                    restoreConfiguration(new WarehouseView(temporaryWarehouseShelves), shelves, resources, toPlace,
                            null, false);
                }
            }
            return shelves;
        } catch (NotExistingNickname e){
            e.printStackTrace();
        }
        return null; //TODO: :(
    }

    private boolean isShelvesEmpty(List<Shelf> shelves){
        for (Shelf shelf : shelves)
            if(shelf.getResources().getQuantity()>0)
                return false;
        return true;
    }

    private boolean isWarehouseEmpty(){
        try {
            return isShelvesEmpty(getLocalPlayerBoard().getWarehouse().getShelves());
        } catch (NotExistingNickname e){
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

    private int askWhichShelf(Resource resource, int numberOfShelves, boolean rearranged, boolean canDiscard) {
        int level;
        if(rearranged) {
            graphicalCLI.printString("Do you want to restore warehouse to its original configuration? ");
            if (isAnswerYes())
                return -1;
        }
        graphicalCLI.printString("Where do you want to place " + resource.getResourceType()
                + (canDiscard ? "? (0 to discard it) " : "? "));
        level = getNextInt();
        while ((canDiscard && level<0) || (!canDiscard && level<=0) || level>numberOfShelves){
            graphicalCLI.printString("Choose a valid shelf: ");
            level = getNextInt();
        }
        return level;
    }

    private void emptyShelfManagement(List<Shelf> shelves, List<Resource> toPlace,
                                      Shelf selectedShelf, Resource resourceToPlace) {
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

    private void sameResTypeShelfManagement(List<Shelf> shelves, List<Resource> toPlace,
                                            Shelf selectedShelf, Resource resourceToPlace){
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
                    resetShelf(selectedShelf);
                }
            }
        }
    }

    private void differentResTypeShelfManagement(List<Shelf> shelves, List<Resource> toPlace,
                                                 Shelf selectedShelf, Resource resourceToPlace) {
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

    private boolean isShelfRearrangeable(List<Shelf> shelves, Resource resource){ //TODO: nome da cambiare?
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
        graphicalCLI.printString("Warehouse restored\n");
    }

    private boolean isDiscardedResCorrect(List<Resource> resources, List<Resource> toDiscard){
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

    public void selectMarket() {
        graphicalCLI.printMarket(getMarketView());
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
        setMainActionPlayed(true);
    }

    private void selectDevDecks() {
        printDevelopmentDeckTop();

        if(askGoBack())
            return;

        DevelopmentCard developmentCard = chooseCardFromDecks();
        setSpaceToPlace(chooseDevCardSpace());

        setCardToBuy(new DevelopmentCard(developmentCard.getID(),developmentCard.getVP(),developmentCard.getColor(),
                developmentCard.getLevel(),developmentCard.getProduction(),developmentCard.getCost()));
        messageHandler.sendMessage(new BuyDevelopmentCardMessage(getCardToBuy(), getSpaceToPlace())); //TODO: è più un CANBuyDevelopmentCardMessage?
        //setMainActionPlayed(true); TODO: mettere variabile a true solo quando si è certi che la carta si può comprare, altrimenti richiedere o tornare indietro (fare in un altro metodo)
    }

    private DevelopmentCard chooseCardFromDecks() {
        boolean valid;
        String choice;
        int level;
        //TODO: va bene? non verrà mai utilizzato. Oppure costruttore vuoto?
        DevelopmentCard developmentCard = new DevelopmentCard(-1,0,null,-1,null,null);
        graphicalCLI.printString("Which card do you want to buy?\nChoose B (blue), G (green), P (purple) " +
                "or Y (yellow) followed by a number corresponding to its level: ");
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
                for (DevelopmentDeckView developmentDeck : getDevelopmentDecks()) {
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

    private int chooseDevCardSpace() {
        int space;
        graphicalCLI.printString("Which space do you want to put the card on? ");

        space = getNextInt()-1;
        while (space<0 || space>=3){
            graphicalCLI.printString("Your choice is invalid, please try again ");
            space = getNextInt()-1;
        }

        setSpaceToPlace(space);
        return space;
    }

    public List<LeaderCard> chooseLeaderCard(){ //TODO: gestire caso non ho leader
        try {
            List<LeaderCard> hand = (List<LeaderCard>)(List<?>) getLocalPlayerBoard().getLeaderBoard().getHand().getCards();
            graphicalCLI.printNumberedList(hand, graphicalCLI::printLeaderCard);
            graphicalCLI.printString("Choose a leader card by selecting the corresponding number: ");

            int index = getNextInt() - 1;
            List<LeaderCard> temp = new ArrayList<>();
            temp.add(hand.get(index));
            return temp;
        }catch(NotExistingNickname e){
            e.printStackTrace();
        }
        return null; //TODO: fa schifo tornare null
    }

    private List<DevelopmentCard> getActiveCardsInSpaces(String nickname) { //TODO: non serve più?
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
            DevelopmentBoardView developmentBoard = getLocalPlayerBoard().getDevelopmentBoard();
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
                        index = getNextInt() - 1;
                        if (index < 0 || index >= productions.size())
                            validIndex = false;
                    } while (!validIndex);

                    getProductionsToActivate().add(productions.get(index));

                    graphicalCLI.printString("Do you want to activate another production? ");
                    if (!isAnswerYes())
                        endChoice = true;
                } while (!endChoice);
                resolveProductionWildcards();
                messageHandler.sendMessage(new ActivateProductionsMessage(getProductionsToActivate()));
            }
            else
                graphicalCLI.printString("No productions\n");
        } catch(NotExistingNickname e) {
            e.printStackTrace();
        }
        //setMainActionPlayed(true); TODO: mettere variabile a true solo quando si è certi che la carta si può attivare, altrimenti richiedere o tornare indietro (fare in un altro metodo)
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
        setProductionsToActivate(resolvedProductions);
    }

    public void turnMenu(boolean isPlayerTurn) { //TODO: gestire per far fare comunque altre azioni
        int action;
        //goBack = false;
        //endTurn = false;
        refresh();
        if(isPlayerTurn) {
            graphicalCLI.printActions();
            /*if (goBack) {
                graphicalCLI.printString("Choose another action to do on your turn: ");
                goBack = false;
            }*/
            action = getNextInt();
            while (action < 1 || action > 8){
                graphicalCLI.printString("Invalid choice, please try again: ");
                action = getNextInt();
            }

            switch (action) {
                case 1:
                    if (!isMainActionPlayed())
                        selectMarket();
                    else {
                        graphicalCLI.printString("You can't play this action on your turn anymore\n");
                    }
                    //goBack = true;
                    break;
                case 2:
                    if (!isMainActionPlayed()) {
                        selectDevDecks();
                        if (isMainActionPlayed()) { //TODO: da spostare in un doAction (togliendo if e controllando ack)
                            chooseStorages(getCardToBuy().getCost());  //TODO: Ste: invio al server le risorse, va testato
                        }
                    }
                    else {
                        graphicalCLI.printString("You can't play this action on your turn anymore\n");
                    }
                    //goBack = true;
                    break;
                case 3:
                    if (!isMainActionPlayed())
                        selectProductions();
                    else {
                        graphicalCLI.printString("You can't play this action on your turn anymore\n");
                    }
                    //goBack = true;
                    break;
                case 4:
                    messageHandler.sendMessage(new LeaderCardPlayMessage(chooseLeaderCard())); //TODO: controllare se non ho leader da attivare
                    //goBack = true;
                    break;
                case 5: messageHandler.sendMessage(new LeaderCardDiscardMessage(chooseLeaderCard())); //TODO: controllare se non ho leader da scartare
                    //goBack = true;
                    break;
                case 6:
                    if(isWarehouseEmpty()){
                        graphicalCLI.printString("You have no resources to rearrange\n");
                    } else {
                        messageHandler.sendMessage(new ShelvesConfigurationMessage(
                                rearrangeWarehouse(), new ArrayList<>()));
                    }
                    //goBack = true;
                        //TODO: messaggio per confermare config?
                    break;
                case 7:
                    showOpponents(); //TODO: caso single?
                    //goBack = true;
                    break;
                case 8:
                    if (isMainActionPlayed())
                        //endTurn = true; TODO: gestire fine turno
                        break;
                default: //boh, default non lo farò mai :)
                    break;
            }
        }
    }

    private boolean askGoBack() {
        graphicalCLI.printString("Do you want to go back and choose another action?" +
                "\nIf you want to, insert YES: ");
        if(isAnswerYes()){
            goBack = true;
            return true;
        }
        return false;
    }

    public void chooseStorages(List<Resource> resources) {
        try {
            PlayerBoardView playerBoard = getLocalPlayerBoard();
            graphicalCLI.printWarehouse(playerBoard.getWarehouse());
            graphicalCLI.printExtraShelfLeader(getLocalPlayerBoard().getWarehouse());
            graphicalCLI.printStrongbox(getLocalPlayerBoard().getStrongbox());
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
                choice = getNextInt();
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

        messageHandler.sendMessage(new RequestResourcesDevMessage(getCardToBuy(), getSpaceToPlace(), requestResources)); //TODO: manca il metodo che riceve ack, quindi si blocca
    }
  
    private boolean isAnswerYes() {
        String command = scanner.next();
        return  command.equalsIgnoreCase("YES") || command.equalsIgnoreCase("Y");
    }

    private void printDevelopmentDeckTop() { //TODO: da mettere nella graphic?
        graphicalCLI.printString("The development decks:\n");
        List<DevelopmentCard> developmentCards =  new ArrayList<>();
        for(DevelopmentDeckView temp : getDevelopmentDecks()){
            if(!temp.getDeck().isEmpty())
                developmentCards.add((DevelopmentCard) temp.getDeck().getCards().get(0));
        }
        graphicalCLI.printNumberedList(developmentCards, graphicalCLI::printDevelopmentCard);
    }

    public void showOpponents(){
        for(PlayerBoardView playerBoardView : getPlayerBoards()){
            if(!playerBoardView.getNickname().equals(getNickname())){
                graphicalCLI.printOpponent(playerBoardView,getFaithTrackView());
            }
        }
    }
}
