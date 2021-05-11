package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.client.structures.*;
import it.polimi.ingsw.exceptions.NotExistingNickname;
import it.polimi.ingsw.server.Server;
import it.polimi.ingsw.server.model.cards.ability.*;
import it.polimi.ingsw.server.model.cards.card.CardColors;
import it.polimi.ingsw.server.model.cards.card.DevelopmentCard;
import it.polimi.ingsw.server.model.cards.card.LeaderCard;
import it.polimi.ingsw.server.model.cards.deck.Deck;
import it.polimi.ingsw.server.model.cards.deck.DevelopmentDeck;
import it.polimi.ingsw.server.model.faith.VaticanReport;
import it.polimi.ingsw.server.model.storage.Production;
import it.polimi.ingsw.server.model.storage.Resource;
import it.polimi.ingsw.server.model.storage.ResourceType;
import it.polimi.ingsw.server.model.storage.Shelf;
import it.polimi.ingsw.utils.messages.client.*;
import it.polimi.ingsw.utils.messages.server.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CLI {

    private String nickname;
    private int numberOfPlayers;
    private int lorenzoFaith;                   //TODO: gestione lorenzo WIP
    private List<PlayerBoardView> playerBoards;
    private MarketView marketView;
    private List<DevelopmentDeckView> developmentDecks;
    private FaithTrackView faithTrackView;
    private List<Resource> resourcesToPut;
    private DevelopmentCard cardToBuy;
    private List<Production> productionsToActivate;
    private Scanner scanner;
    private GraphicalCLI graphicalCLI;
    private PacketHandler packetHandler;

    public CLI(){
        lorenzoFaith = -1;
        playerBoards = new ArrayList<>();
        marketView = new MarketView();
        developmentDecks = new ArrayList<>();
        faithTrackView = new FaithTrackView();
        scanner = new Scanner(System.in);
        graphicalCLI = new GraphicalCLI();
        packetHandler = new PacketHandler(this);
    }

    public void setup() {
        while(!connect());
        System.out.println("Insert your nickname");
        askNickname();
    }

    private boolean connect(){
        System.out.println("Insert the IP address of server");
        String ip = scanner.nextLine();

        return packetHandler.start(ip,Server.SOCKET_PORT);
    }

    public void askNickname(){
        do {
            nickname = scanner.nextLine();
        }while (nickname.equals(""));
        packetHandler.sendMessage(new ConnectionMessage(nickname));
    }

    public void createNewLobby(){
        int size;
        System.out.println("There isn't any player waiting for a match!");
        do {
            System.out.println("Insert the number of players that will play the game " +
                    "(value inserted must between 1 and 4)");
            size = scanner.nextInt();
        }while(size <= 0 || size >= 5);
        setNumberOfPlayers(size);
        packetHandler.sendMessage(new NewLobbyMessage(size));
    }

    public void setNumberOfPlayers(int numberOfPlayers){
        this.numberOfPlayers = numberOfPlayers;
        if(numberOfPlayers==1)
            lorenzoFaith=0;
    }

    public void notifyNewPlayer(String nickname){
        if(!this.nickname.equals(nickname)) {
            System.out.println("The player " + nickname + " has joined the game!");
        }else{
            System.out.println("You have been added to the game!");
        }
    }

    public void playerBoardSetup(PlayerBoardSetupMessage message){
        String nickname = message.getNickname();
        DevelopmentBoardView developmentBoard = new DevelopmentBoardView(message.getDevelopmentBSpaces());
        LeaderBoardView leaderBoard = new LeaderBoardView();
        leaderBoard.setBoard(message.getLeaderBBoard());
        leaderBoard.setHand(message.getLeaderBHand());
        FaithBoardView faithBoard = new FaithBoardView(message.getFaithBFaith(), message.getFaithBPope());
        WarehouseView warehouse = new WarehouseView(message.getWarehouse());
        StrongboxView strongbox = new StrongboxView(message.getStrongbox());
        boolean inkwell = message.isFirstPlayer();

        PlayerBoardView playerBoard = new PlayerBoardView(nickname,developmentBoard,leaderBoard,faithBoard,
                warehouse,strongbox,inkwell);
        playerBoards.add(playerBoard);

        if(playerBoards.size()==numberOfPlayers)
            System.out.println("\nTHE GAME CAN START!\n");
    }

    private PlayerBoardView playerBoardFromNickname(String nickname) throws NotExistingNickname {
        for(PlayerBoardView playerBoard : playerBoards)
            if(playerBoard.getNickname().equals(nickname))
                return playerBoard;
        throw new NotExistingNickname();
    }

    public void updateMarket(MarketMessage message){
        marketView.setMarbleMatrix(message.getMarbleMatrix());
        marketView.setFloatingMarble(message.getFloatingMarble());
    }

    public void developmentDecksSetup(DevelopmentDecksMessage message){
        for(DevelopmentDeck developmentDeck : message.getDevelopmentDecks())
            developmentDecks.add(new DevelopmentDeckView(developmentDeck.getDeck(), developmentDeck.getDeckColor(),
                    developmentDeck.getDeckLevel()));
    }

    public void faithTrackSetup(FaithTrackMessage message){
        List<VaticanReportView> vaticanReports = new ArrayList<>();
        for(VaticanReport vaticanReport : message.getVaticanReports()){
            vaticanReports.add(new VaticanReportView(vaticanReport.getMin(), vaticanReport.getMax(),
                    vaticanReport.getPopeValue()));
        }
        faithTrackView.setFaithTrackView(vaticanReports, message.getFaithSpaces());
    }

    public void askDiscardLeader(){
        try {
            PlayerBoardView playerBoard = playerBoardFromNickname(nickname);
            int firstOne, secondOne, size = playerBoard.getLeaderBoard().getHand().size();

            System.out.println("You have to discard 2 leader cards from your hand:");
            graphicalCLI.printLeaderCardList(playerBoard.getLeaderBoard().getHand());

            System.out.print("Choose the first one by selecting the corresponding number: ");
            firstOne = scanner.nextInt()-1;
            while(firstOne<0 || firstOne>=size){
                System.out.print("The chosen number is invalid, please choose another one: ");
                firstOne = scanner.nextInt()-1;
            }
            System.out.print("Choose the second one by selecting the corresponding number: ");
            secondOne = scanner.nextInt()-1;
            while(secondOne<0 || secondOne>=size || secondOne == firstOne){
                System.out.print("The chosen number is invalid, please choose another one: ");
                secondOne = scanner.nextInt()-1;
            }

            List<LeaderCard> leaderCards = new ArrayList<>();
            leaderCards.add((LeaderCard) playerBoard.getLeaderBoard().getHand().get(firstOne));
            leaderCards.add((LeaderCard) playerBoard.getLeaderBoard().getHand().get(secondOne));
            sendDiscardedLeader(leaderCards);
        }catch (NotExistingNickname e){
            e.printStackTrace();
        }
    }

    private void sendDiscardedLeader(List<LeaderCard> leaderCards){ //TODO: conviene mettere qua o lasciamo nel metodo? (stessa cosa per gli altri send)
        packetHandler.sendMessage(new LeaderCardDiscardMessage(leaderCards));
    }

    public void updateLeaderHand(PlayerLeaderBHandMessage message){
        try {
            playerBoardFromNickname(message.getNickname()).getLeaderBoard().setHand(message.getHand());
        } catch(NotExistingNickname e){
            e.printStackTrace();
        }
    }

    public void askResourcesToEqualize(ResourcesEqualizeMessage message){ //TODO: da controllare poi con il metodo corrispondente del server
        List<Resource> newResources = new ArrayList<>();
        for(Resource resource : message.getResources()) {
            if (resource.getResourceType() == ResourceType.FAITH) {
                try {
                    playerBoardFromNickname(nickname).getFaithBoard().setFaith(resource.getQuantity()); //TODO: Do per scontato che arriverà solo quello del player corretto?
                    System.out.println(resource.getQuantity() + " " + resource.getResourceType()
                            + " has been added to your faith board");
                } catch (NotExistingNickname e) {
                    e.printStackTrace();
                }
            }
            else if (resource.getResourceType() == ResourceType.WILDCARD){
                newResources = resolveResourcesToEqualize(resource.getQuantity());
            }
        }
        if(newResources.size()>0) {
            storeTempResources(newResources);
            System.out.println("Now place the resources on the shelves:");
            selectShelvesManagement(newResources);
        }
    }

    private List<Resource> resolveResourcesToEqualize(int wildcardQuantity){ //TODO: sarà chiamato una sola volta per equalizzare
        int index;
        List<Resource> resources = new ArrayList<>();
        for(int num=0; num<wildcardQuantity; num++){
            System.out.println("You can choose a resource from the following: "); //TODO: in ordine come nel file... va bene?
            for(int i=0;i<4;i++)
                System.out.println((i+1) + ": " + ResourceType.values()[i].toString());
            index = -1;
            while(index<0 || index>=4){
                System.out.print("Please choose a valid resource: ");
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

    private List<Resource> resolveResourcesWithLeader(int wildcardQuantity){ //TODO: Simile a resolveResourcesToEqualize
        List<Resource> resources = new ArrayList<>();
        List<AbilityMarble> leaderAbility = getActiveAbilityMarble();
        int index, size=leaderAbility.size();

        if(size>0 && wildcardQuantity>0) {
            System.out.println("You can choose a resource from the following leader's ability: ");
            graphicalCLI.printLeaderAbilityMarble(leaderAbility);

            for (int num = 1; num <= wildcardQuantity; num++) {
                index = -1;
                while (index < 0 || index >= size) {
                    System.out.print("Wildcard" + num + ": please choose a valid resource ");
                    index = scanner.nextInt() - 1;
                }

                if (resources.size() > 0 && resources.get(0).getResourceType() == ResourceType.values()[index]) {
                    resources.get(0).setQuantity(resources.get(0).getQuantity() + 1);
                } else {
                    resources.add(new Resource(ResourceType.values()[index], 1));
                }
            }
        }
        return resources;
    }

    private void refresh(String nickname){
        //TODO: da fare
        graphicalCLI.printMarket(marketView);
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

    private void storeTempResources(List<Resource> resourcesToMemorize){
        resourcesToPut = new ArrayList<>(resourcesToMemorize);
    }

    private void storeTempCard(DevelopmentCard devCardToMemorize){ //TODO: valutare se aggiungere costruttore apposito
        cardToBuy = new DevelopmentCard(devCardToMemorize.getID(),devCardToMemorize.getVP(),devCardToMemorize.getColor(),
                devCardToMemorize.getLevel(),devCardToMemorize.getProduction(),devCardToMemorize.getCost());
    }

    private void storeTempProduction(List<Production> productionsToMemorize){
        productionsToActivate = new ArrayList<>(productionsToMemorize);
    }

    public void tryAgainToPlaceResources(){ //TODO: decidere visibility (anche x altri try)
        System.out.println("Please, try again to place on the shelves:");
        selectShelvesManagement(resourcesToPut);
    }

    private void tryAgainToBuyCard(){
        //TODO: da fare
    }

    private void tryAgainToActivateProduction(){
        //TODO: da fare
    }

    private void selectShelvesManagement(List<Resource> resources){ //TODO: x controllare se si hanno o meno i leader
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
        //TODO: da completare, è un casino :)
        //sendShelvesConfiguration();
    }

    private void placeResourcesOnShelves(List<Resource> resources, boolean leaderShelfActive){
        //TODO: gestire così il parametro va bene?
        //TODO: da completare, è un casino :)
        //sendShelvesConfiguration();
    }

    private void sendShelvesConfiguration(List<Shelf> shelves, List<Resource> extra){
        packetHandler.sendMessage(new ShelvesConfigurationMessage(shelves, extra));
    }

    public void selectMarket(){
        graphicalCLI.printMarket(marketView);
        System.out.print("Where do you want to place the marble?\nChoose R (row) or C (column) followed by a number: ");

        boolean valid;
        do {
            valid = true;
            String choice = scanner.next();
            switch (choice.toUpperCase()) {
                case "R1": sendMarketChoice(0, -1);
                    break;
                case "R2": sendMarketChoice(1, -1);
                    break;
                case "R3": sendMarketChoice(2, -1);
                    break;
                case "C1": sendMarketChoice(-1, 0);
                    break;
                case "C2": sendMarketChoice(-1, 1);
                    break;
                case "C3": sendMarketChoice(-1, 2);
                    break;
                case "C4": sendMarketChoice(-1, 3);
                    break;
                default: System.out.println("Your choice is invalid, please try again"); valid=false;
                    break;
            }
        } while(!valid);
    }

    private void sendMarketChoice(int row, int column){
        packetHandler.sendMessage(new SelectMarketMessage(row, column));
    }

    private void selectDevDecks(){
        System.out.print("Which card do you want to buy?\nChoose B (blue), G (green), P (purple) or Y (yellow)" +
                " followed by a number corresponding to its level: ");

        DevelopmentCard developmentCard = chooseCardFromDecks();
        int space = chooseDevCardSpace(developmentCard.getLevel());

        storeTempCard(developmentCard);
        sendDevDeckChoice(developmentCard, space);
    }

    private DevelopmentCard chooseCardFromDecks(){
        boolean valid;
        String choice;
        int level=0;
        List<DevelopmentCard> activeCards = getActiveCardsInSpaces(nickname);
        DevelopmentCard developmentCard = new DevelopmentCard(-1,0,null,-1,null,null); //TODO: va bene? non verrà mai utilizzato. Oppure costruttore vuoto?

        do {
            valid = true;
            choice = scanner.next();
            switch (choice.toUpperCase()) {
                case "B1":
                case "B2":
                case "B3":
                    level = Integer.parseInt(Character.toString(choice.charAt(1)));
                    if(!isDeckCardAvailable(CardColors.BLUE.toString(), level)){
                        System.out.print("There's no more cards available from this deck, please try again ");
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
                        System.out.print("There's no more cards available from this deck, please try again ");
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
                        System.out.print("There's no more cards available from this deck, please try again ");
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
                        System.out.print("There's no more cards available from this deck, please try again ");
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
                    System.out.print("Your choice is invalid, please try again ");
                    valid = false;
                    break;
            }

            if(valid){ //TODO: verificare che siano controlli giusti (sono tra l'altro controlli già presenti a lato server)
                int finalLevel = level;
                if(!((finalLevel==1 && activeCards.size()<=2) || (activeCards.size()>0 &&
                        activeCards.stream().anyMatch(card -> card.getLevel()==finalLevel-1)))){
                    System.out.print("You don't have any slot to place the selected card on, please choose another one ");
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
            System.out.print("Which space do you want to put the card on? ");
            space = scanner.nextInt()-1;
            do{
                valid=true;
                while (space<0 || space>=3){
                    System.out.print("Your choice is invalid, please try again ");
                    space = scanner.nextInt()-1;
                }
                if((cardLevel==1 && spaces.get(space).size()>0) || cardLevel >1 && (spaces.get(space).size()==0 ||
                        ((DevelopmentCard)spaces.get(space).get(0)).getLevel()>=cardLevel-1)){
                    System.out.print("The selected slot has been already filled, please try again ");
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

    private void sendDevDeckChoice(DevelopmentCard developmentCard,  int space){
        packetHandler.sendMessage(new BuyDevelopmentCardMessage(developmentCard, space));
    }

    public void chooseAction(StartTurnMessage message) {
        //TODO: serve una stringa che inserita in qualsiasi modo ci faccia tornare indietro
        // (es. se provo a comprare una carta ma non ho risorse se no si blocca il gioco)
        if (message.getPlayingNickname().equals(nickname)) {
            System.out.println("\nNOW IT'S YOUR TURN!\n");
            refresh(nickname);
            int action;
            graphicalCLI.printActions();
            do {
                action = scanner.nextInt();
            } while (action < 1 || action > 4);
            switch (action) {
                case 1:
                    selectMarket();
                    break;
                case 2:
                    selectDevDecks();
                    break;
                case 3: //chiedo che produzioni attivare
                    break;
                case 4: //chiedo che leader card attivare
                    break;
                case 5: //chiedo che leader card scartare
                    break;
                default: //boh, default non lo farò mai :)
                    break;
            }
        }
        else System.out.println("Now is " + message.getPlayingNickname() + "'s turn");
    }
}
