package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.client.gui.controllers.*;
import it.polimi.ingsw.client.structures.DevelopmentDeckView;
import it.polimi.ingsw.client.structures.MarketView;
import it.polimi.ingsw.exceptions.NotExistingNicknameException;
import it.polimi.ingsw.server.model.boards.Player;
import it.polimi.ingsw.server.model.cards.card.Card;
import it.polimi.ingsw.server.model.cards.card.LeaderCard;
import it.polimi.ingsw.server.model.cards.card.LorenzoCard;
import it.polimi.ingsw.server.model.storage.RequestResources;
import it.polimi.ingsw.server.model.storage.Resource;
import it.polimi.ingsw.server.model.storage.ResourceType;
import it.polimi.ingsw.server.model.storage.Shelf;
import it.polimi.ingsw.server.saves.GameSave;
import it.polimi.ingsw.utils.listeners.Listeners;
import it.polimi.ingsw.utils.listeners.client.*;
import it.polimi.ingsw.utils.messages.client.*;
import it.polimi.ingsw.utils.messages.server.ack.ServerAckMessage;
import it.polimi.ingsw.utils.messages.server.action.ServerActionMessage;
import javafx.application.Platform;
import javafx.scene.control.Alert;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;


public class GUI extends ClientController {

    private final GUIApplication guiApplication;
    private List<Resource> resourcesToEqualize; //TODO: serve per memorizzare le risorse che si devono equalizzare e usarle al momento opportuno
    private List<Resource> resourcesToPlace; //TODO: serve per memorizzare le risorse che si devono piazzare se viene richiamato il restore e devono essere rimesse
    private List<Resource> resourcesToDiscard; //TODO: serve per memorizzare le risorse che si devono scartare e se c'è della fede la immagazzina
    private int waitingPlayers;

    public GUI(GUIApplication guiApplication) {
        super();
        this.guiApplication = guiApplication;
    }

    @Override
    public void setup() {
        resourcesToEqualize = new ArrayList<>();
        resourcesToPlace = new ArrayList<>();
        resourcesToDiscard = new ArrayList<>();
    }

    public void attachListeners() {
        PlayerBoardController playerBoardController = (PlayerBoardController) guiApplication
                .getController(SceneNames.PLAYER_BOARD);
        DecksBoardController developmentDecksController = (DecksBoardController) guiApplication
                .getController(SceneNames.DECKS_BOARD);
        MarketBoardController marketController = (MarketBoardController) guiApplication
                .getController(SceneNames.MARKET_BOARD);
        getPlayerBoards().forEach(pb -> {
                pb.getLeaderBoard().addListener(Listeners.BOARD_LEADER_BOARD.value(),
                        new LeaderBBoardViewListener(playerBoardController));
                pb.getLeaderBoard().addListener(Listeners.BOARD_LEADER_HAND.value(),
                        new LeaderBHandViewListener(playerBoardController));
                pb.getFaithBoard().addListener(Listeners.BOARD_FAITH_FAITH.value(),
                        new FaithBFaithViewListener(playerBoardController));
                pb.getFaithBoard().addListener(Listeners.BOARD_FAITH_POPE.value(),
                        new FaithBPopeChangeViewListener(playerBoardController));
                pb.getWarehouse().addListener(Listeners.BOARD_WAREHOUSE.value(),
                        new WarehouseViewListener(playerBoardController));
                pb.getStrongbox().addListener(Listeners.BOARD_STRONGBOX.value(),
                        new StrongboxViewListener(playerBoardController));
        });
        getDevelopmentDecks().forEach(dd ->
                dd.addListener(Listeners.GAME_DEV_DECK.value(),
                        new DevelopmentDeckViewListener(developmentDecksController)));
        getMarket().addListener(Listeners.GAME_MARKET.value(),
                new MarketViewListener(marketController));
    }

    public boolean connect(String address, Integer port) {
        boolean success = getMessageHandler().connect(address, port);
        if(success)
            new Thread(getMessageHandler()).start();
        return success;
    }

