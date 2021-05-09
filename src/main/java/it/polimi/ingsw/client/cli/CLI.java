package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.client.structures.*;
import it.polimi.ingsw.server.Server;
import it.polimi.ingsw.server.model.cards.card.LeaderCard;
import it.polimi.ingsw.server.model.cards.deck.Deck;
import it.polimi.ingsw.server.model.cards.deck.DevelopmentDeck;
import it.polimi.ingsw.server.model.faith.VaticanReport;
import it.polimi.ingsw.server.model.market.Marble;
import it.polimi.ingsw.utils.messages.client.LeaderCardDiscardMessage;
import it.polimi.ingsw.utils.messages.client.SelectMarketMessage;
import it.polimi.ingsw.utils.messages.server.DevelopmentDecksMessage;
import it.polimi.ingsw.utils.messages.server.FaithTrackMessage;
import it.polimi.ingsw.utils.messages.server.MarketMessage;
import it.polimi.ingsw.utils.messages.server.PlayerBoardSetupMessage;

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
        packetHandler.sendConnectionMessage(nickname);
    }

    public void createNewLobby(){
        int size;
        System.out.println("There isn't any player waiting for a match!");
        do {
            System.out.println("Insert the number of player that will play the game (between 1 and 4)");
            size = scanner.nextInt();
        }while(size <= 0 || size >= 5);
        setNumberOfPlayers(size);
        packetHandler.sendNewGameSize(size);
    }

    public void setNumberOfPlayers(int numberOfPlayers){
        this.numberOfPlayers = numberOfPlayers;
    }

    public void notifyNewPlayer(String nickname){
        if(!this.nickname.equals(nickname)) {
            System.out.println("The player " + nickname + " has joined the game!");
        }else{
            System.out.println("You have been added to the game!");
        }
    }

    public void playerBoardSetup(PlayerBoardSetupMessage message){  //TODO: metodo di update da rimuovere quando ci saranno le action
        DevelopmentBoardView developmentBoard = new DevelopmentBoardView(message.getDevelopmentBSpaces());
        LeaderBoardView leaderBoard = new LeaderBoardView();
        leaderBoard.setBoard(message.getLeaderBBoard());
        leaderBoard.setHand(message.getLeaderBHand());
        FaithBoardView faithBoard = new FaithBoardView(message.getFaithBFaith(), message.getFaithBPope());
        WarehouseView warehouse = new WarehouseView(message.getWarehouse());
        StrongboxView strongbox = new StrongboxView(message.getStrongbox());
        boolean inkwell = message.isFirstPlayer();

        PlayerBoardView playerBoard = new PlayerBoardView();    //TODO: costruttore con parametri
        /*
        ((PlayerBoardView)opposingPlayerBoards.get(index)).setDevelopmentBoard(developmentBoard);
        ((PlayerBoardView)opposingPlayerBoards.get(index)).setLeaderBoard(leaderBoard);
        ((PlayerBoardView)opposingPlayerBoards.get(index)).setFaithBoard(faithBoard);
        ((PlayerBoardView)opposingPlayerBoards.get(index)).setWarehouse(warehouse);
        ((PlayerBoardView)opposingPlayerBoards.get(index)).setStrongbox(strongbox);
        ((PlayerBoardView)opposingPlayerBoards.get(index)).setInkwell(inkwell);
         */
        playerBoards.add(playerBoard);
    }

    private PlayerBoardView playerBoardFromNickname(String nickname){
        for(PlayerBoardView playerBoard : playerBoards)
            if(playerBoard.getNickname().equals(nickname))
                return playerBoard;
        return null;    //TODO: possibile eccezione?
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
    }

    private void sendDiscardedLeader(List<LeaderCard> leaderCards){
        for(LeaderCard leaderCard : leaderCards)
            packetHandler.sendMessage(new LeaderCardDiscardMessage(leaderCard));
    }

    public void updateLeaderHand(Deck leaderCards){ //TODO: messaggio unico con lista carte
        /*
        playerBoardView.getLeaderBoard().setHand(leaderCards);
        if(playerBoardView.getLeaderBoard().getHand().size() == 2){
            temp();
        }*/
    }

    public void temp(){/*
        for(int i = 0; i<playerBoardView.getLeaderBoard().getHand().size();i++){  //TODO: da togliere (per testare)
            graphicalCLI.printLeaderCard((LeaderCard)playerBoardView.getLeaderBoard().getHand().get(i));
        }*/
    }

    public void chooseAction() {        // TODO: scritto di fretta per testare (da togliere prima o poi)
        int action = -1;
        while(action != 1) {
            System.out.println("1-market 2-development 3-productions");
            action = scanner.nextInt();
        }
        int column = 0;
        int row = -1;
        while(row < 0 || row > 3) {
            System.out.println("row number? (0 for column)");
            row = scanner.nextInt();
        }
        if(row == 0){
            while(column <= 0 || column > 4) {
                System.out.println("column number?");
                column = scanner.nextInt();
            }
        }
        packetHandler.sendMessage(new SelectMarketMessage(row-1, column-1));
    }
}
