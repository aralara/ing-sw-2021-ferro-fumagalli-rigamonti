package it.polimi.ingsw.server.view;

import it.polimi.ingsw.client.cli.GraphicalCLI;
import it.polimi.ingsw.server.controller.GameHandler;
import it.polimi.ingsw.utils.messages.Message;
import it.polimi.ingsw.utils.messages.server.action.EndGameMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

/**
 * Class that represents a server side view and communicates with the client
 */
public class VirtualView implements Runnable {

    private final Socket client;
    private final ObjectOutputStream output;
    private final ObjectInputStream input;

    private boolean active;
    private String nickname;
    private GameHandler gameHandler;


    /**
     * VirtualView constructor given the player's nickname, input and output streams
     *
     * @param out      Object output stream
     * @param in       Object input stream
     * @param nickname Nickname of the player
     */
    public VirtualView(ObjectOutputStream out, ObjectInputStream in, String nickname) {
        this.nickname = nickname;
        this.client = null;
        this.output = out;
        this.input = in;
        this.active = false;
    }

    /**
     * VirtualView constructor given the player's nickname, client's socket, input and output streams
     *
     * @param client   Socket of the client
     * @param out      Object output stream
     * @param in       Object input stream
     * @param nickname Nickname of the player
     */
    public VirtualView(Socket client, ObjectOutputStream out, ObjectInputStream in, String nickname) {
        this.nickname = nickname;
        this.client = client;
        this.output = out;
        this.input = in;
        this.active = false;
    }


    @Override
    public void run() {
        active = true;
        try {
            handleClientConnection();
        } catch (IOException e) {
            if (client != null)
                GraphicalCLI.printlnString("Client " + client.getInetAddress() + ":" +
                        client.getLocalPort() + ":" + client.getPort() + " connection dropped");
            stop(true, false);
        }
    }

    /**
     * Waits for a message to arrive and handles it
     *
     * @throws IOException Throws an IOException if there is an error during the input read action
     */
    private void handleClientConnection() throws IOException {
        Object message;
        try {
            while (active) {
                message = input.readObject();
                onMessageReceived((Message) message);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends a message to the client
     * @param message Message that will be sent
     * @return Returns true if the message was sent successfully, false otherwise
     */
    public boolean sendMessage(Message message) {
        try {
            output.writeObject(message);
            output.reset();
            output.flush();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Calls the GameHandler to elaborate a received message
     *
     * @param message The received message
     */
    public void onMessageReceived(Message message) {
        getGameHandler().handleMessage(this, message);
    }

    /**
     * Interrupts the connection, closing the streams and propagating the message accordingly to the propagate parameter
     *
     * @param propagate   If true calls the stop method on the GameHandler
     * @param sendMessage If true sends a message to the client, notifying them of the disconnection
     */
    public void stop(boolean propagate, boolean sendMessage) {
        if (active) {
            if (sendMessage)
                sendMessage(new EndGameMessage(null, true));
            active = false;
            try {
                if (client != null && !client.isClosed()) {
                    TimeUnit.MILLISECONDS.sleep(500);
                    output.close();
                    input.close();
                    client.close();
                }
            } catch (IOException | InterruptedException e) {
                GraphicalCLI.printlnString("Fatal Error! Unable to close socket");
            }
        }
        if (propagate)
            getGameHandler().stop(true);
    }

    /**
     * Gets the nickname attribute
     *
     * @return Returns nickname value
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * Sets the nickname attribute
     *
     * @param nickname New attribute value
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * Gets the gameHandler attribute
     *
     * @return Returns gameHandler value
     */
    public GameHandler getGameHandler() {
        return gameHandler;
    }

    /**
     * Sets the gameHandler attribute
     *
     * @param gameHandler New attribute value
     */
    public void setGameHandler(GameHandler gameHandler) {
        this.gameHandler = gameHandler;
    }
}