    @Override
    public void run() {
        LinkedBlockingQueue<ServerActionMessage> actionQueue = getMessageHandler().getActionQueue();
        LinkedBlockingQueue<ServerAckMessage> responseQueue = getMessageHandler().getResponseQueue();
        List<ClientMessage> confirmationList = getMessageHandler().getConfirmationList();

        new Thread(getUpdateMessageReader()).start();

        while(true) {
            try {
                if (confirmationList.size() != 0)
                    responseQueue.take().activateResponse(this);
                else if (actionQueue.size() > 0)
                        actionQueue.poll().doAction(this);
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void ackNotification(String message, boolean visual) {
        //messaggio ricevuto da ack
        if(visual)
            Platform.runLater(() -> guiApplication.getController(guiApplication.getActiveSceneName()).
                    showAlert(Alert.AlertType.INFORMATION,"Notification","Event notification", message));
    }

    @Override
    public void askNickname() {
        guiApplication.changeNicknameMenuStatus();
    }

    @Override
    public void askNewLobby(int lobbySize, int waitingPlayers) {
        if (lobbySize == waitingPlayers){
            Platform.runLater(() -> guiApplication.setActiveScene(SceneNames.GAME_MODE_MENU));
        }
        else {
            setNumberOfPlayers(lobbySize);
            Platform.runLater(() -> guiApplication.setActiveScene(SceneNames.MULTI_PLAYER_WAITING));
        }
        this.waitingPlayers = waitingPlayers;
    }

    @Override
    public void displaySaves(List<GameSave> saves) {

    }

    @Override
    public void notifyNewPlayer(String nickname) {
        Platform.runLater(() -> ((SetupController)guiApplication.
                getController(SceneNames.MULTI_PLAYER_WAITING)).notifyNewPlayer(nickname));

        if(getNumberOfPlayers() == ++this.waitingPlayers){
            Platform.runLater(() -> guiApplication.setActiveScene(SceneNames.LOADING));
        }
    }

    @Override
    public void askLeaderDiscard() {
        //TODO:settare parti gioco
        // TODO: override dei metodi del client contorller perche quando faccio un set ho bisogno di fare anche un update (copio da market lb19)
        attachListeners();  //TODO: TEMPORANEO, non valido quando si carica un gioco dal salvataggio
        updateLeaderHandToDiscard();
        try {
            if (getLocalPlayerBoard().isInkwell()) {
                Platform.runLater(() -> ((PlayerBoardController) guiApplication.
                        getController(SceneNames.PLAYER_BOARD)).enableInkwell());
            }
        }catch(NotExistingNicknameException e){
            e.printStackTrace();
        }
        //altro?

        //???
        Platform.runLater(() -> ((PlayerBoardController)guiApplication.
                getController(SceneNames.PLAYER_BOARD)).disableActivateProductionsAction());
        Platform.runLater(() -> ((PlayerBoardController) guiApplication.
                getController(SceneNames.PLAYER_BOARD)).disableActivateProductionsAction());
        Platform.runLater(() -> ((MarketBoardController) guiApplication.
                getController(SceneNames.MARKET_BOARD)).disableMarketAction());
        Platform.runLater(() -> ((DecksBoardController) guiApplication.
                getController(SceneNames.DECKS_BOARD)).disableBuyCardAction());
        Platform.runLater(() -> guiApplication.setActiveScene(SceneNames.PLAYER_BOARD));
        Platform.runLater(() -> guiApplication.setActiveScene(SceneNames.LEADER_CHOICE_MENU));
    }

    @Override
    public void askResourceEqualize(List<Resource> resources) {
        resourcesToEqualize.addAll(resources);
        checkFaithResource(resourcesToEqualize);
    }

    @Override
    public void notifyStartTurn(String nickname) {
        Platform.runLater(() -> ((PlayerBoardController) guiApplication.
                getController(SceneNames.PLAYER_BOARD)).enableButtons());
    }

    @Override
    public void addMarketResources(List<Resource> resources, List<ResourceType> availableAbilities) {
        resourcesToPlace.clear();
        resourcesToPlace.addAll(resources);  //TODO: a volte lancia un'eccezione perchè: "Cannot invoke "java.util.Collection.toArray()" because "c" is null"
        resolveAbilityMarble(availableAbilities);
        boolean faith = checkFaithResource(resourcesToPlace);
        if(!checkOnlyWildcard(resourcesToPlace)) {
            Platform.runLater(() -> ((PlayerBoardController)guiApplication.getController(SceneNames.PLAYER_BOARD))
                    .showAlert(Alert.AlertType.INFORMATION, "Success!", "Resources taken",
                            "Now you need to place each taken resource"));
            updateResourcesToPlace();
        }
        else if(faith) {
            Platform.runLater(() -> ((PlayerBoardController)guiApplication.getController(SceneNames.PLAYER_BOARD))
                .showAlert(Alert.AlertType.INFORMATION, "Success!", "Resources taken",
                        "Your faith has been updated"));
            Platform.runLater(() -> ((PlayerBoardController)guiApplication.getController(SceneNames.PLAYER_BOARD))
                    .enableButtons());
            try {
                sendShelvesConfigurationMessage(getLocalPlayerBoard().getWarehouse().getShelves(),new ArrayList<>());
            } catch (NotExistingNicknameException e) {
                e.printStackTrace();
            }
        }
        else {
            Platform.runLater(() -> ((PlayerBoardController)guiApplication.getController(SceneNames.PLAYER_BOARD))
                    .showAlert(Alert.AlertType.INFORMATION, "Success!", "Resources taken",
                            "You've nothing to place"));
            Platform.runLater(() -> ((PlayerBoardController)guiApplication.getController(SceneNames.PLAYER_BOARD))
                    .enableButtons());
        }
    }

    @Override
    public void notifyLorenzoCard(LorenzoCard lorenzoCard) {

    }

    @Override
    public void notifyLastRound() {

    }

    @Override
    public void notifyEndGame(List<Player> players) {

    }

    @Override
    public void selectMarket() {

    }

    @Override
    public void selectDevDecks() {

    }

    @Override
    public void selectProductions() {

    }

    @Override
    public void placeResourcesOnShelves(List<Resource> resources) {

    }

    @Override
    public List<RequestResources> chooseStorages(List<Resource> resources) {
        return null;
    }

    @Override
    public void setMarket(MarketView market) {
        super.setMarket(market);
        //updateMarket();
        Platform.runLater(() -> ((MarketBoardController)guiApplication.getController(SceneNames.MARKET_BOARD))
                .setMarket(market));
    }

    @Override
    public void setDevelopmentDecks(List<DevelopmentDeckView> developmentDecks) {
        super.setDevelopmentDecks(developmentDecks);
        updateDevDecks();
    }


    public void sendNickname(String nickname){
        getMessageHandler().sendClientMessage(new ConnectionMessage(nickname));
        setNickname(nickname);
        ((PlayerBoardController)guiApplication.getController(SceneNames.PLAYER_BOARD)).setPlayer_label(nickname);
    }

    public void setLobbySize(int size){
        setNumberOfPlayers(size);
        getMessageHandler().sendClientMessage(new NewLobbyMessage(size));
        if(size==1)
            guiApplication.setActiveScene(SceneNames.LOADING);
        else
            guiApplication.setActiveScene(SceneNames.MULTI_PLAYER_WAITING);
    }

    /*public void updateMarket(){
        List<MarbleColors> marbleColors = new ArrayList<>();
        for(int row = 0; row < MARKET_ROW_SIZE.value(); row++)
            for(int col = 0; col < MARKET_COLUMN_SIZE.value(); col++)
                marbleColors.add(getMarket().getMarbleMatrix()[row][col].getColor());
        Platform.runLater(() -> ((MarketBoardController)guiApplication.getController(SceneNames.MARKET_BOARD))
                .updateMarket(marbleColors, getMarket().getFloatingMarble().getColor()));
    }*/

    public void updateDevDecks(){
        List<Integer> listID = new ArrayList<>();
        for(DevelopmentDeckView deck : getDevelopmentDecks())
            listID.add(deck.getDeck().get(0).getID());
        Platform.runLater(() -> ((DecksBoardController)guiApplication.getController(SceneNames.DECKS_BOARD))
                .updateDevDecks(listID));
    }

    public void updateLeaderHandToDiscard(){
        try {
            List<Integer> listID = new ArrayList<>();
            for(Card card : getLocalPlayerBoard().getLeaderBoard().getHand())
                listID.add(card.getID());
            Platform.runLater(() -> ((FirstPhaseController)guiApplication.getController(SceneNames.LEADER_CHOICE_MENU))
                    .setLeaders(listID));
        } catch (NotExistingNicknameException e) {
            e.printStackTrace();
        }
    }

    public void sendLeaderCardDiscardMessage(List<Integer> indexes){
        try {
            List<LeaderCard> leadersToDiscard = new ArrayList<>();
            for(int index : indexes)
                leadersToDiscard.add((LeaderCard) getLocalPlayerBoard().getLeaderBoard().getHand().get(index));
            getMessageHandler().sendClientMessage(new LeaderCardDiscardMessage(leadersToDiscard, true));
            guiApplication.closePopUpStage();
            callAskResourceToEqualize();
        } catch (NotExistingNicknameException e) {
            e.printStackTrace();
        }
    }

    public void sendLeaderCardDiscardMessage(int index){
        List<Integer> oneElementList = new ArrayList<>();
        oneElementList.add(index);
        sendLeaderCardDiscardMessage(oneElementList);
    }

    private void callAskResourceToEqualize(){
        int size = 0;
        for(Resource resource : resourcesToEqualize)
            if(resource.getResourceType() != ResourceType.FAITH)
                size += resource.getQuantity();
        String title;
        if(size > 0) {
            if(size > 1) {
                title = "Choose two resources to take";
            }
            else{
                title = "Choose one resource to take";
            }
            int finalSize = size;
            Platform.runLater(() -> ((WildcardResolverController) guiApplication.
                    getController(SceneNames.RESOURCE_CHOICE_MENU)).setTotalResources(finalSize));
            Platform.runLater(() -> ((WildcardResolverController) guiApplication.
                    getController(SceneNames.RESOURCE_CHOICE_MENU)).setChooseResources_label(title));
            Platform.runLater(() -> guiApplication.setActiveScene(SceneNames.RESOURCE_CHOICE_MENU));
        }
    }

    public void sendShelvesConfigurationMessage(List<Shelf> shelves, List<Resource> toDiscard){
        //TODO: da inserire modo per creare lista di shelf dal nostro wh e una lista do shelves
        resourcesToDiscard.addAll(toDiscard);
        getMessageHandler().sendClientMessage(new ShelvesConfigurationMessage(shelves, resourcesToPlace, resourcesToDiscard)); //TODO: giusto?
        //TODO: ripulire resourcesToDiscard e resourcesToPlace?
        resourcesToPlace.clear();
        resourcesToDiscard.clear();
    }

    private boolean checkFaithResource(List<Resource> resources){
        int faithQuantity=0;
        for(Resource resource : resources){
            if(resource.getResourceType() == ResourceType.FAITH)
                faithQuantity++;
        }
        if (faithQuantity>0) {
            resourcesToDiscard.add(new Resource(ResourceType.FAITH, faithQuantity));
            return true;
        }
        return false;
    }

    public void sendMarketMessage(int row, int col){
        getMessageHandler().sendClientMessage(new SelectMarketMessage(row, col));
    }

    public List<Shelf> getWarehouseShelvesCopy(){
        List<Shelf> shelvesCopy = new ArrayList<>();
        try {
            for(Shelf shelf : getLocalPlayerBoard().getWarehouse().getShelves())
                shelvesCopy.add(new Shelf(shelf.getResourceType(), new Resource(shelf.getResources().getResourceType(),
                        shelf.getResources().getQuantity()), shelf.getLevel(), shelf.isLeader()));
        } catch (NotExistingNicknameException e) {
            e.printStackTrace();
        }
        return shelvesCopy;
    }

    public void updateResourcesToPlace(){
        for(Resource resource : resourcesToPlace){
            Platform.runLater(() -> ((PlayerBoardController)guiApplication.getController(SceneNames.PLAYER_BOARD))
                    .setResToPlaceQuantity(resource.getResourceType(),resource.getQuantity()));
        }
    }

    public void updateWarehouse(){
        Platform.runLater(() -> {
            try {
                ((PlayerBoardController)guiApplication.getController(SceneNames.PLAYER_BOARD))
                        .setWarehouse(getLocalPlayerBoard().getWarehouse().getShelves());
            } catch (NotExistingNicknameException e) {
                e.printStackTrace();
            }
        });
    }

    public void setResourcesToPlace(List<Resource> resources){
        resourcesToPlace=resources;
    }

    public void sendEndTurnMessage(){
        getMessageHandler().sendClientMessage(new EndTurnMessage());
    }

    public void sendLeaderMessage(List<Integer> positions, boolean toActivate){
        List<LeaderCard> leaderHand = new ArrayList<>(), leaderCards = new ArrayList<>();
        try {
            for(Card card : getLocalPlayerBoard().getLeaderBoard().getHand())
                leaderHand.add((LeaderCard)card);
        } catch (NotExistingNicknameException e) {
            e.printStackTrace();
        }
        if (positions.size() == 1 && leaderHand.size()>1)
            leaderCards.add(leaderHand.get(positions.get(0)));
        else if (positions.size() == 1 && leaderHand.size()==1)
            leaderCards.add(leaderHand.get(0));
        else
            leaderCards.addAll(leaderHand);
        if(toActivate)
            getMessageHandler().sendClientMessage(new LeaderCardPlayMessage(leaderCards));
        else
            getMessageHandler().sendClientMessage(new LeaderCardDiscardMessage(leaderCards));
    }

    public int getLeaderShelfPosition(ResourceType resourceType){
        try {
            List<Shelf> shelves = getLocalPlayerBoard().getWarehouse().getShelves();
            for(int i=0; i<shelves.size();i++)
                if(shelves.get(i).isLeader() && shelves.get(i).getResourceType()==resourceType)
                    return i;
        } catch (NotExistingNicknameException e) {
            e.printStackTrace();
        }
        return -1;
    }

    private void resolveAbilityMarble(List<ResourceType> availableAbilities){
        //TODO: fare
    }

    private boolean checkOnlyWildcard(List<Resource> resources){
        boolean onlyWildcard = true;
        for(Resource resource : resources)
            if(resource.getResourceType() != ResourceType.WILDCARD && resource.getResourceType() != ResourceType.FAITH) {
                onlyWildcard = false;
                break;
            }
        return onlyWildcard;
    }
}
