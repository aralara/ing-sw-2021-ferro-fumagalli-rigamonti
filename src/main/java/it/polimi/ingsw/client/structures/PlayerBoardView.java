package it.polimi.ingsw.client.structures;

import it.polimi.ingsw.server.model.storage.Production;
import it.polimi.ingsw.server.model.storage.ResourceType;

import java.util.ArrayList;
import java.util.List;

public class PlayerBoardView {

    private String nickname;
    private DevelopmentBoardView developmentBoard;
    private LeaderBoardView leaderBoard;
    private FaithBoardView faithBoard;
    private WarehouseView warehouse;
    private StrongboxView strongbox;
    private boolean inkwell;
    private Production basicProduction;  //TODO: come la gestiamo? con gli observer? per ora la metto qui un po a caso

    private List<Production> activeAbilityProductions;
    private List<ResourceType> activeAbilityMarbles;
    private List<ResourceType> activeAbilityDiscounts;


    public PlayerBoardView(String nickname, DevelopmentBoardView developmentBoard, LeaderBoardView leaderBoard,
                           FaithBoardView faithBoard, WarehouseView warehouse, StrongboxView strongbox,
                           boolean inkwell) {
        this.nickname = nickname;
        this.developmentBoard = developmentBoard;
        this.leaderBoard = leaderBoard;
        this.faithBoard = faithBoard;
        this.warehouse = warehouse;
        this.strongbox = strongbox;
        this.inkwell = inkwell;
        this.activeAbilityProductions = new ArrayList<>();
        this.activeAbilityMarbles = new ArrayList<>();
        this.activeAbilityDiscounts = new ArrayList<>();
        this.basicProduction = new Production();
    }

    public PlayerBoardView(String nickname) {
        this.nickname = nickname;
        developmentBoard = new DevelopmentBoardView();
        leaderBoard = new LeaderBoardView();
        faithBoard = new FaithBoardView();
        warehouse = new WarehouseView();
        strongbox = new StrongboxView();
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
     * Sets the developmentBoard attribute
     * @param developmentBoard New attribute value
     */
    public void setDevelopmentBoard(DevelopmentBoardView developmentBoard) {
        this.developmentBoard = developmentBoard;
    }

    /**
     * Gets the leaderBoard attribute
     * @return Returns leaderBoard
     */
    public LeaderBoardView getLeaderBoard() {
        return leaderBoard;
    }

    /**
     * Sets the leaderBoard attribute
     * @param leaderBoard New attribute value
     */
    public void setLeaderBoard(LeaderBoardView leaderBoard) {
        this.leaderBoard = leaderBoard;
    }

    /**
     * Gets the faithBoard attribute
     * @return Returns faithBoard
     */
    public FaithBoardView getFaithBoard() {
        return faithBoard;
    }

    /**
     * Sets the faithBoard attribute
     * @param faithBoard New attribute value
     */
    public void setFaithBoard(FaithBoardView faithBoard) {
        this.faithBoard = faithBoard;
    }

    /**
     * Gets the warehouse attribute
     * @return Returns warehouse
     */
    public WarehouseView getWarehouse() {
        return warehouse;
    }

    /**
     * Sets the warehouse attribute
     * @param warehouse New attribute value
     */
    public void setWarehouse(WarehouseView warehouse) {
        this.warehouse = warehouse;
    }

    /**
     * Gets the strongbox attribute
     * @return Returns strongbox
     */
    public StrongboxView getStrongbox() {
        return strongbox;
    }

    /**
     * Sets the strongbox attribute
     * @param strongbox New attribute value
     */
    public void setStrongbox(StrongboxView strongbox) {
        this.strongbox = strongbox;
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
