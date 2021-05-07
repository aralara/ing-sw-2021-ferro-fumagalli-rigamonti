package it.polimi.ingsw.client.structures;

public class MarketView {

    private static final int ROWS = 3; //TODO: servono?
    private static final int COLUMNS = 4;

    private MarbleView[][] marbleMatrix;
    private MarbleView floatingMarble;


    public MarketView(MarbleView[][] marbleMatrix, MarbleView floatingMarble){
        this.marbleMatrix = new MarbleView[ROWS][COLUMNS];
        setMarbleMatrix(marbleMatrix);
        this.floatingMarble = floatingMarble;
    }


    /**
     * Gets the marbleMatrix attribute
     * @return Returns a copy of marbleMatrix
     */
    public MarbleView[][] getMarbleMatrix() {
        MarbleView[][] marbleMatrixCopy = new MarbleView[ROWS][COLUMNS];
        for(int r=0; r<ROWS; r++)
            for(int c=0; c<COLUMNS; c++)
                marbleMatrixCopy[r][c]=marbleMatrix[r][c];
        return marbleMatrixCopy;
    }

    /**
     * Sets the marbleMatrix attribute
     * @param marbleMatrix New attribute value
     */
    public void setMarbleMatrix(MarbleView[][] marbleMatrix) {
        for(int r=0; r<ROWS; r++)
            for(int c=0; c<COLUMNS; c++)
                this.marbleMatrix[r][c]=marbleMatrix[r][c];
    }

    /**
     * Gets the floatingMarble attribute
     * @return Returns floatingMarble
     */
    public MarbleView getFloatingMarble() {
        return floatingMarble;
    }

    /**
     * Sets the floatingMarble attribute
     * @param floatingMarble New attribute value
     */
    public void setFloatingMarble(MarbleView floatingMarble) {
        this.floatingMarble = floatingMarble;
    }
}
