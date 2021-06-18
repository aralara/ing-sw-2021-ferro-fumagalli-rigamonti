package it.polimi.ingsw.utils;

import java.io.*;

/**
 * TODO: fare javadoc
 */
public class PipedPair {

    private final PipedOutputStream pipeOut;
    private final PipedInputStream pipeIn;


    /**
     * Constructor with no parameters that creates input and output piped streams
     */
    public PipedPair() {
        this.pipeOut = new PipedOutputStream();
        this.pipeIn = new PipedInputStream();
    }


    /**
     * Creates a new PipedPair and connects it to the current one
     * @return Returns the new connected PipedPair
     * @throws IOException Throws an IOException if the piped streams can't be connected
     */
    public PipedPair connect() throws IOException {
        PipedPair external = new PipedPair();
        external.getPipeIn().connect(pipeOut);
        external.getPipeOut().connect(pipeIn);
        return external;
    }

    /**
     * Gets the pipeOut attribute
     * @return Returns pipeOut
     */
    public PipedOutputStream getPipeOut() {
        return pipeOut;
    }

    /**
     * Gets the pipeIn attribute
     * @return Return pipeIn
     */
    public PipedInputStream getPipeIn() {
        return pipeIn;
    }
}
