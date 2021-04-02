package it.polimi.ingsw.model.games;

import it.polimi.ingsw.model.boards.LorenzoBoard;

public class SingleGame extends Game{

    private LorenzoBoard lorenzoBoard;
    private boolean isLorenzoTurn;


    public SingleGame(){

    }


    @Override
    public void initGame(){

    }

    @Override
    public void loadNextTurn(){

    }

    @Override
    void checkFaith(){

    }

    //aggiunge a lorenzo la fede per le risorse scartate
    @Override
    public void addFaithAll(int quantity){}

    //controlla anche se Lorenzo ha finito il game (faith al massimo o mazzetti finiti)
    @Override
    boolean checkEndGame(){
        return false;
    }

    //solo per il player
    @Override
    public int[] calculateTotalVP() {
        return null;
    }

    //dice solo se è primo o secondo
    @Override
    public int[] calculateFinalPositions() {
        return null;
    }
}
