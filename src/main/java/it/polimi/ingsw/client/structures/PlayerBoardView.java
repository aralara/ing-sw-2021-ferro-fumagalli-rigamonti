package it.polimi.ingsw.client.structures;

import it.polimi.ingsw.server.model.boards.PlayerBoard;
import it.polimi.ingsw.server.model.storage.Production;
import it.polimi.ingsw.server.model.storage.Resource;
import it.polimi.ingsw.server.model.storage.ResourceType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PlayerBoardView implements Serializable {

    private String nickname;
    private final DevelopmentBoardView developmentBoard;
    private final LeaderBoardView leaderBoard;
    private final FaithBoardView faithBoard;
    private final WarehouseView warehouse;
    private final StrongboxView strongbox;
    private boolean inkwell;
    private Production basicProduction;

    private List<Production> activeAbilityProductions;
    private List<ResourceType> activeAbilityMarbles;
    private List<ResourceType> activeAbilityDiscounts;


    public PlayerBoardView(PlayerBoard playerBoard) {
        this.nickname = playerBoard.getPlayer().getNickname();
        this.developmentBoard = new DevelopmentBoardView(playerBoard.getDevelopmentBoard());
        this.leaderBoard = new LeaderBoardView(playerBoard.getLeaderBoard());
        this.faithBoard = new FaithBoardView(playerBoard.getFaithBoard());
        this.warehouse = new WarehouseView(playerBoard.getWarehouse());
        this.strongbox = new StrongboxView(playerBoard.getStrongbox());
        this.inkwell = playerBoard.isFirstPlayer();
        this.activeAbilityProductions = playerBoard.getAbilityProductions();
        this.activeAbilityDiscounts = playerBoard.getAbilityDiscounts();
        this.activeAbilityMarbles = playerBoard.getAbilityMarbles();
        this.basicProduction = new Production();
        this.basicProduction = new Production(
                new ArrayList<>(List.of(new Resource(ResourceType.WILDCARD,1))),
                new ArrayList<>(List.of(
                        new Resource(ResourceType.COIN,100),
                        new Resource(ResourceType.STONE,100),
                        new Resource(ResourceType.SHIELD,100),
                        new Resource(ResourceType.SERVANT,100))));
        //TODO: inserita per testare, da togliere quando la CLI sarà PERFETTA (come Bonucci)
    }


    /**
     * Gets the nickname attribute
     * @return Returns nickname
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * Sets the nickname attribute
     * @param nickname New attribute value
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * Gets the developmentBoard attribute
     * @return Returns developmentBoard
     */
    public DevelopmentBoardView getDevelopmentBoard() {
        return developmentBoard;
    }

    /**
     * Gets the leaderBoard attribute
     * @return Returns leaderBoard
     */
    public LeaderBoardView getLeaderBoard() {
        return leaderBoard;
    }

    /**
     * Gets the faithBoard attribute
     * @return Returns faithBoard
     */
    public FaithBoardView getFaithBoard() {
        return faithBoard;
    }

    /**
     * Gets the warehouse attribute
     * @return Returns warehouse
     */
    public WarehouseView getWarehouse() {
        return warehouse;
    }

    /**
     * Gets the strongbox attribute
     * @return Returns strongbox
     */
    public StrongboxView getStrongbox() {
        return strongbox;
    }

    /**
     * Gets the inkwell attribute
     * @return Returns inkwell
     */
    public boolean isInkwell() {
        return inkwell;
    }

    /**
     * Sets the inkwell attribute
     * @param inkwell New attribute value
     */
    public void setInkwell(boolean inkwell) {
        this.inkwell = inkwell;
    }

    /**
     * Gets the activeAbilityProductions attribute
     * @return Returns activeAbilityProductions
     */
    public List<Production> getActiveAbilityProductions() {
        return activeAbilityProductions;
    }

    /**
     * Sets the activeAbilityProductions attribute
     * @param activeAbilityProductions New attribute value
     */
    public void setActiveAbilityProductions(List<Production> activeAbilityProductions) {
        this.activeAbilityProductions = activeAbilityProductions;
    }

    /**
     * Gets the activeAbilityMarbles attribute
     * @return Returns activeAbilityMarbles
     */
    public List<ResourceType> getActiveAbilityMarbles() {
        return activeAbilityMarbles;
    }

    /**
     * Sets the activeAbilityMarbles attribute
     * @param activeAbilityMarbles New attribute value
     */
    public void setActiveAbilityMarbles(List<ResourceType> activeAbilityMarbles) {
        this.activeAbilityMarbles = activeAbilityMarbles;
    }

    /**
     * Gets the activeAbilityDiscounts attribute
     * @return Returns activeAbilityDiscounts
     */
    public List<ResourceType> getActiveAbilityDiscounts() {
        return activeAbilityDiscounts;
    }

    /**
     * Sets the activeAbilityDiscounts attribute
     * @param activeAbilityDiscounts New attribute value
     */
    public void setActiveAbilityDiscounts(List<ResourceType> activeAbilityDiscounts) {
        this.activeAbilityDiscounts = activeAbilityDiscounts;
    }

    /**
     * Gets the basicProduction attribute
     * @return Returns basicProduction
     */
    public Production getBasicProduction() {
        return basicProduction;
    }
}
