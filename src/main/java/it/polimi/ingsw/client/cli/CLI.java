package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.client.structures.*;
import it.polimi.ingsw.exceptions.NotExistingNickname;
import it.polimi.ingsw.server.Server;
import it.polimi.ingsw.server.model.cards.ability.*;
import it.polimi.ingsw.server.model.cards.card.CardColors;
import it.polimi.ingsw.server.model.cards.card.DevelopmentCard;
import it.polimi.ingsw.server.model.cards.deck.Deck;
import it.polimi.ingsw.server.model.storage.*;
import it.polimi.ingsw.utils.messages.Message;
import it.polimi.ingsw.utils.messages.client.*;
import it.polimi.ingsw.utils.messages.server.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;

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
    private List<Production> productionsToActivate;
    private final Scanner scanner;
    private boolean goBack, mainActionPlayed, endTurn;
    private final GraphicalCLI graphicalCLI;
    private final PacketHandler packetHandler;

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
        packetHandler = new PacketHandler(this);
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

    public PacketHandler getPacketHandler() {
        return packetHandler;
    }

    public int getNextInt() {
        return scanner.nextInt();
    }

    public void setup() {
        while(!connect());
        graphicalCLI.printString("Insert your nickname\n");
        askNickname();
    }

    public void run() {
        Queue<Message> messageQueue = packetHandler.getQueue();
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        while(true) {
            try {
                if (br.ready())
                    turnMenu(true);
                else if (messageQueue.size() > 0)
                    ((ServerActionMessage) messageQueue.remove()).doAction(this);
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    private boolean connect() {
        graphicalCLI.printString("Insert the IP address of server\n");
        String ip = scanner.nextLine();

        return packetHandler.start(ip,Server.SOCKET_PORT);
    }

    public void askNickname() {
        nickname = scanner.next();
        packetHandler.sendMessage(new ConnectionMessage(nickname));
    }

    public void createNewLobby(){
        int size;
        graphicalCLI.printString("There isn't any player waiting for a match!\n");
        do {
            graphicalCLI.printString("Insert the number of players that will play the game " +
                    "(value inserted must between 1 and 4)\n");
            size = scanner.nextInt();
        }while(size <= 0 || size >= 5);
        setNumberOfPlayers(size);
        packetHandler.sendMessage(new NewLobbyMessage(size));
    }

    public void setNumberOfPlayers(int numberOfPlayers){
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

    public List<Resource> resolveResourcesToEqualize(int wildcardQuantity){ //TODO: sarà chiamato una sola volta per equalizzare
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

    private void refresh(String nickname){
        //TODO: da fare
        graphicalCLI.printMarket(marketView);
        graphicalCLI.printString("\nThe development decks:\n");
        graphicalCLI.printDevelopmentDecks(developmentDecks);
        try {
            PlayerBoardView playerBoard = playerBoardFromNickname(nickname);
            graphicalCLI.printFaithBoard(playerBoard,faithTrackView);
            //developmentboard
            graphicalCLI.printWarehouse(playerBoard.getWarehouse());
            graphicalCLI.printExtraShelfLeader(playerBoard.getWarehouse());
            graphicalCLI.printStrongbox(playerBoard.getStrongbox());
        }catch(NotExistingNickname e){
            e.printStackTrace();
        }
    }

    private List<AbilityDiscount> getActiveAbilityDiscount(){
        List<AbilityDiscount> leaderAbility = new ArrayList<>();
        try {
            LeaderBoardView leaderBoard = playerBoardFromNickname(nickname).getLeaderBoard();
            for(int i=0;i<leaderBoard.getBoard().size();i++){
                if(leaderBoard.getBoard().get(i) instanceof AbilityDiscount) //TODO:instanceof (uguale anche x altre get di ability)
                    leaderAbility.add((AbilityDiscount) leaderBoard.getBoard().get(i));
            }
        }catch (NotExistingNickname e){
            e.printStackTrace();
        }
        return leaderAbility;
    }

    private List<AbilityMarble> getActiveAbilityMarble(){
        List<AbilityMarble> leaderAbility = new ArrayList<>();
        try {
            LeaderBoardView leaderBoard = playerBoardFromNickname(nickname).getLeaderBoard();
            for(int i=0;i<leaderBoard.getBoard().size();i++){
                if(leaderBoard.getBoard().get(i) instanceof AbilityMarble)
                    leaderAbility.add((AbilityMarble) leaderBoard.getBoard().get(i));
            }
        }catch (NotExistingNickname e){
            e.printStackTrace();
        }
        return leaderAbility;
    }

    private List<AbilityProduction> getActiveAbilityProduction(){
        List<AbilityProduction> leaderAbility = new ArrayList<>();
        try {
            LeaderBoardView leaderBoard = playerBoardFromNickname(nickname).getLeaderBoard();
            for(int i=0;i<leaderBoard.getBoard().size();i++){
                if(leaderBoard.getBoard().get(i) instanceof AbilityProduction)
                    leaderAbility.add((AbilityProduction) leaderBoard.getBoard().get(i));
            }
        }catch (NotExistingNickname e){
            e.printStackTrace();
        }
        return leaderAbility;
    }

    private List<AbilityWarehouse> getActiveAbilityWarehouse(){
        List<AbilityWarehouse> leaderAbility = new ArrayList<>();
        try {
            LeaderBoardView leaderBoard = playerBoardFromNickname(nickname).getLeaderBoard();
            for(int i=0;i<leaderBoard.getBoard().size();i++){
                if(leaderBoard.getBoard().get(i) instanceof AbilityWarehouse)
                    leaderAbility.add((AbilityWarehouse) leaderBoard.getBoard().get(i));
            }
        }catch (NotExistingNickname e){
            e.printStackTrace();
        }
        return leaderAbility;
    }

    public void storeTempResources(List<Resource> resourcesToMemorize){
        resourcesToPut = new ArrayList<>(resourcesToMemorize);
    }

    private void storeTempCard(DevelopmentCard devCardToMemorize){ //TODO: valutare se aggiungere costruttore apposito
        cardToBuy = new DevelopmentCard(devCardToMemorize.getID(),devCardToMemorize.getVP(),devCardToMemorize.getColor(),
                devCardToMemorize.getLevel(),devCardToMemorize.getProduction(),devCardToMemorize.getCost());
    }

    private void storeTempProduction(List<Production> productionsToMemorize){
        productionsToActivate = new ArrayList<>(productionsToMemorize);
    }

    public void tryToPlaceShelves(){ //TODO: decidere visibility (anche x altri try)
        graphicalCLI.printString("The selected configuration is invalid\n");
        if(askGoBack())
            turnMenu(true);
        else {
            graphicalCLI.printString("Please, try again to place on the shelves:\n");
            chooseShelvesManagement(resourcesToPut);
        }
    }

    private void tryAgainToBuyCard(){
        graphicalCLI.printString("The selected configuration is invalid\n");
        //TODO: da fare
        if(askGoBack())
            turnMenu(true);
        else { }
    }

    private void tryAgainToActivateProduction(){
        graphicalCLI.printString("The selected configuration is invalid\n");
        //TODO: da fare
        if(askGoBack())
            turnMenu(true);
        else { }
    }

    public void chooseShelvesManagement(List<Resource> resources){ //TODO: x controllare se si hanno o meno i leader
        try {
            PlayerBoardView player = playerBoardFromNickname(nickname);
            graphicalCLI.printWarehouse(player.getWarehouse());

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

    private boolean isLeaderShelfActive(){
        List<Shelf> shelves = new ArrayList<>();
        try{
            shelves = playerBoardFromNickname(nickname).getWarehouse().getShelves();
        }catch (NotExistingNickname e){
            e.printStackTrace();
        }
        return shelves.stream().anyMatch(Shelf::IsLeader);
    }

    private void placeResourcesOnShelves(List<Resource> resources){
        //TODO: da completare, è un casino :) (WIP)
        WarehouseView warehouse;
        List<Shelf> shelves = new ArrayList<>();
        List<Resource> toPlace;
        List<Resource> toDiscard = new ArrayList<>();
        Shelf selectedShelf;
        Resource resourceToPlace;
        int level;

        try{
            if(checkFreeSlotInWarehouse()){ //it's possible to place resources
                rearrangeWarehouse();

                warehouse = playerBoardFromNickname(nickname).getWarehouse();
                shelves = getShelvesWarehouseCopy(warehouse.getShelves());
                toPlace = getResourcesOneByOne(resources);

                while (!toPlace.isEmpty()){
                    resourceToPlace = toPlace.get(0);
                    graphicalCLI.printWarehouse(new WarehouseView(shelves)); //temp warehouse

                    level=askWhichShelf(resourceToPlace, shelves.size());

                    if(level>0) {
                        selectedShelf = shelves.get(level - 1);

                        if (selectedShelf.getResourceType().equals(ResourceType.WILDCARD)) { //empty shelf
                            emptyShelfManagement(shelves, toPlace, selectedShelf, resourceToPlace);
                        }
                        else if (selectedShelf.getResourceType().equals(resourceToPlace.getResourceType())) { //shelf with the same resource type
                            sameResTypeShelfManagement(toPlace, selectedShelf, resourceToPlace);
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
                        shelves = getShelvesWarehouseCopy(warehouse.getShelves());
                        toPlace = getResourcesOneByOne(resources);
                        toDiscard.clear();
                        graphicalCLI.printString("Warehouse restored\n");
                    }
                }
            }
            else{
                graphicalCLI.printString("There are no available slots, all the resources will be discarded\n");
                toDiscard = resources;
            }

            //TODO: controlla res da scartare

            packetHandler.sendMessage(new ShelvesConfigurationMessage(shelves, toDiscard));

        }catch (NotExistingNickname e){
            e.printStackTrace();
        }
    }

    private boolean checkFreeSlotInWarehouse(){
        try {
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

    private void rearrangeWarehouse(){
        graphicalCLI.printString("Do you want to rearrange the warehouse? ");
        if(isAnswerYes()){
            //TODO: da fare
        }
    }

    private List<Shelf> getShelvesWarehouseCopy(List<Shelf> warehouse){
        List<Shelf> shelves = new ArrayList<>();
        for(Shelf shelf : warehouse)
            shelves.add(new Shelf(shelf.getResourceType(), shelf.getResources(), shelf.getLevel(), shelf.IsLeader()));
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

    private int askWhichShelf(Resource resource, int numberOfShelves){
        int level;
        graphicalCLI.printString("Do you want to restore warehouse to its original configuration? ");
        if(isAnswerYes())
            return -1;
        graphicalCLI.printString("Where do you want to place " + resource.getResourceType()
                + "? (0 to discard it) ");
        level = scanner.nextInt();
        while (level<0 || level>numberOfShelves){
            graphicalCLI.printString("Choose a valid shelf: ");
            level = scanner.nextInt();
        }
        return level;
    }

    private void emptyShelfManagement(List<Shelf> shelves, List<Resource> toPlace, Shelf selectedShelf, Resource resourceToPlace){
        if(isResourceTypeUnique(shelves,resourceToPlace.getResourceType())) { //there are no shelves with the same resource type
            placeResource(selectedShelf, resourceToPlace);
            toPlace.remove(0);
        }
        else {//there are shelves with the same resource type
            graphicalCLI.printString("There's already another shelf with the same resource type\n");
            if(isShelfRearrangeable(resourceToPlace)) { //it's possible to rearrange shelves
                graphicalCLI.printString("If you want to place it here anyway, insert YES and " +
                        "then you'll place again the removed ones from the other shelf: ");
                if(isAnswerYes()) {
                    Shelf otherShelf = getShelfWithSameResource(resourceToPlace.getResourceType());
                    for (int i = 0; i < otherShelf.getResources().getQuantity(); i++) {
                        toPlace.add(1, new Resource(selectedShelf.getResourceType(), 1));
                    }
                    resetShelf(otherShelf);
                    placeResource(selectedShelf, resourceToPlace);
                    toPlace.remove(0);
                }
            }
        }
    }

    private void sameResTypeShelfManagement(List<Resource> toPlace, Shelf selectedShelf, Resource resourceToPlace){
        if (selectedShelf.getResources().getQuantity() <= selectedShelf.getLevel() - 1) { //shelf not completely full
            placeResource(selectedShelf, resourceToPlace);
            toPlace.remove(0);
        }
        else { //shelf completely full
            graphicalCLI.printString("The selected shelf is already full\n");
            if(isShelfRearrangeable(resourceToPlace)){
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

    private void differentResTypeShelfManagement(List<Shelf> shelves, List<Resource> toPlace, Shelf selectedShelf, Resource resourceToPlace){
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
                if (isShelfRearrangeable(resourceToPlace)) { //it's possible to rearrange shelves
                    graphicalCLI.printString("If you want to place it here anyway, insert YES and " +
                            "then you'll place again the removed ones: ");
                    if (isAnswerYes()) {
                        Shelf otherShelf = getShelfWithSameResource(resourceToPlace.getResourceType());
                        for (int i = 0; i < otherShelf.getResources().getQuantity(); i++) {
                            toPlace.add(1, new Resource(selectedShelf.getResourceType(), 1));
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

    private boolean isResourceTypeUnique(List<Shelf> shelves, ResourceType resourceType){
        return shelves.stream().noneMatch(shelf -> shelf.getResourceType().equals(resourceType));
    }

    private void placeResource(Shelf shelf, Resource resource){
        if(shelf.getResourceType().equals(resource.getResourceType())){ //ripiano con stesso tipo di risorsa
            shelf.getResources().setQuantity(shelf.getResources().getQuantity() +
                    resource.getQuantity());
        }
        else { //ripiano vuoto o con altro da sostituire
            shelf.setResourceType(resource.getResourceType());
            shelf.getResources().setResourceType(resource.getResourceType());
            shelf.getResources().setQuantity(resource.getQuantity());
        }
    }

    private Shelf getShelfWithSameResource(ResourceType resourceType){
        try {
            List<Shelf> shelves = playerBoardFromNickname(nickname).getWarehouse().getShelves();

            for(Shelf shelf : shelves){
                if(shelf.getResourceType().equals(resourceType))
                    return shelf;
            }
        } catch (NotExistingNickname notExistingNickname) {
            notExistingNickname.printStackTrace();
        }
        return null;
    }

    private boolean isShelfRearrangeable(Resource resource){ //TODO: nome da cambiare?
        Shelf shelfWithResources = getShelfWithSameResource(resource.getResourceType());
        return shelfWithResources.getResources().getQuantity()+resource.getQuantity() <= 3;
    }

    private void resetShelf(Shelf shelf){
        shelf.setResourceType(ResourceType.WILDCARD);
        shelf.getResources().setResourceType(ResourceType.WILDCARD);
        shelf.getResources().setQuantity(0);
    }

    private void placeResourcesOnShelves(List<Resource> resources, boolean leaderShelfActive){
        //TODO: gestire così il parametro va bene?
        //TODO: da completare, è un casino :)
        //sendShelvesConfiguration();
    }

    public void selectMarket(){
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
                packetHandler.sendMessage(new SelectMarketMessage(row, column));
            else
                graphicalCLI.printString("Your choice is invalid, please try again\n");
        } while(!valid);
        mainActionPlayed = true;
    }

    private void selectDevDecks(){ //TODO: dividere in selezione carta (aspettando ack) e selezione spazio?
        graphicalCLI.printDevelopmentDecks(developmentDecks);
        if(askGoBack())
            return;
        graphicalCLI.printString("Which card do you want to buy?\nChoose B (blue), G (green), P (purple) or Y (yellow)" +
                " followed by a number corresponding to its level: ");

        DevelopmentCard developmentCard = chooseCardFromDecks();
        int space = chooseDevCardSpace(developmentCard.getLevel());
        //TODO: aggiungere controlli anche su risorse da spendere?

        storeTempCard(developmentCard);
        packetHandler.sendMessage(new BuyDevelopmentCardMessage(developmentCard, space));
        mainActionPlayed = true;
    }

    private DevelopmentCard chooseCardFromDecks(){
        boolean valid;
        String choice;
        int level=0;
        List<DevelopmentCard> activeCards = getActiveCardsInSpaces(nickname);
        //TODO: va bene? non verrà mai utilizzato. Oppure costruttore vuoto?
        DevelopmentCard developmentCard = new DevelopmentCard(-1,0,null,-1,null,null);
        do {
            valid = true;
            choice = scanner.next();
            switch (choice.toUpperCase()) {
                case "B1":
                case "B2":
                case "B3":
                    level = Integer.parseInt(Character.toString(choice.charAt(1)));
                    if(!isDeckCardAvailable(CardColors.BLUE.toString(), level)){
                        graphicalCLI.printString("There's no more cards available from this deck, please try again ");
                        valid=false;
                        break;
                    }
                    for (DevelopmentDeckView developmentDeck : developmentDecks) {
                        if (developmentDeck.getDeckColor().equals(CardColors.BLUE) &&
                                developmentDeck.getDeckLevel() == level) {
                            developmentCard = (DevelopmentCard) developmentDeck.getDeck().get(0);
                            break;
                        }
                    }
                    break;
                case "G1":
                case "G2":
                case "G3":
                    level = Integer.parseInt(Character.toString(choice.charAt(1)));
                    if(!isDeckCardAvailable(CardColors.GREEN.toString(), level)){
                        graphicalCLI.printString("There's no more cards available from this deck, please try again ");
                        valid=false;
                        break;
                    }
                    for (DevelopmentDeckView developmentDeck : developmentDecks) {
                        if (developmentDeck.getDeckColor().equals(CardColors.GREEN) &&
                                developmentDeck.getDeckLevel() == level) {
                            developmentCard = (DevelopmentCard) developmentDeck.getDeck().get(0);
                            break;
                        }
                    }
                    break;
                case "P1":
                case "P2":
                case "P3":
                    level = Integer.parseInt(Character.toString(choice.charAt(1)));
                    if(!isDeckCardAvailable(CardColors.PURPLE.toString(), level)){
                        graphicalCLI.printString("There's no more cards available from this deck, please try again ");
                        valid=false;
                        break;
                    }
                    for (DevelopmentDeckView developmentDeck : developmentDecks) {
                        if (developmentDeck.getDeckColor().equals(CardColors.PURPLE) &&
                                developmentDeck.getDeckLevel() == level) {
                            developmentCard = (DevelopmentCard) developmentDeck.getDeck().get(0);
                            break;
                        }
                    }
                    break;
                case "Y1":
                case "Y2":
                case "Y3":
                    level = Integer.parseInt(Character.toString(choice.charAt(1)));
                    if(!isDeckCardAvailable(CardColors.YELLOW.toString(), level)){
                        graphicalCLI.printString("There's no more cards available from this deck, please try again ");
                        valid=false;
                        break;
                    }
                    for (DevelopmentDeckView developmentDeck : developmentDecks) {
                        if (developmentDeck.getDeckColor().equals(CardColors.YELLOW) &&
                                developmentDeck.getDeckLevel() == level) {
                            developmentCard = (DevelopmentCard) developmentDeck.getDeck().get(0);
                            break;
                        }
                    }
                    break;
                default:
                    graphicalCLI.printString("Your choice is invalid, please try again ");
                    valid = false;
                    break;
            }

            if(valid){ //TODO: verificare che siano controlli giusti (sono tra l'altro controlli già presenti a lato server)
                int finalLevel = level;
                if(!((finalLevel==1 && activeCards.size()<=2) || (activeCards.size()>0 &&
                        activeCards.stream().anyMatch(card -> card.getLevel()==finalLevel-1)))){
                    graphicalCLI.printString("You don't have any slot to place the selected card on, please choose another one ");
                    valid = false;
                }
            }

        } while (!valid);
        return developmentCard;
    }

    private boolean isDeckCardAvailable(String color, int level){
        for(DevelopmentDeckView deck : developmentDecks){
            if(deck.getDeckColor().toString().equals(color) &&
                    deck.getDeckLevel()==level && !deck.getDeck().isEmpty())
                return true;
        }
        return false;
    }

    private int chooseDevCardSpace(int cardLevel){
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
        return space;
    }

    private List<DevelopmentCard> getActiveCardsInSpaces(String nickname){ //TODO: da testare quando funzionerà tutto e si sarà comprata qualche carta
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

    public void turnMenu(boolean isPlayerTurn){ //TODO: gestire per far fare comunque altre azioni
        int action;
        goBack = false;
        endTurn = false;
        refresh(nickname);
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
                        chooseStorages(cardToBuy.getCost());
                    }
                    else {
                        graphicalCLI.printString("You can't play this action on your turn anymore\n");
                        goBack = true;
                    }
                    break;
                case 3:
                    if (!mainActionPlayed)
                        //chiedo che produzioni attivare
                        ;
                    else {
                        graphicalCLI.printString("You can't play this action on your turn anymore\n");
                        goBack = true;
                    }

                    break;
                case 4: //chiedo che leader card attivare
                    break;
                case 5: //chiedo che leader card scartare
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

    private boolean askGoBack(){
        graphicalCLI.printString("Do you want to go back and choose another action?\nIf you want to, insert YES: ");
        if(isAnswerYes()){
            goBack = true;
            return true;
        }
        return false;
    }


    public List<RequestResources> chooseStorages(List<Resource> resources){

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
                choice = scanner.nextInt();
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
  
    private boolean isAnswerYes(){
        String command = scanner.next();
        return  command.equalsIgnoreCase("YES") || command.equalsIgnoreCase("Y");

    }
}
