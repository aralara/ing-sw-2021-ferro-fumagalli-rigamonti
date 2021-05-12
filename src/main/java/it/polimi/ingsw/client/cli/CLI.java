package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.client.structures.*;
import it.polimi.ingsw.exceptions.NotExistingNickname;
import it.polimi.ingsw.server.Server;
import it.polimi.ingsw.server.model.cards.ability.*;
import it.polimi.ingsw.server.model.cards.card.CardColors;
import it.polimi.ingsw.server.model.cards.card.DevelopmentCard;
import it.polimi.ingsw.server.model.cards.deck.Deck;
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
    private final List<PlayerBoardView> playerBoards;
    private final MarketView marketView;
    private final List<DevelopmentDeckView> developmentDecks;
    private final FaithTrackView faithTrackView;
    private List<Resource> resourcesToPut;
    private DevelopmentCard cardToBuy;
    private List<Production> productionsToActivate;
    private final Scanner scanner;
    private boolean goBack, mainActionPlayed, endTurn;
    private final GraphicalCLI graphicalCLI;
    private final PacketHandler packetHandler;

    public CLI(){
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

    private boolean connect(){
        graphicalCLI.printString("Insert the IP address of server\n");
        String ip = scanner.nextLine();

        return packetHandler.start(ip,Server.SOCKET_PORT);
    }

    public void askNickname(){
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

    private List<Resource> resolveResourcesWithLeader(int wildcardQuantity){ //TODO: Simile a resolveResourcesToEqualize
        List<Resource> resources = new ArrayList<>();
        List<AbilityMarble> leaderAbility = getActiveAbilityMarble();
        int index, size=leaderAbility.size();

        if(size>0 && wildcardQuantity>0) {
            graphicalCLI.printString("You can choose a resource from the following leader's ability: \n");
            graphicalCLI.printLeaderAbilityMarble(leaderAbility);

            for (int num = 1; num <= wildcardQuantity; num++) {
                index = -1;
                while (index < 0 || index >= size) {
                    graphicalCLI.printString("Wildcard" + num + ": please choose a valid resource ");
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

    public void tryAgainToPlaceResources(){ //TODO: decidere visibility (anche x altri try)
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
        //TODO: da completare, è un casino :)
        if(askGoBack())
            return;
        //sendShelvesConfiguration();
    }

    private void placeResourcesOnShelves(List<Resource> resources, boolean leaderShelfActive){
        //TODO: gestire così il parametro va bene?
        //TODO: da completare, è un casino :)
        if(askGoBack())
            return;
        //sendShelvesConfiguration();
    }

    private void sendShelvesConfiguration(List<Shelf> shelves, List<Resource> extra){
        packetHandler.sendMessage(new ShelvesConfigurationMessage(shelves, extra));
    }

    public void selectMarket(){
        graphicalCLI.printMarket(marketView);
        if(askGoBack())
            return;
        graphicalCLI.printString("Where do you want to place the marble?\nChoose R (row) or C (column) followed by a number: \n");

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

    public void chooseAction(StartTurnMessage message) {
        //TODO: serve una stringa che inserita in qualsiasi modo ci faccia tornare indietro
        // (es. se provo a comprare una carta ma non ho risorse se no si blocca il gioco)

        if (message.getPlayingNickname().equals(nickname)) {
            graphicalCLI.printString("\nNOW IT'S YOUR TURN!\n\n");
            mainActionPlayed = false;

            turnMenu(true);
        }
        else {
            graphicalCLI.printString("Now is " + message.getPlayingNickname() + "'s turn\n");
            //turnMenu(false); TODO: gestire per far fare comunque altre azioni
        }
    }

    public void turnMenu(boolean isPlayerTurn){ //TODO: gestire per far fare comunque altre azioni
        int action;
        goBack = false;
        endTurn = false;
        refresh(nickname);
        if(isPlayerTurn) {
            do {
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
                        if (!mainActionPlayed)
                            selectDevDecks();
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
            } while (!endTurn);
        }
    }

    private boolean askGoBack(){
        graphicalCLI.printString("Do you want to go back and choose another action?\nIf you want to, insert YES: ");
        String command = scanner.next();
        if(command.equalsIgnoreCase("YES") || command.equalsIgnoreCase("Y")){
            goBack = true;
            return true;
        }
        return false;
    }
}
