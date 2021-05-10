package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.client.structures.*;
import it.polimi.ingsw.exceptions.NotExistingNickname;
import it.polimi.ingsw.server.Server;
import it.polimi.ingsw.server.model.cards.ability.AbilityWarehouse;
import it.polimi.ingsw.server.model.cards.card.LeaderCard;
import it.polimi.ingsw.server.model.cards.deck.Deck;
import it.polimi.ingsw.server.model.cards.deck.DevelopmentDeck;
import it.polimi.ingsw.server.model.faith.VaticanReport;
import it.polimi.ingsw.server.model.storage.Resource;
import it.polimi.ingsw.server.model.storage.ResourceType;
import it.polimi.ingsw.server.model.storage.Shelf;
import it.polimi.ingsw.utils.messages.client.ConnectionMessage;
import it.polimi.ingsw.utils.messages.client.LeaderCardDiscardMessage;
import it.polimi.ingsw.utils.messages.client.NewLobbyMessage;
import it.polimi.ingsw.utils.messages.client.ShelvesConfigurationMessage;
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
        System.out.println("IP address of server?");
        String ip = scanner.nextLine();

        return packetHandler.start(ip,Server.SOCKET_PORT);
    }

    public void askNickname(){
        nickname = scanner.nextLine();
        packetHandler.sendMessage(new ConnectionMessage(nickname));
    }

    public void createNewLobby(){
        int size;
        System.out.println("There isn't any player waiting for a match!");
        do {
            System.out.println("Insert the number of player that will play the game (between 1 and 4)");
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

    public void playerBoardSetup(PlayerBoardSetupMessage message){  //TODO: metodo di update da rimuovere quando ci saranno le action
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
        throw new NotExistingNickname();    //TODO: va bene gestire con eccezione?
    }

    public void updateMarket(MarketMessage message){    //TODO: metodo di update da rimuovere quando ci saranno le action
        marketView.setMarbleMatrix(message.getMarbleMatrix());
        marketView.setFloatingMarble(message.getFloatingMarble());
        graphicalCLI.printMarket(marketView);  //TODO: da spostare nel metodo refresh
    }

    public void developmentDecksSetup(DevelopmentDecksMessage message){ //TODO: metodo di update da rimuovere quando ci saranno le action
        for(DevelopmentDeck developmentDeck : message.getDevelopmentDecks())
            developmentDecks.add(new DevelopmentDeckView(developmentDeck.getDeck(), developmentDeck.getDeckColor(),
                    developmentDeck.getDeckLevel()));
    }

    public void faithTrackSetup(FaithTrackMessage message){ //TODO: metodo di update da rimuovere quando ci saranno le action
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

            System.out.println("You have to discard 2 leader cards from your hand:");
            int size = playerBoard.getLeaderBoard().getHand().size();
            int firstOne, secondOne;
            for(int i=0; i<size; i++){
                System.out.print((i+1) + ": ");
                graphicalCLI.printLeaderCard((LeaderCard) playerBoard.getLeaderBoard().getHand().get(i));
            }

            System.out.print("Choose the first one by selecting the corresponding number: ");
            firstOne = scanner.nextInt();
            while(firstOne<=0 || firstOne>size){
                System.out.print("The chosen number is invalid, please choose another one: ");
                firstOne = scanner.nextInt();
            }
            System.out.print("Choose the second one by selecting the corresponding number: ");
            secondOne = scanner.nextInt();
            while(secondOne<=0 || secondOne>size || secondOne == firstOne){
                System.out.print("The chosen number is invalid, please choose another one: ");
                secondOne = scanner.nextInt();
            }

            List<LeaderCard> leaderCards = new ArrayList<>();
            leaderCards.add((LeaderCard) playerBoard.getLeaderBoard().getHand().get(firstOne-1));
            leaderCards.add((LeaderCard) playerBoard.getLeaderBoard().getHand().get(secondOne-1));
            sendDiscardedLeader(leaderCards);
        }catch (NotExistingNickname e){
            e.printStackTrace();
        }
    }

    private void sendDiscardedLeader(List<LeaderCard> leaderCards){
        packetHandler.sendMessage(new LeaderCardDiscardMessage(leaderCards));
    }

    public void updateLeaderHand(PlayerLeaderBHandMessage message){ //TODO: messaggio unico con lista carte
        try {
            PlayerBoardView playerBoard = playerBoardFromNickname(message.getNickname()); //TODO: controllare se così va bene
            playerBoard.getLeaderBoard().setHand(message.getHand());
            if (message.getNickname().equals(nickname) && playerBoard.getLeaderBoard().getHand().size()==2) { //TODO: da togliere
                temp(playerBoard);
            }
        } catch(NotExistingNickname e){
            e.printStackTrace();
        }
    }

    public void temp(PlayerBoardView player){
        for(int i = 0; i<player.getLeaderBoard().getHand().size();i++){  //TODO: da togliere (per testare)
            graphicalCLI.printLeaderCard((LeaderCard)player.getLeaderBoard().getHand().get(i));
        }
    }

    public void askResourcesToEqualize(ResourcesEqualizeMessage message){ //TODO: da controllare poi con il metodo corrispondente del server
        for(Resource resource : message.getResources()) {
            if (resource.getResourceType() == ResourceType.FAITH) {
                try {
                    playerBoardFromNickname(nickname).getFaithBoard().setFaith(resource.getQuantity()); //Do per scontato che arriverà solo quello del player corretto?
                    System.out.println(resource.getQuantity() + " " + resource.getResourceType()
                            + " has been added to your Faith Board");
                } catch (NotExistingNickname e) {
                    e.printStackTrace();
                }
            }
            else if (resource.getResourceType() == ResourceType.WILDCARD){ //TODO: potrebbe essere migliorato
                List<Resource> newResources = new ArrayList<>();
                for(int num=0; num<resource.getQuantity(); num++){
                    System.out.println("You can choose a resource from the following: "); //TODO: in ordine come nel file... va bene?
                    for(int i=0;i<4;i++)
                        System.out.println((i+1) + ": " + ResourceType.values()[i].toString());
                    int index = -1;
                    while(index<0 || index>=4){
                        System.out.print("Please choose a valid resource: ");
                        index = scanner.nextInt()-1;
                    }

                    if(newResources.size()>0 && newResources.get(0).getResourceType()==ResourceType.values()[index]){
                        newResources.get(0).setQuantity(newResources.get(0).getQuantity()+1);
                    } else {
                        newResources.add(new Resource(ResourceType.values()[index], 1));
                    }
                } //TODO: risorse da memorizzare da qualche parte prima che sia convalidato il loro posizionamento
                System.out.println("Now place on the shelves:");
                selectShelvesManagement(newResources); //x controllare se si hanno o meno i leader
            }
        }
    }

    private void selectShelvesManagement(List<Resource> resources){ //TODO: manca gestione slot leader
        //printare ordine shelves

        try {
            PlayerBoardView player = playerBoardFromNickname(nickname);
            List<AbilityWarehouse> leaderWarehouse = new ArrayList<>();

            for (int i = 0; i < player.getLeaderBoard().getBoard().size(); i++) {
                LeaderCard leaderCard = (LeaderCard) player.getLeaderBoard().getBoard().get(i);
                if (leaderCard.getAbility() instanceof AbilityWarehouse)
                    leaderWarehouse.add((AbilityWarehouse) leaderCard.getAbility());
            }

            if(leaderWarehouse.size()==0)
                placeResourcesOnShelves(resources);
            else
                placeResourcesOnShelves(resources, leaderWarehouse);
        }catch (NotExistingNickname e){
            e.printStackTrace();
        }
    }

    private void placeResourcesOnShelves(List<Resource> resources){
        //TODO: da completare
        //sendShelvesConfiguration();
    }

    private void placeResourcesOnShelves(List<Resource> resources, List<AbilityWarehouse> leaderWarehouse){
        //TODO: da completare
        //sendShelvesConfiguration();
    }

    private void sendShelvesConfiguration(List<Shelf> shelves, List<Resource> extra){
        packetHandler.sendMessage(new ShelvesConfigurationMessage(shelves, extra));
    }

    public void chooseAction() {
        int action;
        graphicalCLI.printActions();
        do {
            action = scanner.nextInt();
        }while(action<1 || action >4);
        switch (action){
            case 1: //chiedo  riga/ colonna e invio packetHandler.sendMessage(new SelectMarketMessage();
                break;
            case 2: //chiedo che dev ccard comprare
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
}
