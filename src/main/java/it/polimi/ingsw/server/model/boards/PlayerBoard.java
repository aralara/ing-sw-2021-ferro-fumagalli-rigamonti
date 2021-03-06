package it.polimi.ingsw.server.model.boards;

import it.polimi.ingsw.utils.exceptions.InvalidSpaceException;
import it.polimi.ingsw.server.model.cards.card.*;
import it.polimi.ingsw.server.model.cards.requirement.Requirement;
import it.polimi.ingsw.server.model.faith.FaithTrack;
import it.polimi.ingsw.server.model.storage.*;
import it.polimi.ingsw.utils.listeners.server.PlayerListened;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Handles methods to update the playerBoard
 */
public class PlayerBoard extends PlayerListened implements Serializable {

    private final Player player;
    private final DevelopmentBoard developmentBoard;
    private final LeaderBoard leaderBoard;
    private final FaithBoard faithBoard;
    private final Warehouse warehouse;
    private final Strongbox strongbox;
    private boolean inkwell;
    private boolean turnPlayed;
    private final Production basicProduction;

    private final List<Production> activeAbilityProductions;
    private final List<ResourceType> activeAbilityMarbles;
    private final List<ResourceType> activeAbilityDiscounts;

    private List<Resource> equalizedResources;


    /**
     * PlayerBoard constructor that, given the nickname of its player, initializes all of its components
     * @param player Nickname of the player
     */
    public PlayerBoard(String player) {
        this.player = new Player(player);
        this.developmentBoard = new DevelopmentBoard();
        this.leaderBoard = new LeaderBoard();
        this.faithBoard = new FaithBoard();
        this.warehouse = new Warehouse();
        this.strongbox = new Strongbox();
        this.inkwell = false;
        this.turnPlayed = false;
        this.basicProduction = new Production();
        this.activeAbilityProductions = new ArrayList<>();
        this.activeAbilityMarbles = new ArrayList<>();
        this.activeAbilityDiscounts = new ArrayList<>();
        this.equalizedResources = new ArrayList<>();
    }


    /**
     * Creates a list containing all of the player resources
     * @return Returns a list of resources
     */
    public List<Resource> createResourceStock() {
        return Storage.mergeResourceList(warehouse.getList(), strongbox.getList());
    }

    /**
     * Creates a list containing all of the player productions
     * @return Returns a list of productions
     */
    public List<Production> createProductionStock() {
        return Stream.of(
                List.of(basicProduction).stream(),
                activeAbilityProductions.stream(),
                developmentBoard.getActiveProductions().stream()
        ).reduce(Stream::concat).orElseGet(Stream::empty).collect(Collectors.toList());
    }

    /**
     * Calculates total VPs for the player checking the FaithTrack, leader cards, development cards and resources and
     * saves the result to the player attribute
     * @param faithTrack FaithTrack reference to calculate faith-based VPs
     */
    public void calculateVP(FaithTrack faithTrack) {
        player.setTotalVP(
                faithBoard.calculateVP(faithTrack) +
                leaderBoard.calculateVP() +
                developmentBoard.calculateVP() +
                (int) Math.floor((double) createResourceStock().stream().mapToInt(Resource::getQuantity).sum() / 5)
        );
    }

    /**
     * Puts a development card at the top of one of the spaces specified by the parameter
     * @param card The development card to be added
     * @param space Position of the space on the board
     * @param requests List of requests containing resource quantity and location
     * @return Returns true if the card is bought, false otherwise
     */
    public boolean buyDevCard(DevelopmentCard card, int space, List<RequestResources> requests) {
        boolean bought = canTakeFromStorages(requests);
        if(bought) {
            takeFromStorages(requests);
            try {
                developmentBoard.addDevCard(card, space);
            } catch (InvalidSpaceException e) {
                e.printStackTrace();
            }
        }
        return bought;
    }

    /**
     * If the resources contained in the requests match the actual available resources detained by the current player,
     * activates the productions for said player by adding the produced resources to their Strongbox
     * @param produced List of resources to produce
     * @param requests List of requests containing resource quantity and location for the spent resources
     * @return Returns true if the productions can be activated, false otherwise
     */
    public boolean activateProductions(List<Resource> produced, List<RequestResources> requests) {
        boolean activated = canTakeFromStorages(requests);
        if(activated) {
            takeFromStorages(requests);
            strongbox.addResources(produced);
        }
        return activated;
    }

