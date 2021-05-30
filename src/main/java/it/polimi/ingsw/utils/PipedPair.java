package it.polimi.ingsw.utils;

import java.io.PipedInputStream;
import java.io.PipedOutputStream;

public class PipedPair {

    private final PipedOutputStream pipeOut;
    private final PipedInputStream pipeIn;


    public PipedPair() {
        this.pipeOut = new PipedOutputStream();
        this.pipeIn = new PipedInputStream();
    }


    public PipedOutputStream getPipeOut() {
        return pipeOut;
    }

    public PipedInputStream getPipeIn() {
        return pipeIn;
    }
}
