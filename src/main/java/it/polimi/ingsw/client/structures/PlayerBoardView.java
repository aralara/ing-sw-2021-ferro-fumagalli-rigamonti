package it.polimi.ingsw.client.structures;

public class PlayerBoardView {

    private String nickname;
    private DevelopmentBoardView developmentBoard;
    private LeaderBoardView leaderBoard;
    private FaithBoardView faithBoard;
    private WarehouseView warehouse;
    private StrongboxView strongbox;
    private boolean inkwell;


    public PlayerBoardView() {
        developmentBoard = new DevelopmentBoardView();
        leaderBoard = new LeaderBoardView();
        faithBoard = new FaithBoardView();
        warehouse = new WarehouseView();
        strongbox = new StrongboxView();
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
}