    /**
     * Checks if the resources from the Storages specified by the RequestResources can be taken
     * @param requests List of requests containing resource quantity and location for the spent resources
     * @return Returns true if the resources can be taken, false otherwise
     */
    public boolean canTakeFromStorages(List<RequestResources> requests) {
        boolean canTake = true;
        List<Resource> containerList;
        for(int i = 0; i < requests.size() && canTake; i++){
            RequestResources request = requests.get(i);
            if (request.getStorageType() == StorageType.STRONGBOX)
                containerList = strongbox.getList();
            else if (request.getStorageType() == StorageType.WAREHOUSE)
                containerList = warehouse.getList(false);
            else
                containerList = warehouse.getList(true);
            canTake = Storage.checkContainedResources(containerList, request.getList());
        }
        return canTake;
    }

    /**
     * Takes the resources from the Storages specified by the RequestResources if all the requests are valid
     * @param requests List of requests containing resource quantity and location for the spent resources
     */
    private void takeFromStorages(List<RequestResources> requests) {
        for(RequestResources request : requests) {
            if (request.getStorageType() == StorageType.STRONGBOX)
                strongbox.removeResources(request.getList());
            else if (request.getStorageType() == StorageType.LEADER)
                warehouse.removeResources(request.getList(),true);
            else if (request.getStorageType() == StorageType.WAREHOUSE)
                warehouse.removeResources(request.getList(),false);
        }
    }

    /**
     * Plays a LeaderCard from the hand to the board
     * @param leaderCard LeaderCard to be played
     * @return Returns true if the LeaderCard can be played, false otherwise
     */
    public boolean playLeaderCard(LeaderCard leaderCard) {
        boolean canPlay = false, flag = true;
        for(Requirement req : leaderCard.getRequirements()) {
            if(flag) {
                canPlay = req.checkRequirement(this);
                if(!canPlay)
                    flag = false;
            }
        }
        if(canPlay) {
            leaderCard.getAbility().activateAbility(this);
            leaderBoard.playLeaderHand(leaderCard);
        }
        return canPlay;
    }

    /**
     * Gets the player attribute
     * @return Returns player value
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Gets the developmentBoard attribute
     * @return Returns developmentBoard value
     */
    public DevelopmentBoard getDevelopmentBoard() {
        return developmentBoard;
    }

    /**
     * Gets the leaderBoard attribute
     * @return Returns leaderBoard value
     */
    public LeaderBoard getLeaderBoard() {
        return leaderBoard;
    }

    /**
     * Gets the faithBoard attribute
     * @return Returns faithBoard value
     */
    public FaithBoard getFaithBoard() {
        return faithBoard;
    }

    /**
     * Gets the warehouse attribute
     * @return Returns warehouse value
     */
    public Warehouse getWarehouse() {
        return this.warehouse;
    }

    /**
     * Gets the strongbox attribute
     * @return Returns strongbox value
     */
    public Strongbox getStrongbox() {
        return this.strongbox;
    }

    /**
     * Sets the current PlayerBoard as the first playing one
     */
    public void firstPlayer() {
        this.inkwell = true;
    }

    /**
     * Gets the inkwell attribute
     * @return Returns inkwell value
     */
    public boolean isFirstPlayer() {
        return inkwell;
    }

    /**
     * Gets the turnPlayed attribute
     * @return Returns turnPlayed value
     */
    public boolean isTurnPlayed() {
        return turnPlayed;
    }

    /**
     * Sets the turnPlayed attribute
     * @param value New turnPlayed value
     */
    public void setTurnPlayed(boolean value) {
        this.turnPlayed = value;
    }

    /**
     * Gets the activeAbilityProductions attribute
     * @return Returns activeAbilityProductions value
     */
    public List<Production> getAbilityProductions() {
        return activeAbilityProductions;
    }

    /**
     * Gets the activeAbilityMarbles attribute
     * @return Returns activeAbilityMarbles value
     */
    public List<ResourceType> getAbilityMarbles() {
        return activeAbilityMarbles;
    }

    /**
     * Gets the activeAbilityDiscounts attribute
     * @return Returns activeAbilityDiscounts value
     */
    public List<ResourceType> getAbilityDiscounts() {
        return activeAbilityDiscounts;
    }

    /**
     * Gets the equalizedResources attribute
     * @return Returns equalizedResources value
     */
    public List<Resource> getEqualizedResources() {
        return equalizedResources;
    }

    /**
     * Sets the equalizedResources attribute
     * @param equalizedResources New equalizedResources value
     */
    public void setEqualizedResources(List<Resource> equalizedResources) {
        this.equalizedResources = equalizedResources;
    }

    /**
     * Resets the equalizedResources attribute
     */
    public void resetEqualizedResources() {
        this.equalizedResources = new ArrayList<>();
    }
}
